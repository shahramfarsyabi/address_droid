package ir.iraddress.www;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.helper.MyLocationServiceManager;
import ir.iraddress.www.map.MapItemShowLinearAdapter;

/**
 * Created by shahram on 2/18/17.
 */

public class MapActivity extends MainController implements OnMapReadyCallback {
    private GoogleMap mMap;
    public JSONObject location;
    public int traffic = 0;
    public int page = 0;
    public Button loadMore;
    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        extras = getIntent().getExtras();
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");

        params = new RequestParams();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            MyLocationServiceManager myLocationServiceManager = new MyLocationServiceManager(this);
            location = myLocationServiceManager.getLocation();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_map_items);
        recyclerViewAdapter = new MapItemShowLinearAdapter(this, collection);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        loadMore = (Button) findViewById(R.id.buttonLoadMoreItems);
        loadMore.setTypeface(typeface);


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(32.046699, 54.192378);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        Handler mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(context, "Permisson is Disabled in your device", Toast.LENGTH_SHORT).show();

                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);


                    }else{

//                    mMap.getUiSettings().setZoomControlsEnabled(true);
//                    mMap.getUiSettings().setCompassEnabled(true);
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);

                        LatLng clientLocation = new LatLng(location.getDouble("lat"),location.getDouble("lng"));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(clientLocation)
                                .bearing(45)
                                .tilt(90)
                                .zoom(15)
                                .build();

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(clientLocation).title(""));

                        fetchMoreItems();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, 2000);


    }


    public void changeTrafficeStatus(View view){
        if(mMap != null){
            switch(traffic){
                case 0:
                    mMap.setTrafficEnabled(Boolean.TRUE);
                    traffic = 1;
                    break;

                case 1:
                    mMap.setTrafficEnabled(Boolean.FALSE);
                    traffic = 0;
                    break;
            }
        }
    }

    @Override
    public void callback(JSONObject response, int statusCode) {


        loadMore.setText("مشاهده موارد بیشتر");

        try {
            if(response.length() > 0 && response.has("data")){

                JSONArray data = response.getJSONArray("data");

                for(int n = 0; n < data.length(); n++) {
                    JSONObject object = (JSONObject) data.get(n);
                    System.out.println("arta cheshm");
                    System.out.println(object.getDouble("latitude"));
                    LatLng itemlatLng = new LatLng(object.getDouble("latitude"), object.getDouble("longitude"));
                    mMap.addMarker(new MarkerOptions().position(itemlatLng).title(object.getString("title")));

                    collection.add(object);
                }

                recyclerViewAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadMoreItemOnMap(View view){
        loadMore.setText("در حال بارگزاری ...");
        fetchMoreItems();
    }

    public void fetchMoreItems(){
        if(extras.containsKey("route")){
            route = extras.getString("route");
            params.put("page", ++page);

            try {
                params.put("city_title", location.getString("city"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getRequest(route, params);
        }
    }
}
