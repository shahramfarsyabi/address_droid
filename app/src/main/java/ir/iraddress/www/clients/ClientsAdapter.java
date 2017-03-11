package ir.iraddress.www.clients;

import android.app.Activity;
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
import ir.iraddress.www.extend.TextViewIranSansBold;

/**
 * Created by shahram on 3/10/17.
 */

public class ClientsAdapter extends RecyclerView.Adapter<ClientHolder> {

    Context context;
    Activity activity;
    LayoutInflater inflater;
    List collection;

    public ClientsAdapter(Context context, Activity activity, List collection){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.activity = activity;
        this.collection = collection;
    }

    @Override
    public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClientHolder(inflater.inflate(R.layout.viewholder_client, parent, false));
    }

    @Override
    public void onBindViewHolder(ClientHolder holder, int position) {


        try {

            JSONObject client = (JSONObject) collection.get(position);

            ImageView avatar = (ImageView) holder.cardView.findViewById(R.id.client_avatar);
            TextViewIranSansBold fullanem = (TextViewIranSansBold) holder.cardView.findViewById(R.id.client_fullname);

            Picasso.with(context).load(client.getString("avatar")).fit().centerCrop().into(avatar);
            fullanem.setText(client.getString("fullName"));

            holder.cardView.setTag(client);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
