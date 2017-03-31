package ir.iraddress.www.services;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import ir.iraddress.www.MainController;
import ir.iraddress.www.helper.HttpRequest;
import ir.iraddress.www.helper.SharedPrefered;

import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by shahram on 2/16/17.
 */

public class MyFirebaseInstanceService extends FirebaseInstanceIdService {

    public SharedPrefered sharedPrefered;
    public JSONObject user = null;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        try {
            sendRegistrationToServer(refreshedToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendRegistrationToServer(String refreshedToken) throws JSONException {

        sharedPrefered = new SharedPrefered(this, "user");
        Toast.makeText(MyFirebaseInstanceService.this, "FCM need to be Update", Toast.LENGTH_LONG).show();

        if(sharedPrefered.count() > 0){
            user = sharedPrefered.findByIndex(0);

            SyncHttpClient syncHttpClient = new SyncHttpClient();
            syncHttpClient.addHeader("Authorization", "Bearer "+user.getString("token"));

            RequestParams requestParams = new RequestParams();
            requestParams.put("fcm_token", refreshedToken);

            syncHttpClient.put("http://app.iraddress.ir/api/v1/users/"+user.getInt("id")+"/profile", requestParams, new JsonHttpResponseHandler(){
                public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                    Toast.makeText(MyFirebaseInstanceService.this, "FCM Updated", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
}
