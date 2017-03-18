package ir.iraddress.www.profile;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
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

    private JSONObject comment;

    public void onCreate(Bundle savedInstanceState){

        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_comments);

        if(extras != null && extras.containsKey("user_id")){
            route = "users/"+extras.getInt("user_id")+"/comments";
        }else{

            try {
                route = "users/"+user.getInt("id")+"/comments";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getRequest(route, params);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_comments);
        recyclerViewAdapter = new MyCommentsAdapter(this, collection);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);


    }

    public void onClickRemove(final View view){

        Dialog dialogRemove = new Dialog(this);
        dialogRemove.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogRemove.setContentView(R.layout.dialog_confirm_remove);
        dialogRemove.show();

//        AlertDialog confirmation = new AlertDialog.Builder(this)
//        .setTitle("Destroy")
//        .setMessage("Are you sure you want remove this item ?")
//        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                System.out.println("btn remove comment "+view.getTag());
//
//                try {
//                    comment = (JSONObject) view.getTag();
//                    deleteRequest("users/"+user.getInt("id")+"/comments/"+comment.getInt("comment_id"), params);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        })
//        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
//            }
//        }).show();
    }

    @Override
    public void destroy(JSONObject response, int statusCode) {
        switch(statusCode){
            case 200:

                try {

                    collection.remove(comment.getInt("position_id"));

                    recyclerViewAdapter.notifyItemRemoved(comment.getInt("position_id"));

                    Toast.makeText(getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
        }
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
