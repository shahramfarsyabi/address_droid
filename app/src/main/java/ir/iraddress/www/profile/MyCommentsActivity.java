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
import ir.iraddress.www.extend.AppButton;

/**
 * Created by shahram on 2/16/17.
 */

public class MyCommentsActivity extends ProfileMainActivity {

    private JSONObject comment;
    private Boolean owner;

    public void onCreate(Bundle savedInstanceState){

        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_comments);
        owner = Boolean.TRUE;

        if(extras != null && extras.containsKey("user_id")){
            owner = Boolean.FALSE;
            route = "public/users/"+extras.getInt("user_id")+"/comments";
        }else{

            try {
                route = "users/"+user.getInt("id")+"/comments";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getRequest(route, params);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_comments);
        recyclerViewAdapter = new MyCommentsAdapter(this, collection, owner);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);


    }

    public void onClickRemove(final View view){

        final Dialog dialogRemove = new Dialog(this);
        dialogRemove.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogRemove.setContentView(R.layout.dialog_confirm_remove);

        AppButton yes = (AppButton) dialogRemove.findViewById(R.id.yes);
        AppButton no = (AppButton) dialogRemove.findViewById(R.id.no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    comment = (JSONObject) view.getTag();
                    deleteRequest("users/"+user.getInt("id")+"/comments/"+comment.getInt("comment_id"), params);
                    dialogRemove.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRemove.cancel();
            }
        });

        dialogRemove.show();

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
    public void callback(JSONArray response, int statusCode, String method) {

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
