package ir.iraddress.www.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.helper.SharedPrefered;
import ir.iraddress.www.profile.ProfileActivity;

/**
 * Created by shahram on 2/14/17.
 */

public class SignInActivity extends MainController {

    public Button btnSignIn;
    public void onCreate(Bundle savedInstanceState){

        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        btnSignIn = (Button) findViewById(R.id.btn_signin);
    }

    public void singin(View view){

        btnSignIn.setVisibility(View.INVISIBLE);

        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.password);


        RequestParams params = new RequestParams();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        postRequest("signin", params);

    }

    @Override
    public void callback(JSONObject response, int statusCode) {

        switch(statusCode){

            case 200:

                try {
                    System.out.println(response);
                    SharedPrefered sharedPrefered = new SharedPrefered(context, "user");
                    sharedPrefered.empty();
                    sharedPrefered.store(response);

                    finish();

                    Intent intent = new Intent(context, ProfileActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case 422:
                btnSignIn.setVisibility(View.VISIBLE);
                break;

            default:
                btnSignIn.setVisibility(View.VISIBLE);
                break;
        }
        System.out.println(response);
    }

}
