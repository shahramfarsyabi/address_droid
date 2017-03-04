package ir.iraddress.www.directories;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

/**
 * Created by shahram on 3/5/17.
 */

public class DirectoryMapActivity extends MainController implements OnMapReadyCallback{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_directory);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
