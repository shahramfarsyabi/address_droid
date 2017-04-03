package ir.iraddress.www.profile;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyPhotosActivity extends ProfileMainActivity {

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photos);

        if(extras != null && extras.containsKey("user_id")){
            route = "users/"+extras.getInt("user_id")+"/photos";
        }else{

            try {
                route = "users/"+user.getInt("id")+"/photos";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        fetchData(1, route, params);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_photos);
        recyclerViewAdapter = new MyPhotosAdapter(this, collection);
//        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        layoutManager = new GridLayoutManager(this,2);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        grid();

    }

    @Override
    public void callback(JSONObject response, int statusCode, String method) {

        switch (statusCode){
            case 200:
                try {

                    for(int n = 0; n < response.getJSONArray("data").length(); n++){

                        collection.add(response.getJSONArray("data").get(n));

                    }

                    recyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }


    }
}
