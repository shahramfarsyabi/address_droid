package ir.iraddress.www.profile;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyPhotosActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_photos);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_photos);
        recyclerViewAdapter = new MyPhotosAdapter(this);
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }

}
