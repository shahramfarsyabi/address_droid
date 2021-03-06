package ir.iraddress.www.profile;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.AppButton;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;


public class ProfileActivity extends ProfileMainActivity {

    private static final int CODE_FOR_LOGOUT = 0;
    private static final int CODE_FOR_UPDATE_PROFILE = 2;
    private TextView clientName;
    private CircleImageView avatar;


    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {

            avatar = (CircleImageView) findViewById(R.id.profile_image);
            Picasso.with(context).load(user.getString("avatar")).fit().centerCrop().into(avatar);

            typeface = Typeface.createFromAsset(getAssets(), "fonts/ttf/IRANSansWeb.ttf");

            clientName = (TextView) findViewById(R.id.client_name);
            clientName.setText(user.getString("fullName"));
            clientName.setTypeface(typeface);


            getRequest("users/"+user.getInt("id")+"/profile", params);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppButton logout = (AppButton) findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout(v);
            }
        });


        TextViewIranSansBold btnEditProfile = (TextViewIranSansBold) findViewById(R.id.editProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileEditActivity.class);
                startActivityForResult(intent, CODE_FOR_UPDATE_PROFILE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case CODE_FOR_UPDATE_PROFILE:
                if (resultCode == RESULT_OK) {

                    try {
                        if(sharedPrefered.count() > 0){

                            user = sharedPrefered.findByIndex(0);
                            clientName.setText(user.getString("fullName"));
                            Picasso.with(context).load(user.getString("avatar")).fit().centerCrop().into(avatar);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    appToast("اطلاعات کاربری شما با موفقیت ویرایش شد");
                }
                break;

        }

    }

    @Override
    public void callback(JSONObject response, int statusCode, String method) {

        switch(statusCode){

            case 200:

                try {

                    TextViewIranSans itemsCount = (TextViewIranSans) findViewById(R.id.items_count);
                    itemsCount.setText(response.getInt("directories") + " آیتم");

                    TextViewIranSans tripsCount = (TextViewIranSans) findViewById(R.id.trips_count);
                    tripsCount.setText(response.getInt("trips") + " سفر");

                    TextViewIranSans commentsCount = (TextViewIranSans) findViewById(R.id.comments_count);
                    commentsCount.setText(response.getInt("comments") + " نظر");

                    TextViewIranSans photosCount = (TextViewIranSans) findViewById(R.id.photos_count);
                    photosCount.setText(response.getInt("images") + " عکس");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            default:

                break;
        }
        System.out.println(response);
    }

    public void profileActions(View view){
        int id = view.getId();
        Intent intent = null;
        System.out.println(id);
        switch (id){
            case R.id.myphotos:
                intent = new Intent(this, MyPhotosActivity.class);
                break;

            case R.id.mycomments:
                intent = new Intent(this, MyCommentsActivity.class);

                break;

            case R.id.mytrips:
                intent = new Intent(this, MyTripsActivity.class);

                break;

            case R.id.myitems:
                intent = new Intent(this, MyItemsActivity.class);
                break;
        }

        startActivity(intent);
    }

    public void getConnections(View view) throws JSONException {

        int id = view.getId();

        Intent intent = new Intent(context, ConnectionsActivity.class);

        switch(id){

            case R.id.followed:
                intent.putExtra("url", "users/"+user.getInt("id")+"/followed");
                intent.putExtra("type", "followed");
                break;

            case R.id.followers:
                intent.putExtra("url", "users/"+user.getInt("id")+"/followers");
                intent.putExtra("type", "followers");
                break;
        }

        startActivity(intent);
    }



    public void signout(View view) {
        try {
            sharedPrefered.empty();
            Intent returnIntent = getIntent();
            returnIntent.putExtra("resultCode", CODE_FOR_LOGOUT);
            setResult(RESULT_OK,returnIntent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
