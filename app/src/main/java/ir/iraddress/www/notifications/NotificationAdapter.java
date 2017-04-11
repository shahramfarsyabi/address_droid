package ir.iraddress.www.notifications;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoryActivity;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.profile.ConnectionsActivity;
import ir.iraddress.www.profile.MyTripActivity;

/**
 * Created by shahram on 4/11/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {

    LayoutInflater inflater;
    Context _context;
    List _collection;
    JSONObject _user;

    public NotificationAdapter(Context context, List collection, JSONObject user){
        inflater = LayoutInflater.from(context);
        _context = context;
        _collection = collection;
        _user = user;
    }

    @Override
    public NotificationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationHolder(inflater.inflate(R.layout.viewholder_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificationHolder holder, int position) {

        try {
            final JSONObject notif = (JSONObject) _collection.get(position);

            TextViewIranSans title = (TextViewIranSans) holder.cardView.findViewById(R.id.notification_title);
            TextViewIranSans content = (TextViewIranSans) holder.cardView.findViewById(R.id.notification_content);

            title.setText(notif.getString("title"));
            content.setText(notif.getString("content"));

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // 'message', 'follow_request', 'notification', 'comment_directory', 'comment_trip'
                    Intent intent = null;
                    try {
                        switch(notif.getString("type")){
                            case "comment_directory":

                                intent = new Intent(_context, DirectoryActivity.class);
                                intent.putExtra("directory_id", notif.getInt("item_id"));
                                _context.startActivity(intent);

                                break;

                            case "comment_trip":

                                intent = new Intent(_context, MyTripActivity.class);
                                intent.putExtra("trip_id", notif.getInt("item_id"));
                                _context.startActivity(intent);

                                break;

                            case "follow_request":
                                intent = new Intent(_context, ConnectionsActivity.class);
                                intent.putExtra("url", "users/"+_user.getInt("id")+"/followers");
                                intent.putExtra("type", "followers");
                                _context.startActivity(intent);
                                break;

                            default:

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
        return _collection.size();
    }
}
