package ir.iraddress.www.profile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoryCommentActivity;
import ir.iraddress.www.directories.DirectoryCommentsActivity;
import ir.iraddress.www.directories.DirectoryCommentsAdapter;
import ir.iraddress.www.extend.AppButton;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.helper.ArrayUtil;

/**
 * Created by shahram on 4/4/17.
 */

public class MyTripActivity extends ProfileMainActivity implements OnMapReadyCallback{

    AppButton date;
    AppButton rate;
    TextViewIranSans content;
    private GoogleMap mMap;
    private JSONObject trip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        route = "trips/" + extras.getInt("trip_id");
        date = (AppButton) findViewById(R.id.trip_date);
        rate = (AppButton) findViewById(R.id.trip_rate);
        content = (TextViewIranSans) findViewById(R.id.trip_content);
        typeface = Typeface.createFromAsset(context.getAssets(),"fonts/ttf/IRANSansWeb.ttf");


        getRequest(route, params);
    }

    public void callback(JSONObject response, int statusCode, String method) {

        switch(statusCode){
            case 200:
                    switch (method){
                        case "GET":
                            try {

                                CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
                                collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
                                collapsingToolbarLayout.setTitle(response.getString("title"));
                                collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                                collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

                                SliderLayout slider = (SliderLayout) findViewById(R.id.slider);

                                Display display =getWindowManager().getDefaultDisplay();
                                android.view.ViewGroup.LayoutParams layoutParams = slider.getLayoutParams();
                                layoutParams.width = display.getWidth();
                                layoutParams.height = (int) ((display.getWidth()*56.25)/100);
                                slider.setLayoutParams(layoutParams);

                                if(response.getJSONArray("images").length() > 0){

                                    for(int n = 0; n < response.getJSONArray("images").length(); n++){

                                        JSONObject photo = (JSONObject) response.getJSONArray("images").get(n);

                                        DefaultSliderView textSliderView = new DefaultSliderView(this);
                                        // initialize a SliderLayout
                                        textSliderView
                                                .image(photo.getString("href"))
                                                .setScaleType(BaseSliderView.ScaleType.Fit);

                                        slider.addSlider(textSliderView);

                                    }


                                }

                                trip = response;
                                date.setText(response.getString("date"));
                                String rating = "امتیاز "+response.getString("rate")+" از ۵";
                                rate.setText(rating);
                                content.setText(response.getString("content"));

                                if(trip.get("location") instanceof JSONObject){
                                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                                    mapFragment.getMapAsync(this);
                                }

                                Button btnAllComments = (Button) findViewById(R.id.trip_all_comments);
                                Button btnFirstComment = (Button) findViewById(R.id.trip_first_comment);

                                if (response.getJSONArray("comments").length() > 0) {

                                    recyclerView = (RecyclerView) findViewById(R.id.trip_comments);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    layoutManager = new LinearLayoutManager(this);

                                    ArrayList<Object> comments = ArrayUtil.convert(response.getJSONArray("comments"));

                                    recyclerViewAdapter = new DirectoryCommentsAdapter(this, comments);
                                    recyclerView.setAdapter(recyclerViewAdapter);
                                    recyclerView.setLayoutManager(layoutManager);

                                    btnAllComments.setVisibility(View.VISIBLE);
                                    btnFirstComment.setVisibility(View.GONE);

                                } else {

                                    recyclerView = (RecyclerView) findViewById(R.id.trip_comments);
                                    recyclerView.setVisibility(View.GONE);
                                    btnAllComments.setVisibility(View.GONE);
                                    btnFirstComment.setVisibility(View.VISIBLE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney, Australia, and move the camera.
        try {
            mMap = googleMap;

            LatLng location = new LatLng(trip.getJSONObject("location").getDouble("lat"), trip.getJSONObject("location").getDouble("lng"));
            mMap.addMarker(new MarkerOptions().position(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void comments(View view){
        Intent intent = new Intent(this, DirectoryCommentsActivity.class);
        intent.putExtra("route", route+"/comments");
        intent.putExtra("type", "trip");
        intent.putExtra("item_id", extras.getInt("trip_id"));

        startActivity(intent);
    }

    public void btnWriteReview(View view) throws JSONException {
        Intent intent = new Intent(this, DirectoryCommentActivity.class);
        intent.putExtra("type", "trip");
        intent.putExtra("item_id", trip.getInt("id"));
        startActivity(intent);
    }
}
