package ir.iraddress.www.authentication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.helper.SharedPrefered;
import ir.iraddress.www.profile.ProfileActivity;

public class SignInActivity extends MainController {

    public Button btnSignIn;
    private AppCompatEditText email;
    private AppCompatEditText password;
    private static final int CODE_FOR_LOGIN = 1;


    public void onCreate(Bundle savedInstanceState){

        context = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        btnSignIn = (Button) findViewById(R.id.btn_signin);
    }

    public void singin(View view){

        btnSignIn.setVisibility(View.INVISIBLE);

        email = (AppCompatEditText) findViewById(R.id.email);
        password = (AppCompatEditText) findViewById(R.id.password);


        RequestParams params = new RequestParams();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());
        params.put("fcm_token", FirebaseInstanceId.getInstance().getToken());

        postRequest("signin", params);

    }

    public void singup(View view){
        finish();
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void callback(JSONObject response, int statusCode, String method) {

        System.out.println(statusCode);
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

                try {

                    if(response.has("email")){
                        JSONArray emailValidationError = response.getJSONArray("email");
//                        email.setError((CharSequence) emailValidationError.get(0));
                        email.setBackgroundColor(Color.parseColor("#ffebeb"));
                        Toast.makeText(context, emailValidationError.get(0).toString(), Toast.LENGTH_SHORT).show();
                    }

                    if(response.has("password")){
                        JSONArray passwordValidationError = response.getJSONArray("password");
//                        password.setError((CharSequence) passwordValidationError.get(0));
                        password.setBackgroundColor(Color.parseColor("#ffebeb"));
                        Toast.makeText(context, passwordValidationError.get(0).toString(), Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                btnSignIn.setVisibility(View.VISIBLE);
                break;

            default:
                Toast.makeText(getApplicationContext(), "UnAuthorized", Toast.LENGTH_SHORT).show();
                btnSignIn.setVisibility(View.VISIBLE);
                break;
        }
        System.out.println(response);
    }

}
