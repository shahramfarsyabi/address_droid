package ir.iraddress.www.profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
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
            user = sharedPrefered.findByIndex(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


