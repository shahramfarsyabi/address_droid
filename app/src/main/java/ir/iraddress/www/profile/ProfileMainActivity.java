package ir.iraddress.www.profile;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.AppButton;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.helper.HttpRequest;
import ir.iraddress.www.helper.SharedPrefered;

/**
 * Created by shahram on 2/28/17.
 */

public class ProfileMainActivity extends MainController {

    public SharedPrefered sharedPrefered;
    public JSONObject user = null;
    public RequestParams params = new RequestParams();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        try {
            sharedPrefered = new SharedPrefered(this, "user");
            if(sharedPrefered.count() > 0){
                user = sharedPrefered.findByIndex(0);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Boolean checkAuthenticated(){
        if(user == null){

            Dialog dialogAuth = new Dialog(this, R.style.MyDialogSize);
            dialogAuth.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogAuth.setContentView(R.layout.dialog_authenticate);

            dialogAuth.show();

            return false;
        }

        return true;
    }

    public void requestFollowOrUnfollow(View view) throws JSONException {

        final JSONObject client = (JSONObject) view.getTag();
        AppButton appButton = (AppButton) view;
        RequestParams params = new RequestParams();
        params.put("type", client.getString("follow_type"));

        switch(client.getString("follow_type")){
            case "follow":
                appButton.setText("در انتظار تایید");
                break;

            case "unfollow":
                appButton.setText("دنبال کردن");
                break;

            default:
                appButton.setText("در انتظار تایید");
                break;
        }

//        HttpRequest.client.addHeader("Authorization", "Bearer "+user.getString("token"));

        Toast.makeText(context, "/users/"+user.getInt("id")+"/checkforfollow/"+client.getInt("id"), Toast.LENGTH_LONG).show();

        HttpRequest.post("users/"+user.getInt("id")+"/checkforfollow/"+client.getInt("id"), params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String data, Throwable throwable) {

            }
        });

    }

    public void onClickAccept(View view) throws JSONException {
        JSONObject connection = (JSONObject) view.getTag();
        HttpRequest.put("users/"+user.getInt("id")+"/connection/"+connection.getInt("id")+"/accept", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            }
        });

    }

    public void onClickReject(View view) throws JSONException {
        JSONObject connection = (JSONObject) view.getTag();
        HttpRequest.put("users/"+user.getInt("id")+"/connection/"+connection.getInt("id")+"/reject", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

            }
        });
    }
}


