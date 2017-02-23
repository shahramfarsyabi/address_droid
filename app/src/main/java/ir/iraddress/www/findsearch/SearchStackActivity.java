package ir.iraddress.www.findsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.helper.SharedPrefered;

/**
 * Created by shahram on 2/14/17.
 */

public class SearchStackActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_search);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search_latest);
        recyclerViewAdapter = new SearchAdapter(this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void filteredDirectories(View view){
        int id = view.getId();
        String url = "";
        switch(id){
            case R.id.featured:
                    System.out.println("featured");
                    url ="directories/featured";
                break;

            case R.id.newest:
                System.out.println("image_new");
                url ="directories/newest";
                break;

            case R.id.discounts:
                System.out.println("discounts");
                url ="directories/discount";
                break;

            case R.id.nearme:
                System.out.println("nearme");
                url ="directories/nearme";
                break;

        }

        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
