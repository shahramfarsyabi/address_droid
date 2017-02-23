package ir.iraddress.www.findsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoryActivity;
import ir.iraddress.www.directories.DirectoryHolder;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.helper.SharedPrefered;

/**
 * Created by shahram on 2/20/17.
 */

public class SearchAdapter extends RecyclerView.Adapter {

    LayoutInflater inflater;
    Context context;
    JSONArray collection;
    JSONObject location;
    String locationString;

    public SearchAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        locationString = sharedPreferences.getString("location", "");
        try {

            SharedPrefered sharedPrefered = new SharedPrefered(context, "last_viewed");
            sharedPrefered.latest(10);
            collection = sharedPrefered.reverse();

            if(!locationString.isEmpty()){
                location = new JSONObject(locationString);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case 0:
                return new SearchHeaderHolder(inflater.inflate(R.layout.viewholder_search_header, parent, false));

            default:
                return new DirectoryHolder(inflater.inflate(R.layout.viewholder_directory_list, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:
                SearchHeaderHolder searchHeaderHolder = (SearchHeaderHolder) holder;


                break;

            default :
                try {

                    DirectoryHolder directoryHolder = (DirectoryHolder) holder;

                    final JSONObject object = (JSONObject) collection.get((position-1));

                    TextViewIranSans title = (TextViewIranSans) directoryHolder.linearLayout.findViewById(R.id.directory_title);
                    title.setText(object.getString("title"));

                    ImageView image = (ImageView) directoryHolder.linearLayout.findViewById(R.id.directory_image);
                    if(object.has("image") && !object.getString("image").isEmpty()){
                        Picasso.with(context).load(object.getString("image")).fit().centerCrop().into(image);
                    }else{

                    }

                    TextViewIranSans address = (TextViewIranSans) directoryHolder.linearLayout.findViewById(R.id.directory_address);
                    address.setText(object.getString("address").trim());

                    TextViewIranSans phone = (TextViewIranSans) directoryHolder.linearLayout.findViewById(R.id.directory_phone);
                    phone.setText(object.getString("phone").trim());

                    TextViewIranSans distance = (TextViewIranSans) directoryHolder.linearLayout.findViewById(R.id.directory_distance);


                    if(!locationString.isEmpty()){
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
                    }


                    directoryHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(context, DirectoryActivity.class);
                                intent.putExtra("city_id", object.getInt("id"));
                                context.startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (collection.length()+1);
    }
}
