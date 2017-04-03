package ir.iraddress.www.profile;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.R;

public class ProfileEditActivity extends ProfileMainActivity {

    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText mobile;
    TextInputEditText email;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        firstName = (TextInputEditText) findViewById(R.id.firstName);
        lastName = (TextInputEditText) findViewById(R.id.lastName);
        mobile = (TextInputEditText) findViewById(R.id.mobile);
        email = (TextInputEditText) findViewById(R.id.email);

        try {
            firstName.setText(user.getString("firstName"));
            lastName.setText(user.getString("lastName"));
            mobile.setText(user.getString("mobile"));
            email.setText(user.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void saveProfile(View view) throws JSONException {

        RequestParams profileEdit = new RequestParams();

        profileEdit.put("firstName", firstName.getText().toString().trim());
        profileEdit.put("lastName", firstName.getText().toString().trim());
        profileEdit.put("mobile", firstName.getText().toString().trim());
        profileEdit.put("token", user.getString("token"));

        postRequest("users/"+user.getInt("id")+"/profile", params);

    }

    public void callback(JSONObject response, int statusCode, String method){

    }
}
