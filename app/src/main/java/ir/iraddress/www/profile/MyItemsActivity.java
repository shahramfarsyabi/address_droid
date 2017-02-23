package ir.iraddress.www.profile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoriesAdapter;

/**
 * Created by shahram on 2/16/17.
 */

public class MyItemsActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){

        route = "directories";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_items);

        fetchData(1, "", null);

        recyclerView = (RecyclerView) findViewById(R.id.profile_directories_recyclerview);
        recyclerViewAdapter = new DirectoriesAdapter(this, collection);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        render();

    }

    public void callback(JSONObject response, int statusCode){
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
