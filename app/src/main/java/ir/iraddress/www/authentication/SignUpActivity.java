package ir.iraddress.www.authentication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.AppButton;
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
        TextView firstName = (TextView) findViewById(R.id.firstName);
        firstName.requestFocus();
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
        params.put("fcm_token", FirebaseInstanceId.getInstance().getToken());

        postRequest("signup", params);

    }

    public void singin(View view){
        finish();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void callback(JSONObject response, int statusCode) {

        switch(statusCode){

            case 200:

                try {

                    SharedPrefered sharedPrefered = new SharedPrefered(context, "user");
                    sharedPrefered.empty();
                    sharedPrefered.store(response);

                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("resultCode", CODE_FOR_LOGIN);
                    setResult(RESULT_OK,returnIntent);

                    Intent intent = new Intent(context, ProfileActivity.class);
                    startActivity(intent);

                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case 422:
                btnSignUp.setVisibility(View.VISIBLE);
                Iterator<String> iter = response.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    System.out.println(key);

                    int id = 0;
                    switch(key){
                        case "email":
                            id = R.id.email;
                            break;
                        case "password":
                            id = R.id.password;
                            break;
                        case "password_confirmation":
                            id = R.id.password_confirmation;
                            break;
                        case "firstName":
                            id = R.id.firstName;
                            break;
                        case "lastName":
                            id = R.id.lastName;
                            break;
                    }

                    TextInputEditText textInputEditText = (TextInputEditText) findViewById(id);
                    textInputEditText.setBackgroundColor(Color.parseColor("#ffebeb"));

                    try {
                        JSONArray errorValidation = response.getJSONArray(key);
                        Toast.makeText(context, errorValidation.get(0).toString(), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;

            default:
                btnSignUp.setVisibility(View.VISIBLE);
                break;
        }
        System.out.println(response);
    }
}
