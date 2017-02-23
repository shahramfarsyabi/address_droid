package ir.iraddress.www.directories;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.helper.SharedPrefered;

/**
 * Created by shahram on 2/13/17.
 */

public class DirectoryActivity extends MainController {
    public SupportMapFragment mapFragment;
    private int SELECT_FILE = 1;

    public void onCreate(Bundle savedInstanceState) {

        extras = getIntent().getExtras();
        route = "directories/" + extras.get("city_id");
        context = this;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);


        fetchData(1, route, null);
    }

    public void callback(final JSONObject response, int statusCode) {
        try {

            TextView description = (TextView) findViewById(R.id.description);
            description.setText(response.getString("description"));
            description.setTypeface(typeface);

//            TextView title = (TextView) findViewById(R.id.title);
//            title.setText(response.getString("title"));
//            title.setTypeface(typeface);

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
            collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
            collapsingToolbarLayout.setTitle(response.getString("title"));
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorWhite));
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorWhite));


            Button fileBrowser = (Button) findViewById(R.id.file_browser);
            fileBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select file to upload "), SELECT_FILE);
                }
            });

            TextViewIranSans address = (TextViewIranSans) findViewById(R.id.address);
            address.setText(response.getString("address"));
            address.setTypeface(typeface);


            LinearLayout boxPhone = (LinearLayout) findViewById(R.id.directory_box_phone);

            if (!response.getString("phone").isEmpty() && response.getString("phone") != "null") {
                boxPhone.setVisibility(View.VISIBLE);
                TextViewIranSans phone = (TextViewIranSans) findViewById(R.id.directory_phone);
                phone.setText(response.getString("phone"));

                Button phoneDialer = (Button) findViewById(R.id.dialer);
                phoneDialer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + response.getString("phone").toString().trim()));
                            context.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            LinearLayout boxWebSite = (LinearLayout) findViewById(R.id.directory_box_website);

            if (!response.getString("webSite").isEmpty() && response.getString("webSite") != "null") {
                boxWebSite.setVisibility(View.VISIBLE);
                TextViewIranSans webSite = (TextViewIranSans) findViewById(R.id.directory_website);
                webSite.setText(response.getString("webSite"));
            }


            LinearLayout boxFax = (LinearLayout) findViewById(R.id.directory_box_fax);

            if (!response.getString("fax").isEmpty() && response.getString("fax") != "null") {
                boxFax.setVisibility(View.VISIBLE);
                TextViewIranSans fax = (TextViewIranSans) findViewById(R.id.directory_fax);
                fax.setText(response.getString("fax"));
            }


            TextViewIranSans owner = (TextViewIranSans) findViewById(R.id.sent_by_owner);
            owner.setText("ارسال شده توسط " + response.getJSONObject("owner").get("fullName"));

            SharedPrefered sharedPrefered = new SharedPrefered(context, "last_viewed");
            sharedPrefered.store(response);

            ImageView imageView = (ImageView) findViewById(R.id.app_bar_image);
//            Picasso.with(context).load(response.getString("image")).fit().centerCrop().into(imageView);

            Button button = (Button) findViewById(R.id.share_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);

                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, response.getString("href"));
                        startActivity(Intent.createChooser(intent, "Share"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            Button allComments = (Button) findViewById(R.id.directory_all_comments);
            Button firstComment = (Button) findViewById(R.id.directory_first_comment);

            if (response.getJSONArray("comments").length() > 0) {

//            Comments Recyclerview
                recyclerView = (RecyclerView) findViewById(R.id.directory_comments);
                recyclerView.setVisibility(View.VISIBLE);
                layoutManager = new LinearLayoutManager(this);
                recyclerViewAdapter = new DirectoryCommentsAdapter(this);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.setLayoutManager(layoutManager);

                allComments.setVisibility(View.VISIBLE);
                firstComment.setVisibility(View.GONE);

            } else {

                recyclerView = (RecyclerView) findViewById(R.id.directory_comments);
                recyclerView.setVisibility(View.GONE);
                allComments.setVisibility(View.GONE);
                firstComment.setVisibility(View.VISIBLE);
            }

//            Similars Recylerview
            recyclerView = (RecyclerView) findViewById(R.id.directory_similar);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerViewAdapter = new DirectorySimilarsAdapter(this);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(layoutManager);


            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    try {
                        LatLng location = new LatLng(Double.parseDouble(response.getString("latitude")), Double.parseDouble(response.getString("longitude")));

                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));

                        googleMap.addMarker(new MarkerOptions()
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                                .position(location)
                                .title(response.getString("title")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            if (requestCode == SELECT_FILE) {
                System.out.println(selectedImageUri);
            }


        }


    }
}