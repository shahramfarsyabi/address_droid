package ir.iraddress.www.profile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoriesAdapter;



public class MyItemsActivity extends ProfileActivity {

    public void onCreate(Bundle savedInstanceState){



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_items);

        if(extras != null && extras.containsKey("user_id")){
            route = "public/users/"+extras.get("user_id")+"/directories";

        }else{
            route = "directories";
        }

        fetchData(1, route, null);

        recyclerView = (RecyclerView) findViewById(R.id.profile_directories_recyclerview);
        recyclerViewAdapter = new DirectoriesAdapter(this, this, collection);
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

}
