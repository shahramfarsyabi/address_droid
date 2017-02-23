package ir.iraddress.www.profile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyCommentsActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){

        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_comments);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_comments);
        recyclerViewAdapter = new MyCommentsAdapter(this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }
}
