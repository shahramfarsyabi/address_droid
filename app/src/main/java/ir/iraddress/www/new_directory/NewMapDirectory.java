package ir.iraddress.www.new_directory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

/**
 * Created by shahram on 3/20/17.
 */

public class NewMapDirectory extends MainController implements OnMapReadyCallback{

    private GoogleMap mMap;
    JSONObject selectedPoint = new JSONObject();
    JSONObject location = new JSONObject();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_map_directory);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myLocationServiceManager.connect();

        if(extras != null && extras.containsKey("location")){

            try {
                location = new JSONObject(extras.getString("location"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{

            try {
                location = myLocationServiceManager.getLocation();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {

            mMap = googleMap;

            LatLng sydney = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
            mMap.addMarker(new MarkerOptions().position(sydney));

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    selectedPoint = new JSONObject();
                    try {

                        selectedPoint.put("lat", point.latitude);
                        selectedPoint.put("lng", point.longitude);
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(point));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void submitMapLocation(View view){

        Intent intent = getIntent();
        intent.putExtra("point", selectedPoint.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void cancelMapLocation(View view){
        finish();
    }
}
