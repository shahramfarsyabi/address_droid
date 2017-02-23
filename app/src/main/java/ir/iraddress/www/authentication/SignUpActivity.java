package ir.iraddress.www.authentication;

import android.content.Intent;
import android.graphics.Typeface;
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


public class SignUpActivity extends MainController {

    public Button btnSignUp;

    public void onCreate(Bundle savedInstanceState){
        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignUp = (Button) findViewById(R.id.btn_signup);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/ttf/IRANSansWeb.ttf");
    }

    public void singup(View view){

        btnSignUp.setVisibility(View.INVISIBLE);

        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.password);
        TextView password_confirmation = (TextView) findViewById(R.id.password_confirmation);
        TextView lastName = (TextView) findViewById(R.id.lastName);
        TextView firstName = (TextView) findViewById(R.id.firstName);
        TextView mobile = (TextView) findViewById(R.id.mobile);

        RequestParams params = new RequestParams();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        params.put("password_confirmation", password_confirmation.getText().toString());
        params.put("lastName", lastName.getText().toString());
        params.put("firstName", firstName.getText().toString());
        params.put("mobile", mobile.getText().toString());

        postRequest("signup", params);

    }

    @Override
    public void callback(JSONObject response, int statusCode) {

        switch(statusCode){

            case 200:

                try {

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
                btnSignUp.setVisibility(View.VISIBLE);
                break;

            default:
                btnSignUp.setVisibility(View.VISIBLE);
                break;
        }
        System.out.println(response);
    }
}
