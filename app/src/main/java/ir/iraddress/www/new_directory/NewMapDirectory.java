package ir.iraddress.www.new_directory;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

/**
 * Created by shahram on 3/20/17.
 */

public class NewMapDirectory extends MainController implements OnMapReadyCallback{

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_map_directory);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {

            mMap = googleMap;

            myLocationServiceManager.connect();
            JSONObject clientLocation = (JSONObject) myLocationServiceManager.getLocation();

            LatLng sydney = new LatLng(clientLocation.getDouble("lat"), clientLocation.getDouble("lng"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
