package ir.iraddress.www;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.directories.DirectoryActivity;
import ir.iraddress.www.helper.MyLocationServiceManager;
import ir.iraddress.www.map.MapItemShowLinearAdapter;


public class MapActivity extends MainController implements OnMapReadyCallback {
    private GoogleMap mMap;
    public JSONObject location;
    public int traffic = 0;
    public int page = 0;
    public Button loadMore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        context = this;
        extras = getIntent().getExtras();
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            myLocationServiceManager = new MyLocationServiceManager(this, this);
            location = myLocationServiceManager.getLocation();
            System.out.println(location);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_map_items);
        recyclerViewAdapter = new MapItemShowLinearAdapter(this, this, collection);
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


                    if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE_FOR_FINE_LOCATION);
                        return;

                    }else if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_FOR_FINE_LOCATION);
                        return;

                    }

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
//                    Marker marker = mMap.addMarker(new MarkerOptions().position(clientLocation).title(""));

                    fetchMoreItems();

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
    public void callback(JSONObject response, int statusCode, String method) {


        loadMore.setText("مشاهده موارد بیشتر");

        try {
            if(response.length() > 0 && response.has("data")){

                JSONArray data = response.getJSONArray("data");

                for(int n = 0; n < data.length(); n++) {

                    JSONObject object = (JSONObject) data.get(n);
                    System.out.println(object);
                    LatLng itemlatLng = new LatLng(object.getDouble("latitude"), object.getDouble("longitude"));
                    Marker marker = mMap.addMarker(new MarkerOptions().position(itemlatLng).title(object.getString("title")));
                    marker.setTag(object);

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            try {
                                JSONObject markerData = (JSONObject) marker.getTag();

                                for(int n = 0; n < collection.size(); n++){
                                    JSONObject findItem = (JSONObject) collection.get(n);

                                    if(findItem.getInt("id") == markerData.getInt("id")){
                                        Toast.makeText(context, markerData.getString("id"), Toast.LENGTH_SHORT).show();
//                                        layoutManager.scrollToPosition(n);
                                        recyclerView.scrollToPosition(n);
                                        return false;
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return false;
                        }
                    });

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

    public void showSelectedItem(View view) throws JSONException {

        JSONObject markerData = (JSONObject) view.getTag();

        Intent intent = new Intent(this, DirectoryActivity.class);
        intent.putExtra("directory_id", markerData.getString("id"));
        System.out.println("SHAHRAM DIRECTORY");
        System.out.println(markerData);
        System.out.println(markerData.getString("id"));
        startActivity(intent);
    }

    public void fetchMoreItems(){
        if(extras.containsKey("route")){
            route = extras.getString("route");
            params.put("page", ++page);

            try {
//                Toast.makeText(context, location.getString("city"), Toast.LENGTH_LONG).show();
                params.put("city_title", location.getString("city"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            getRequest(route, params);
        }
    }
}
