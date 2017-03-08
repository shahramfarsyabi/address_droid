package ir.iraddress.www.profile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;


public class ConnectionsActivity extends ProfileMainActivity {


    public void onCreate(Bundle savedInstanceState){

        extras = getIntent().getExtras();
        route = extras.getString("url");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        TextViewIranSansBold textViewIranSansBold = (TextViewIranSansBold) findViewById(R.id.toolbar_title_connections);
        switch(extras.getString("type")){
            case "followers":
                textViewIranSansBold.setText("دنبال شونده ها");
                break;

            default :
                textViewIranSansBold.setText("دنبال کننده ها");
                break;
        }

        fetchData(1, route, params);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_connections);
        recyclerViewAdapter = new ConnectionsAdapter(this, collection, extras.getString("type"));
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        render();

    }

    public void callback(JSONObject response, int statusCode) {
        switch(statusCode){
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
