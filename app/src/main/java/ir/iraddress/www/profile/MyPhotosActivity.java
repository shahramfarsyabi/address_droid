package ir.iraddress.www.profile;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyPhotosActivity extends ProfileMainActivity {

    private JSONObject photo;
    private Boolean owner;
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photos);
        owner = Boolean.TRUE;

        if(extras != null && extras.containsKey("user_id")){
            owner = Boolean.FALSE;
            route = "users/"+extras.getInt("user_id")+"/photos";
        }else{

            if(extras != null && extras.containsKey("type")){

                switch(extras.getString("type")){
                    case "trip":
                        try {
                            route = "users/"+user.getInt("id")+"/trips/"+extras.getInt("trip_id")+"/images";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:

                        break;
                }
            }else{

                try {
                    route = "users/"+user.getInt("id")+"/photos";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        fetchData(1, route, params);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_photos);
        recyclerViewAdapter = new MyPhotosAdapter(this, collection, owner);
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

    public void onClickRemovePhoto(View view) throws JSONException {
        photo = (JSONObject) view.getTag();
        deleteRequest("users/"+user.getInt("id")+"/photos/"+photo.getInt("id"), params);
    }

    public void destroy(JSONObject response, int statusCode){
        switch (statusCode){
            case 200:
                try {

                    collection.remove(photo.getInt("position_id"));
                    recyclerViewAdapter.notifyItemRemoved(photo.getInt("position_id"));
                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
