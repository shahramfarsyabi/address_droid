package ir.iraddress.www.directories;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.android.gms.maps.SupportMapFragment;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.AppButton;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;
import ir.iraddress.www.helper.ArrayUtil;
import ir.iraddress.www.helper.MyLocationServiceManager;
import ir.iraddress.www.helper.SharedPrefered;
import ir.iraddress.www.profile.PublicProfileActivity;

public class DirectoryActivity extends MainController {

    public SupportMapFragment mapFragment;
    public JSONObject directory;
    private MyLocationServiceManager myLocationServiceManager;
    private Uri fileUri;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        extras = getIntent().getExtras();
        route = "directories/" + extras.get("directory_id");
        System.out.println("solmaz route ");
        System.out.println(route);
        System.out.println("solmaz route ");

        context = this;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        loading = (ProgressBar) findViewById(R.id.loading_file_uploader);

        myLocationServiceManager = new MyLocationServiceManager(this, this);

        getRequest(route, params);

    }

    public void callback(final JSONObject response, int statusCode, String method) {
        try {

            directory = response;

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

            TextViewIranSansBold address = (TextViewIranSansBold) findViewById(R.id.address);
            address.setText(response.getString("address").trim());

            TextView createdAt = (TextView) findViewById(R.id.created_at);
            createdAt.setText(response.getString("created_at"));

            TextView directoryRate = (TextView) findViewById(R.id.directory_rate);
            directoryRate.setText("امتیاز "+response.getString("rate")+" از ۵");

            TextView category = (TextView) findViewById(R.id.category_type);

            if(response.getJSONArray("categories") instanceof JSONArray){
                JSONObject firstCategory = (JSONObject) response.getJSONArray("categories").get(0);
                category.setText(firstCategory.getString("title"));
            }

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




            AppButton btnPhoneDialer = (AppButton) findViewById(R.id.directory_phone_dialer);

            if (!response.getString("phone").isEmpty() && response.getString("phone") != "null") {

                btnPhoneDialer.setText(response.getString("phone"));
                btnPhoneDialer.setOnClickListener(new View.OnClickListener() {
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

            TextViewIranSans owner = (TextViewIranSans) findViewById(R.id.sent_by_owner);
            owner.setText(response.getJSONObject("owner").getString("fullName"));

            SharedPrefered sharedPrefered = new SharedPrefered(context, "last_viewed");
            sharedPrefered.store(response);


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


            ArrayList<Object> facilities = ArrayUtil.convert(response.getJSONArray("facilities"));

            if(facilities.size() > 0){

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view_facilities);
                recyclerViewAdapter = new DirectoryFacilitiesAdapter(this, facilities);
                layoutManager = new GridLayoutManager(this, 2);

                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }else{
                LinearLayout facilitiesBox = (LinearLayout) findViewById(R.id.directory_facilities_box);
                facilitiesBox.setVisibility(View.GONE);
            }


            Button btnAllComments = (Button) findViewById(R.id.directory_all_comments);
            Button btnFirstComment = (Button) findViewById(R.id.directory_first_comment);

            if (response.getJSONArray("comments").length() > 0) {

//            Comments Recyclerview
                recyclerView = (RecyclerView) findViewById(R.id.directory_comments);
                recyclerView.setVisibility(View.VISIBLE);
                layoutManager = new LinearLayoutManager(this);

                ArrayList<Object> comments = ArrayUtil.convert(response.getJSONArray("comments"));

                recyclerViewAdapter = new DirectoryCommentsAdapter(this, comments);
                recyclerView.setAdapter(recyclerViewAdapter);
                recyclerView.setLayoutManager(layoutManager);

                btnAllComments.setVisibility(View.VISIBLE);
                btnFirstComment.setVisibility(View.GONE);

            } else {

                recyclerView = (RecyclerView) findViewById(R.id.directory_comments);
                recyclerView.setVisibility(View.GONE);
                btnAllComments.setVisibility(View.GONE);
                btnFirstComment.setVisibility(View.VISIBLE);
            }

//            Similars Recylerview
            recyclerView = (RecyclerView) findViewById(R.id.directory_similar);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

            ArrayList<Object> similars = ArrayUtil.convert(response.getJSONArray("related"));

            recyclerViewAdapter = new DirectorySimilarsAdapter(this, similars);
            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(layoutManager);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        if(resultCode == RESULT_OK){

            final Dialog dialogApproveUploadImage = new Dialog(this);
            dialogApproveUploadImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogApproveUploadImage.setContentView(R.layout.dialog_upload_file_confirmation);
            AppButton btnAccept = (AppButton) dialogApproveUploadImage.findViewById(R.id.approve);
            AppButton btnDeny = (AppButton) dialogApproveUploadImage.findViewById(R.id.deny);

            btnDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogApproveUploadImage.cancel();
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogApproveUploadImage.cancel();

                    if (requestCode == READ_REQUEST_CODE ) {
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

                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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


                    if (requestCode == 100) {

                        Uri selectedImage = resultData.getData();
                        Bitmap photo = (Bitmap) resultData.getExtras().get("data");

                        // Cursor to get image uri to display

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        RequestParams params = new RequestParams();
                        try {

                            params.put("image", new File(picturePath));
                            params.put("directory_id", extras.get("directory_id"));
                            upload("upload", params);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        //            photo = (Bitmap) resultData.getExtras().get("data");
                        //            Toast.makeText(context, picturePath, Toast.LENGTH_LONG).show();
                        //            ImageView imageView = (ImageView) findViewById(R.id.Imageprev);
                        //            imageView.setImageBitmap(photo);
                    }

                }
            });

            dialogApproveUploadImage.show();
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

    public void dialogFileBrowser(View view){
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_file);

        Button fromGallery = (Button) dialog.findViewById(R.id.select_from_gallery);
        fromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                gallery(v);
            }
        });

        Button fromCamera = (Button) dialog.findViewById(R.id.select_from_camera);
        fromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();

                if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    // Open default camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    // start the image capture Intent
                    startActivityForResult(intent, 100);

                } else {
                    Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
                }

            }
        });


        dialog.show();
    }

    public void camera(View view){

    }

    public void gallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void btnRating(View view){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rating);
        dialog.show();

        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        final RatingBar rate = (RatingBar) dialog.findViewById(R.id.rating);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Toast.makeText(context, "your rate "+rate.getRating() , Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void comments(View view){
        Intent intent = new Intent(this, DirectoryCommentsActivity.class);
        intent.putExtra("route", route+"/comments");
        intent.putExtra("type", "directory");
        intent.putExtra("item_id", extras.getInt("directory_id"));
        startActivity(intent);
    }

    public void btnGoogleDirection(View view) throws JSONException {
        callGoogleMapDirection(myLocationServiceManager.getLocation() , directory);
    }

    public void btnSendEmail(View view) throws JSONException {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",directory.getString("email"), null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    public void btnMapLocation(View view) throws JSONException {
        System.out.println("MAP LOCATION ARTA");
        Intent intent = new Intent(this, DirectoryMapActivity.class);
        intent.putExtra("lat", directory.getDouble("latitude"));
        intent.putExtra("lng", directory.getDouble("longitude"));
        intent.putExtra("title", directory.getString("title"));
        startActivity(intent);
    }

    public void btnWriteReview(View view) throws JSONException {
        Intent intent = new Intent(this, DirectoryCommentActivity.class);
        intent.putExtra("type", "directory");
        intent.putExtra("item_id", extras.getInt("directory_id"));
        startActivity(intent);
    }

    public void btnWebSiteBrowser(View view) throws JSONException {
        String url = directory.getString("webSite");
        if (!url.startsWith("https://") && !url.startsWith("http://")){
            url = "http://" + url;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void btnShowProfile(View view) throws JSONException {
        Intent intent = new Intent(this, PublicProfileActivity.class);
        intent.putExtra("user_id", directory.getJSONObject("owner").getInt("id"));
        startActivity(intent);
    }

}