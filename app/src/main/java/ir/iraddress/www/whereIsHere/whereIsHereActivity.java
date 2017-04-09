package ir.iraddress.www.whereIsHere;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

/**
 * Created by shahram on 4/9/17.
 */

public class whereIsHereActivity extends MainController {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_where_is_here);

        route = "where_is_here";

        fetchData(1, "", params);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_where_is_here_list);
        recyclerViewAdapter = new whereIsHereAdapter(this, collection);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        render();

    }
}
