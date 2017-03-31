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
import ir.iraddress.www.extend.AppButton;
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

            ImageView avatar = (ImageView) holder.linearLayout.findViewById(R.id.client_avatar);
            TextViewIranSansBold fullanem = (TextViewIranSansBold) holder.linearLayout.findViewById(R.id.client_fullname);

            Picasso.with(context).load(client.getString("avatar")).fit().centerCrop().into(avatar);
            fullanem.setText(client.getString("fullName"));

            client.put("follow_type", "follow");

            holder.linearLayout.setTag(client);
            AppButton btnFollowUnFollow = (AppButton) holder.linearLayout.findViewById(R.id.btnSendRequestFollowUnFollow);
            btnFollowUnFollow.setTag(client);

            if(client.get("connected_with_me") != null){

                switch(client.getJSONObject("connected_with_me").getString("status")){
                    case "pending":
                        btnFollowUnFollow.setText(R.string.connection_pending);
                        break;

                    case "accepted":
                        btnFollowUnFollow.setText(R.string.connection_accepted);
                        break;

                    case "rejected":
                        btnFollowUnFollow.setText(R.string.connection_rejected);
                        break;

                    default:
                        btnFollowUnFollow.setText(R.string.follow);
                        break;
                }
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
