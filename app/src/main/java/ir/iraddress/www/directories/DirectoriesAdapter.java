package ir.iraddress.www.directories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;
import ir.iraddress.www.helper.MyLocationServiceManager;


public class DirectoriesAdapter extends RecyclerView.Adapter<DirectoryHolder> {

    public LayoutInflater inflater;
    public Context context;
    public List list;
    public Typeface typeface;
    public JSONObject location;
    public MyLocationServiceManager myLocationServiceManager;

    public DirectoriesAdapter(Context context, Activity activity, List list){
        inflater = LayoutInflater.from(context);
        this.context = context;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.list = list;
        myLocationServiceManager = new MyLocationServiceManager(context, activity);
        try {
            location = myLocationServiceManager.getLocation();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public DirectoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectoryHolder(inflater.inflate(R.layout.viewholder_directory_list, parent, false));
    }

    @Override
    public void onBindViewHolder(DirectoryHolder holder, int position) {


        try {
            final JSONObject object = (JSONObject) list.get(position);

            TextViewIranSansBold title = (TextViewIranSansBold) holder.cardView.findViewById(R.id.directory_title);
            title.setText(object.getString("title"));

            ImageView image = (ImageView) holder.cardView.findViewById(R.id.directory_image);
            if(!object.getString("image").isEmpty()){
                Picasso.with(context).load(object.getString("image")).fit().centerCrop().into(image);
            }

            TextViewIranSans address = (TextViewIranSans) holder.cardView.findViewById(R.id.directory_address);
            address.setText(object.getString("address").trim());

            TextViewIranSans phone = (TextViewIranSans) holder.cardView.findViewById(R.id.directory_phone);
            phone.setText(object.getString("phone").trim());

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, DirectoryActivity.class);
                        intent.putExtra("directory_id", object.getInt("id"));
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            TextViewIranSans distance = (TextViewIranSans) holder.cardView.findViewById(R.id.directory_distance);



            if(location.has("lat") && location.has("lng")){

                Location locationA = new Location("A");
                locationA.setLatitude(object.getDouble("latitude"));
                locationA.setLongitude(object.getDouble("longitude"));


                Location locationB = new Location("B");
                locationB.setLatitude(Double.parseDouble(location.getString("lat")));
                locationB.setLongitude(Double.parseDouble(location.getString("lng")));

                float distanceInMeter = locationA.distanceTo(locationB);
                distanceInMeter = distanceInMeter / 1000;

                distance.setText("فاصله شما "+String.format("%.02f", distanceInMeter) + " کیلومتر");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
