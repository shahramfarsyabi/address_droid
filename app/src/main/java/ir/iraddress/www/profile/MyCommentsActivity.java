package ir.iraddress.www.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

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

    public void onClickRemove(View view){
        AlertDialog confirmation = new AlertDialog.Builder(this)
        .setTitle("Destroy")
        .setMessage("Are you sure you want remove this item ?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }
}
