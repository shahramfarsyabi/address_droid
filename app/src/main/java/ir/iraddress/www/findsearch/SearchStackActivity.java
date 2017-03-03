package ir.iraddress.www.findsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.util.Timer;
import java.util.TimerTask;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoriesAdapter;
import ir.iraddress.www.helper.SharedPrefered;

/**
 * Created by shahram on 2/14/17.
 */

public class SearchStackActivity extends MainController {

    Timer timer = new Timer();

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_search);


        final LinearLayout searchMainSection = (LinearLayout) findViewById(R.id.search_main_section);
        final LinearLayout searchMainResult = (LinearLayout) findViewById(R.id.search_main_result);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search_latest);
        recyclerViewAdapter = new SearchAdapter(this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        final TextInputEditText searchInput = (TextInputEditText) findViewById(R.id.search_input);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search_result);
        recyclerViewAdapter = new DirectoriesAdapter(context, collection);
        layoutManager = new LinearLayoutManager(context);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                timer.cancel();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timer.cancel();
                switch (s.length()){
                    case 0:
                        searchMainSection.setVisibility(View.VISIBLE);
                        searchMainResult.setVisibility(View.GONE);

                        break;

                    default:
                        searchMainSection.setVisibility(View.GONE);
                        searchMainResult.setVisibility(View.VISIBLE);
                        break;
                }

            }

            @Override
            public void afterTextChanged(final Editable s) {
                collection.clear();
                if(s.length() <= 0){
                    return;
                }
                timer = new Timer();
                params = new RequestParams();
                params.put("query", s.toString());
                route = "directories";
                System.out.println(s.toString());
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        searchInput.post(new Runnable() {
                            @Override
                            public void run() {

                                recyclerView.removeAllViewsInLayout();
                                fetchData(1, route, params);

                                recyclerView.setAdapter(recyclerViewAdapter);
                                recyclerView.setLayoutManager(layoutManager);

                                render();

                            }
                        });
                    }
                }, 3000);
            }
        });

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
