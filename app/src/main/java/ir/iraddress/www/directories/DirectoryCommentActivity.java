package ir.iraddress.www.directories;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.profile.ProfileMainActivity;


public class DirectoryCommentActivity extends ProfileMainActivity {

    int item_id;
    String type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        item_id = extras.getInt("item_id");
        type = extras.getString("type");

    }

    public void sendCommentToServer(View view) throws JSONException {

        if(checkAuthenticated() == Boolean.FALSE){
            return;
        }

        TextInputEditText comment = (TextInputEditText) findViewById(R.id.comment_content);

        if(comment.getText().length() <= 0){
            Toast.makeText(this, "لطفا نظر خود را بنویسید و سپس دکمه ارسال را فشار دهید", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestParams params = new RequestParams();
        params.put("item_id", item_id);
        params.put("type", type);
        params.put("text", comment.getText().toString());

        postRequest("users/"+user.getInt("id")+"/comments", params);

    }

    public void callback(JSONObject response, int statusCode, String method){

        switch (statusCode){
            case 200:
                finish();
                Toast.makeText(this, "نظر شما ثبت و پس از تایید منتشر خواهد شد", Toast.LENGTH_LONG).show();
                break;

            default:

                break;
        }
    }

}
