package ir.iraddress.www.findsearch;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoriesAdapter;
import ir.iraddress.www.extend.TextViewIranSansBold;

/**
 * Created by shahram on 2/15/17.
 */

public class SearchResultActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){

        extras = getIntent().getExtras();
        route = extras.getString("url");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directories);

        fetchData(1, "", null);

        recyclerView = (RecyclerView) findViewById(R.id.directories_recyclerview);
        recyclerViewAdapter = new DirectoriesAdapter(this, this, collection);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        if(extras.containsKey("toolbar_title")){
            TextViewIranSansBold textViewIranSansBold = (TextViewIranSansBold) findViewById(R.id.toolbar_title_directories);
            textViewIranSansBold.setText(extras.getString("toolbar_title"));
        }

        render();

    }

    public void callback(JSONObject response, int statusCode){

        switch (statusCode){
            case 200:

                try {

                    jsonArray = response.getJSONArray("data");

                    for(int i = 0; i < jsonArray.length(); i++ ){
                        collection.add(jsonArray.get(i));
                    }

                    recyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            default:

                break;
        }

//        mSwipeRefreshLayout.setRefreshing(false);

    }
}
