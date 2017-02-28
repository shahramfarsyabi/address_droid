package ir.iraddress.www.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyCommentsActivity extends ProfileMainActivity {

    public void onCreate(Bundle savedInstanceState){

        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_comments);

        try {

            getRequest("users/"+user.getInt("id")+"/comments", params);

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view_comments);
            recyclerViewAdapter = new MyCommentsAdapter(this, collection);
            layoutManager = new LinearLayoutManager(this);

            recyclerView.setAdapter(recyclerViewAdapter);
            recyclerView.setLayoutManager(layoutManager);


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void onClickRemove(final View view){
        AlertDialog confirmation = new AlertDialog.Builder(this)
        .setTitle("Destroy")
        .setMessage("Are you sure you want remove this item ?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("btn remove comment "+view.getTag());
                collection.remove(view.getTag());
                Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();
                recyclerViewAdapter.notifyItemRemoved((Integer) view.getTag());
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    @Override
    public void callback(JSONArray response, int statusCode) {

        switch(statusCode){

            case 200:

                try {
                    for(int n = 0; n < response.length(); n++){
                        collection.add(response.get(n));
                    }

                    recyclerViewAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            default:

                break;
        }
        System.out.println(response);
    }
}
