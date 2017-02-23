package ir.iraddress.www;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.iraddress.www.helper.HttpRequest;

public abstract class MainController extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.Adapter recyclerViewAdapter;
    public RecyclerView.LayoutManager layoutManager;
    public JSONObject jsonObject;
    public JSONArray jsonArray = new JSONArray();
    public List collection = new ArrayList();
    public String route;
    public Bundle extras;
    public Context context;
    public Typeface typeface;
    public RequestParams params = new RequestParams();
    public SwipeRefreshLayout mSwipeRefreshLayout;

//    private boolean checkPermission() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this, Manifest.permission.DISABLE_KEYGUARD)
//                    != PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }

    public void render(){

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swifeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                collection.clear();
                recyclerView.removeAllViewsInLayout();
                fetchData(1, "", params);

            }
        });

        EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);

                final int curSize = recyclerViewAdapter.getItemCount();

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewAdapter.notifyItemRangeInserted(curSize, collection.size() - 1);
                    }
                });
            }
        };

        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

    }

    public void grid(){

        EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((GridLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);

                final int curSize = recyclerViewAdapter.getItemCount();

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewAdapter.notifyItemRangeInserted(curSize, collection.size() - 1);
                    }
                });
            }
        };

        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

    }


    public void onBackPressed(View view) {
        super.onBackPressed();
    }

    public void loadNextDataFromApi(int offset) {
        fetchData(++offset, "", params);
    }

    public void callback(JSONObject response, int statusCode){

    }

    public void callback(JSONArray response, int statusCode){

    }


    public void fetchData(int page, String url, RequestParams params){
        System.out.println(page);

        if(url.isEmpty()){
            url = route + "?page=" + page;
        }

        System.out.println(url);

        HttpRequest.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                callback(response, statusCode);
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){

            }
        });
    }

    public void getRequest(String url, RequestParams params){
        HttpRequest.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                callback(response, statusCode);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                callback(response, statusCode);
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                callback(response, statusCode);
            }
        });
    }

    public void postRequest(String url, RequestParams params){
        HttpRequest.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                callback(response, statusCode);
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                callback(response, statusCode);
            }
        });
    }

}
