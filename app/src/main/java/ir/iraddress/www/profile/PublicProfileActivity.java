package ir.iraddress.www.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;

/**
 * Created by shahram on 3/8/17.
 */

public class PublicProfileActivity extends ProfileMainActivity{

    protected JSONObject client;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_public);
        getRequest("users/"+extras.getInt("user_id")+"/profile", params);

    }

    @Override
    public void callback(JSONObject response, int statusCode, String method) {

        switch(statusCode){

            case 200:

                try {
                    client = response;

                    CircleImageView avatar = (CircleImageView) findViewById(R.id.profile_image);
                    Picasso.with(context).load(response.getString("avatar")).fit().centerCrop().into(avatar);

                    TextView clientName = (TextView) findViewById(R.id.client_name);
                    clientName.setText(response.getString("fullName"));
                    clientName.setTypeface(typeface);

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

    public void btnSendRequestForFollow(View view){
        Toast.makeText(this, "درخواست شما برای دنبال کردن کاربر ارسال شد.", Toast.LENGTH_LONG).show();
    }

    public void profileActions(View view) throws JSONException {
        int id = view.getId();
        Intent intent = null;
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
        intent.putExtra("public_profile", true);
        intent.putExtra("user_id", client.getInt("id"));
        startActivity(intent);
    }

}
