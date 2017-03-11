package ir.iraddress.www.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;


public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionHolder> {

    public LayoutInflater inflater;
    public Context context;
    public List collection;
    public String type;

    public ConnectionsAdapter(Context context, List collection, String type){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = collection;
        this.type = type;
    }

    @Override
    public ConnectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConnectionHolder(inflater.inflate(R.layout.viewholder_connection, parent, false));
    }

    @Override
    public void onBindViewHolder(ConnectionHolder holder, int position) {

        try {
            JSONObject person = (JSONObject) collection.get(position);
            final JSONObject personData;

            switch(type){
                case "followers":
                    personData = person.getJSONObject("followers");
                    break;

                default:
                    personData = person.getJSONObject("followed");
                    break;
            }

            ImageView avatar = (ImageView) holder.person.findViewById(R.id.person_image);
            TextView name = (TextView) holder.person.findViewById(R.id.person_name);


            Picasso.with(context).load(personData.getString("avatar")).fit().centerCrop().into(avatar);
            name.setText(personData.getString("fullName"));

            holder.person.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, PublicProfileActivity.class);
                        intent.putExtra("user_id", personData.getInt("id"));
                        context.startActivity(intent);
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
