package ir.iraddress.www.notifications;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import ir.iraddress.www.R;
import ir.iraddress.www.profile.ProfileMainActivity;

/**
 * Created by shahram on 4/11/17.
 */

public class NotificationsActivity extends ProfileMainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        try {

            route = "users/"+user.getInt("id")+"/notifications";

            fetchData(1, route, params);

            recyclerView = (RecyclerView) findViewById(R.id.notifications_recyclerview);
            recyclerViewAdapter = new NotificationAdapter(this, collection, user);
            layoutManager = new LinearLayoutManager(this);

            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(layoutManager);

            render();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
