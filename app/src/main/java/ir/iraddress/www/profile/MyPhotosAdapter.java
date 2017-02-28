package ir.iraddress.www.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyPhotosAdapter extends RecyclerView.Adapter<MyPhotosHolder> {

    public LayoutInflater inflater;
    public Context context;
    public List collection;

    public MyPhotosAdapter(Context context, List collection){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = collection;
    }

    @Override
    public MyPhotosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyPhotosHolder(inflater.inflate(R.layout.viewholder_profile_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(MyPhotosHolder holder, int position) {


        try {
            JSONObject photo = (JSONObject) collection.get(position);
            ImageView image = (ImageView) holder.image;
            Picasso.with(context).load(photo.getString("href")).fit().centerCrop().into(image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
