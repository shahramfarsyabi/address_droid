package ir.iraddress.www.new_directory;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.ExpandableListAdapter;
import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.helper.HttpRequest;


public class NewDirectoryActivity extends MainController {


    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_directory);
        context = this;
    }

    public void showExpandableCategoriesList(View view){
        final Dialog expandableDialog = new Dialog(this);
        expandableDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        expandableDialog.setContentView(R.layout.dialog_expandable_categories);

        RequestParams requestParams = new RequestParams();
        requestParams.put("withSubCategories", Boolean.TRUE);

        HttpRequest.get("categories", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {

                ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(context, response);
                ExpandableListView expandableListView = (ExpandableListView) expandableDialog.findViewById(R.id.lvExp);
                expandableListView.setAdapter(expandableListAdapter);

                expandableDialog.show();

            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){

            }
        });

    }

    public void showMapDialog(View view){

        final Dialog googleMapDialog = new Dialog(this);
        googleMapDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        googleMapDialog.setContentView(R.layout.dialog_google_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                try {
                    mMap = googleMap;
                    myLocationServiceManager.connect();
                    JSONObject clientLocation = (JSONObject) myLocationServiceManager.getLocation();

                    LatLng sydney = new LatLng(clientLocation.getDouble("lat"), clientLocation.getDouble("lng"));

//                    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        googleMapDialog.show();
    }

    public void showFacilitiesList(View view){

        final Dialog facilitiesDialog = new Dialog(this, R.style.MyDialogSize);
        facilitiesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        facilitiesDialog.setContentView(R.layout.dialog_facilities);

        final RecyclerView recyclerViewFacilities = (RecyclerView) facilitiesDialog.findViewById(R.id.recycler_view_facilities);

        RequestParams requestParams = new RequestParams();
        requestParams.put("withSubCategories", Boolean.TRUE);

        HttpRequest.get("facilities", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {

                FacilitiesAdapter facilitiesAdapter = new FacilitiesAdapter(context, response);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

                recyclerViewFacilities.setAdapter(facilitiesAdapter);
                recyclerViewFacilities.setLayoutManager(linearLayoutManager);

                facilitiesDialog.show();

            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                facilitiesDialog.cancel();
            }
        });


    }
}
