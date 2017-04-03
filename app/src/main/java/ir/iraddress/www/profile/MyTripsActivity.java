package ir.iraddress.www.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoriesAdapter;

/**
 * Created by shahram on 2/16/17.
 */

public class MyTripsActivity extends ProfileMainActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_trips);

        if(extras != null && extras.containsKey("user_id")){
            route = "users/"+extras.getInt("user_id")+"/trips";
        }else{
            try {
                route = "users/"+user.getInt("id")+"/trips";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        fetchData(1, route, null);

        recyclerView = (RecyclerView) findViewById(R.id.profile_trips_recyclerview);
        recyclerViewAdapter = new MyTripsAdapter(this, this, collection);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        render();
    }

    public void callback(JSONObject response, int statusCode, String method){
        try {

            jsonArray = response.getJSONArray("data");

            for(int i = 0; i < jsonArray.length(); i++ ){
                collection.add(jsonArray.get(i));
            }

            recyclerViewAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void btnShowTrip(View view) throws JSONException {
        JSONObject trip = (JSONObject) view.getTag();

        Intent intent = new Intent(this, MyTripActivity.class);
        intent.putExtra("trip_id", trip.getInt("id"));
        startActivity(intent);

    }
}
