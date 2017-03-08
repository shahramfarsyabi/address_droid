package ir.iraddress.www.map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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


public class MapItemShowLinearAdapter extends RecyclerView.Adapter<MapItemShowLinearHolder>{
    LayoutInflater inflater;
    List collection;
    Context context;
    public JSONObject location;

    public MapItemShowLinearAdapter(Context context, Activity activity, List collection){
        inflater = LayoutInflater.from(context);
        this.collection = collection;
        this.context = context;
        MyLocationServiceManager myLocationServiceManager = new MyLocationServiceManager(context, activity);
        try {
            location = myLocationServiceManager.getLocation();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MapItemShowLinearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MapItemShowLinearHolder(inflater.inflate(R.layout.viewholder_map_item_linear, parent, false));
    }

    @Override
    public void onBindViewHolder(MapItemShowLinearHolder holder, int position) {

        try {
            final JSONObject object = (JSONObject) collection.get(position);

            holder.cardView.setTag(object);

            TextViewIranSansBold title = (TextViewIranSansBold) holder.cardView.findViewById(R.id.directory_title);
            title.setText(object.getString("title"));

            TextViewIranSans address = (TextViewIranSans) holder.cardView.findViewById(R.id.directory_address);
            address.setText(object.getString("address").trim());

            TextViewIranSans phone = (TextViewIranSans) holder.cardView.findViewById(R.id.directory_phone);
            phone.setText(object.getString("phone").trim());

            TextViewIranSans distance = (TextViewIranSans) holder.cardView.findViewById(R.id.directory_distance);

            Location locationA = new Location("A");
            locationA.setLatitude(object.getDouble("latitude"));
            locationA.setLongitude(object.getDouble("longitude"));


            Location locationB = new Location("B");
            locationB.setLatitude(Double.parseDouble(location.getString("lat")));
            locationB.setLongitude(Double.parseDouble(location.getString("lng")));

            float distanceInMeter = locationA.distanceTo(locationB);
            distanceInMeter = distanceInMeter / 1000;


            distance.setText("فاصله شما "+String.format("%.02f", distanceInMeter) + " کیلومتر");

            ImageView image = (ImageView) holder.cardView.findViewById(R.id.directory_image);
            if(object.has("image") && !object.getString("image").isEmpty()){
                Picasso.with(context).load(object.getString("image")).fit().centerCrop().into(image);
            }else{

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
