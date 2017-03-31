package ir.iraddress.www.helper;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shahram on 1/20/17.
 */

public class HttpRequest {

    public static void setHeader(Context context){

        try {
            SharedPrefered sharedPrefered = new SharedPrefered(context, "user");

            if(sharedPrefered.count() > 0){
                JSONObject user = sharedPrefered.findByIndex(0);
                System.out.println(user.getString("token"));
                client.addHeader("Authorization", "Bearer "+user.getString("token"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static final String BASE_URL = "http://api.iraddress.ir/api/v1/";

    public static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void put(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.put(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void delete(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        client.delete(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}