package ir.iraddress.www.directories;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;
import ir.iraddress.www.helper.SharedPrefered;

public class DirectoryActivity extends MainController {

    public SupportMapFragment mapFragment;
    private static final int READ_REQUEST_CODE = 42;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        extras = getIntent().getExtras();
        route = "directories/" + extras.get("directory_id");
        context = this;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        fileBrowser = (Button) findViewById(R.id.file_browser);
        loading = (ProgressBar) findViewById(R.id.loading_file_uploader);

        fetchData(1, route, null);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

    }

    public void callback(final JSONObject response, int statusCode) {
        try {


            if(!response.getString("description").isEmpty()){

                TextView description = (TextView) findViewById(R.id.description);
                description.setText(response.getString("description"));
                description.setTypeface(typeface);

            }else{

                LinearLayout boxDescription = (LinearLayout) findViewById(R.id.directory_description_box);
                boxDescription.setVisibility(View.GONE);

            }

//            TextView title = (TextView) findViewById(R.id.title);
//            title.setText(response.getString("title"));
//            title.setTypeface(typeface);

            CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
            collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
            collapsingToolbarLayout.setTitle(response.getString("title"));
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);

            fileBrowser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, READ_REQUEST_CODE);
                }
            });

            TextViewIranSansBold address = (TextViewIranSansBold) findViewById(R.id.address);
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
            owner.setText(response.getJSONObject("owner").getString("fullName"));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                System.out.println("Uri: " + uri.toString());
                Uri selectedImage = resultData.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                System.out.println(picturePath);

                RequestParams params = new RequestParams();
                try {

                    params.put("image", new File(picturePath));
                    params.put("directory_id", extras.get("directory_id"));
                    upload("upload", params);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dumpImageMetaData(Uri uri) {

        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                System.out.println("Display Name: " + displayName);

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                System.out.println("Size: " + size);
            }
        } finally {
            cursor.close();
        }
    }

}