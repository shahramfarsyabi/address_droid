package ir.iraddress.www.profile;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.AppButton;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;


class MyTripsAdapter extends RecyclerView.Adapter<MyTripHolder> {

    LayoutInflater inflater;
    Context context;
    Activity activity;
    List collection;
    Boolean owner;

    public MyTripsAdapter(Context context, Activity activity, List collection, Boolean owner) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.activity = activity;
        this.collection = collection;
        this.owner = owner;
    }

    @Override
    public MyTripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyTripHolder(inflater.inflate(R.layout.viewholder_profile_trips, parent, false));
    }

    @Override
    public void onBindViewHolder(MyTripHolder holder, int position) {
        JSONObject trip = (JSONObject) collection.get(position);

        TextViewIranSansBold title = (TextViewIranSansBold) holder.cardView.findViewById(R.id.trip_title);
        TextViewIranSans date = (TextViewIranSans) holder.cardView.findViewById(R.id.trip_date);
        ImageView image = (ImageView) holder.cardView.findViewById(R.id.trip_image);
        AppButton edit = (AppButton) holder.cardView.findViewById(R.id.edit);
        AppButton view = (AppButton) holder.cardView.findViewById(R.id.view);
        LinearLayout boxAction = (LinearLayout) holder.cardView.findViewById(R.id.box_action);

        if(owner){
            boxAction.setVisibility(View.VISIBLE);
        }else{
            boxAction.setVisibility(View.GONE);
        }

        holder.cardView.setTag(trip);
        view.setTag(trip);

        try {

            title.setText(trip.getString("title"));
            date.setText(trip.getString("date"));
            edit.setTag(trip);

            if(trip.getString("image").trim().length() > 0){
                Picasso.with(context).load(trip.getString("image")).fit().centerCrop().into(image);
            }else{
                image.setImageResource(R.drawable.ic_noimg);
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
