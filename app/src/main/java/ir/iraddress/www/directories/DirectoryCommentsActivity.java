package ir.iraddress.www.directories;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

public class DirectoryCommentsActivity extends MainController {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        extras = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_comments);

        if(extras.containsKey("route")){
            route = extras.getString("route");
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_comments);
        recyclerViewAdapter = new DirectoryCommentsAdapter(this, collection);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        fetchData(1, route, params);
        render();

    }

    public void sendComment(View view){
        Intent intent = new Intent(this, DirectoryCommentActivity.class);
        startActivity(intent);
    }
}
