package ir.iraddress.www.directories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/23/17.
 */

public class DirectorySimilarsAdapter extends RecyclerView.Adapter<DirectorySimilarHolder> {

    public Context context;
    public LayoutInflater inflater;
    List similars;

    public DirectorySimilarsAdapter(Context context, List similars){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.similars = similars;
    }
    @Override
    public DirectorySimilarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectorySimilarHolder(inflater.inflate(R.layout.viewholder_directory_similar, parent, false));
    }

    @Override
    public void onBindViewHolder(DirectorySimilarHolder holder, int position) {

        try {
            JSONObject item = (JSONObject) similars.get(position);

            TextView address = (TextView) holder.cardView.findViewById(R.id.similiar_address);
            address.setText(item.getString("address"));

            TextView title = (TextView) holder.cardView.findViewById(R.id.similiar_title);
            title.setText(item.getString("title"));

            RatingBar rate = (RatingBar) holder.cardView.findViewById(R.id.similiar_rate);
            rate.setRating(item.getInt("rate"));

            ImageView image = (ImageView) holder.cardView.findViewById(R.id.similiar_image);
            Picasso.with(context)
                    .load(item.getString("image"))
                    .fit()
                    .centerCrop()
                    .error(R.drawable.ic_noimg)
                    .into(image);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return similars.size();
    }
}
