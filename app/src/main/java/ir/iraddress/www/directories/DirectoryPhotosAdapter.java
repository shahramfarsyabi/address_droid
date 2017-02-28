package ir.iraddress.www.directories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/28/17.
 */

public class DirectoryPhotosAdapter extends RecyclerView.Adapter<DirectoryPhotoHolder> {

    LayoutInflater inflater;
    Context context;
    JSONArray collection;

    public DirectoryPhotosAdapter(Context context, JSONArray collection){
        inflater = LayoutInflater.from(context);
        this.collection = collection;
        this.context = context;
    }

    @Override
    public DirectoryPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectoryPhotoHolder(inflater.inflate(R.layout.viewholder_directory_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(DirectoryPhotoHolder holder, int position) {
        try {

            JSONObject photo = (JSONObject) collection.get(position);
            ImageView image = (ImageView) holder.photo.findViewById(R.id.viewholder_directory_image);

            Picasso.with(context).load(photo.getString("href")).fit().centerCrop().into(image);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return collection.length();
    }
}
