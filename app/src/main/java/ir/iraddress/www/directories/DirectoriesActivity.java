package ir.iraddress.www.directories;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;


public class DirectoriesActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){

        route = "directories";
        extras = getIntent().getExtras();

        if(extras.containsKey("route")){
            route = extras.getString("route");
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directories);
        String locationString =  sharedPreferences.getString("location", "");

        if(!locationString.isEmpty()){

            try {
                JSONObject location = new JSONObject(locationString);
                if(location.has("city")){
                    params.add("city_title", location.getString("city"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(extras.containsKey("toolbar_title")){

            TextViewIranSans toolbarTitle = (TextViewIranSans) findViewById(R.id.toolbar_title_directories);
            toolbarTitle.setText(extras.getString("toolbar_title"));
        }

        fetchData(1, "", params);

        recyclerView = (RecyclerView) findViewById(R.id.directories_recyclerview);
        recyclerViewAdapter = new DirectoriesAdapter(this, collection);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

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

        mSwipeRefreshLayout.setRefreshing(false);

    }

    public void advanceFilter(View view){
        System.out.println("START FILTER VIEW");
        View filter = getLayoutInflater().inflate(R.layout.filter_layout, null);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.directories_list_view);
        relativeLayout.addView(filter);


    }

}
