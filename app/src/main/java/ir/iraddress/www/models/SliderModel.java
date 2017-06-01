package ir.iraddress.www.models;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import ir.iraddress.www.helper.HttpRequest;
import ir.iraddress.www.interfaces.SliderViewInterface;
import ir.iraddress.www.mainMenu.MainMenuAdapter;

/**
 * Created by shahram on 6/1/17.
 */

public class SliderModel {

    public JSONArray sliders;

    public void fetch(RequestParams params, final SliderViewInterface viewRender){

        HttpRequest.get("sliders", params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                viewRender.execute(response);
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){

            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, String data, Throwable throwable){
                System.out.println(data);
            }

        });

    }
}
