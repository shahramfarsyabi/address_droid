package ir.iraddress.www.profile;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.AppButton;

/**
 * Created by shahram on 2/16/17.
 */

public class MyPhotosAdapter extends RecyclerView.Adapter<MyPhotosHolder> {

    public LayoutInflater inflater;
    public Context context;
    public List collection;
    public int width;
    public Boolean owner;

    public MyPhotosAdapter(Context context, List collection, Boolean owner){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = collection;
        this.owner = owner;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width = display.getWidth();
    }

    @Override
    public MyPhotosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyPhotosHolder(inflater.inflate(R.layout.viewholder_profile_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(MyPhotosHolder holder, int position) {


        try {

            JSONObject photo = (JSONObject) collection.get(position);
            photo.put("position_id", position);
            ImageView image = (ImageView) holder.image.findViewById(R.id.my_photo);
            AppButton btnDelete = (AppButton) holder.image.findViewById(R.id.btn_delete);
            View boxAction = (View) holder.image.findViewById(R.id.background_box_action);
            image.setMaxHeight(width/2);
            btnDelete.setTag(photo);

            if(owner){
                btnDelete.setVisibility(View.VISIBLE);
                boxAction.setVisibility(View.VISIBLE);
            }else{
                btnDelete.setVisibility(View.GONE);
                boxAction.setVisibility(View.GONE);
            }

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
