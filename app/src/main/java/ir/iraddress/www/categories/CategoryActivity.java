package ir.iraddress.www.categories;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.MapActivity;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;

/**
 * Created by shahram on 2/19/17.
 */

public class CategoryActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){

        context = this;
        extras = getIntent().getExtras();
        route = "categories/"+extras.getInt("main_category_id");
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        TextViewIranSansBold toolbarTitle = (TextViewIranSansBold) findViewById(R.id.toolbar_sub_category_title);
        toolbarTitle.setText(extras.getString("main_category_title"));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_sub_categories);
        recyclerViewAdapter = new SubCategoriesAdapter(this, this, collection, extras.getInt("main_category_id"));
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        System.out.print(route);
        RequestParams params = new RequestParams();
        getRequest(route, params);

    }

    public void callback(JSONArray response, int statusCode, String method) {
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

    public void showMapActivity(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("route", route+"/directories");
        startActivity(intent);
    }
}
