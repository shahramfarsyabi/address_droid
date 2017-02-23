package ir.iraddress.www.categories;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

/**
 * Created by shahram on 2/19/17.
 */

public class CategoriesActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){

        route = "categories";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_categories);
        recyclerViewAdapter = new CategoriesAdapter(this, collection);
        layoutManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        RequestParams params = new RequestParams();
        getRequest(route, params);
    }

    public void callback(JSONArray response, int statusCode) {
        for(int n = 0; n < response.length(); n++){
            try {
                collection.add(response.get(n));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        recyclerViewAdapter.notifyDataSetChanged();
        System.out.println(collection);
    }
}
