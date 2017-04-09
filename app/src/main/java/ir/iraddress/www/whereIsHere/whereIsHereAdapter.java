package ir.iraddress.www.whereIsHere;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoryActivity;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;
import ir.iraddress.www.profile.MyTripActivity;

/**
 * Created by shahram on 4/9/17.
 */

public class whereIsHereAdapter extends RecyclerView.Adapter<whereIsHereHolder> {

    public LayoutInflater inflater;
    public List collection;
    public Context context;

    public whereIsHereAdapter(Context context, List collection){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = collection;
    }

    @Override
    public whereIsHereHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new whereIsHereHolder(inflater.inflate(R.layout.viewholder_where_is_here, parent, false));
    }

    @Override
    public void onBindViewHolder(whereIsHereHolder holder, int position) {
        final JSONObject item = (JSONObject) collection.get(position);

        ImageView image = (ImageView) holder.cardView.findViewById(R.id.directory_image);
        TextViewIranSansBold title = (TextViewIranSansBold) holder.cardView.findViewById(R.id.directory_title);
        final TextViewIranSans comment = (TextViewIranSans) holder.cardView.findViewById(R.id.directory_comment);

        try {

            Picasso.with(context).load(item.getJSONObject("parent").getString("image")).fit().centerCrop().into(image);
            title.setText(item.getJSONObject("parent").getString("title"));
            comment.setText(Html.fromHtml(item.getString("text")));

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        switch (item.getString("type")){
                            case "directory":
                                try {
                                    Intent intent = new Intent(context, DirectoryActivity.class);
                                    intent.putExtra("directory_id", item.getInt("item_id"));
                                    context.startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case "trip":
                                try {
                                    Intent intent = new Intent(context, MyTripActivity.class);
                                    intent.putExtra("trip_id", item.getInt("item_id"));
                                    context.startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
