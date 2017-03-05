package ir.iraddress.www.directories;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;


public class DirectoryMapActivity extends MainController implements OnMapReadyCallback{
    public GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_directory);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_directory);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(extras.getDouble("lat"), extras.getDouble("lng"));
        mMap.addMarker(new MarkerOptions().position(sydney).title(extras.getString("title")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
    }
}
