package ir.iraddress.www.profile;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.helper.SharedPrefered;

/**
 * Created by shahram on 2/14/17.
 */

public class ProfileActivity extends MainController {


    public JSONObject user = null;
    public void onCreate(Bundle savedInstanceState){

        context = this;

        try {
            SharedPrefered sharedPrefered = new SharedPrefered(this, "user");
            user = sharedPrefered.findByIndex(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        try {

            CircleImageView avatar = (CircleImageView) findViewById(R.id.profile_image);
            Picasso.with(context).load(user.getString("avatar")).fit().centerCrop().into(avatar);

            typeface = Typeface.createFromAsset(getAssets(), "fonts/ttf/IRANSansWeb.ttf");

            TextView clientName = (TextView) findViewById(R.id.client_name);
            clientName.setText(user.getString("fullName"));
            clientName.setTypeface(typeface);


            RequestParams params = new RequestParams();
            getRequest("users/"+user.getInt("id")+"/profile", params);

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void callback(JSONObject response, int statusCode) {

        switch(statusCode){

            case 200:

                try {

                    TextViewIranSans itemsCount = (TextViewIranSans) findViewById(R.id.items_count);
                    itemsCount.setText(response.getJSONArray("directories").length() + " آیتم");

                    TextViewIranSans tripsCount = (TextViewIranSans) findViewById(R.id.trips_count);
                    tripsCount.setText(response.getJSONArray("trips").length() + " سفر");

                    TextViewIranSans commentsCount = (TextViewIranSans) findViewById(R.id.comments_count);
                    commentsCount.setText(response.getJSONArray("comments").length() + " نظر");

                    TextViewIranSans photosCount = (TextViewIranSans) findViewById(R.id.photos_count);
                    photosCount.setText(response.getJSONArray("images").length() + " عکس");


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

}
