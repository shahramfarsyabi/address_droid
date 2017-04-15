package ir.iraddress.www;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.iraddress.www.authentication.SignInActivity;
import ir.iraddress.www.authentication.SignUpActivity;
import ir.iraddress.www.categories.AdapterFilterCategories;
import ir.iraddress.www.extend.AppButton;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.helper.ConnectionDetector;
import ir.iraddress.www.helper.HttpRequest;
import ir.iraddress.www.helper.MyLocationServiceManager;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
    public Button fileBrowser;
    public ProgressBar loading;
    public Dialog loadingView;
    public MyLocationServiceManager myLocationServiceManager;
    public static final int PERMISSIONS_REQUEST_CODE_FOR_FINE_LOCATION = 1001;
    public static final int PERMISSIONS_REQUEST_CODE_FOR_WRITE_EXTERNAL_STORAGE = 1002;
    public static final int PERMISSIONS_REQUEST_CODE_FOR_CAMERA = 1003;
    public static final int CODE_FOR_LOGOUT = 0;
    public static final int CODE_FOR_LOGIN = 1;
    public final int READ_REQUEST_CODE = 42;
    public Dialog advanceFilter;
    public BroadcastReceiver broadcastReceiver;
    public IntentFilter intentFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ttf/IRANSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        extras = getIntent().getExtras();
        context = this;
        HttpRequest.setHeader(context);
        myLocationServiceManager = new MyLocationServiceManager(this, this);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        loadingView = new Dialog(this);
        loadingView.setCancelable(false);
        loadingView.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingView.setContentView(R.layout.dialog_loading);

        broadCastReceiver();

    }

    protected void broadCastReceiver(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {


                final Dialog dialogFCM = new Dialog(context, R.style.MyDialogSize);
                dialogFCM.requestWindowFeature(Window.FEATURE_NO_TITLE);


                switch (intent.getExtras().getString("fcm_type")){
                    case "request_follow":
                        dialogFCM.setContentView(R.layout.dialog_fcm_request_follow);
                        AppButton acceptConnection = (AppButton) dialogFCM.findViewById(R.id.btn_accept_connection);
                        AppButton rejectConnection = (AppButton) dialogFCM.findViewById(R.id.btn_reject_connection);

                        final RequestParams followParams = new RequestParams();

                        acceptConnection.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println(intent.getExtras().getString("accept"));
                                HttpRequest.put(intent.getExtras().getString("accept"), followParams, new JsonHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                                        dialogFCM.cancel();
                                    }
                                });


                            }
                        });

                        rejectConnection.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println(intent.getExtras().getString("reject"));
                                HttpRequest.put(intent.getExtras().getString("reject"), followParams, new JsonHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                                        dialogFCM.cancel();
                                    }
                                });
                            }
                        });

                        break;

                    default :
                        dialogFCM.setContentView(R.layout.dialog_fcm);
                        break;
                }

                TextView fcmBody = (TextView) dialogFCM.findViewById(R.id.notification_body);
                TextView fcmTitle = (TextView) dialogFCM.findViewById(R.id.notification_title);
                fcmBody.setText(intent.getExtras().getString("notification_body"));
                fcmTitle.setText(intent.getExtras().getString("notification_title"));
                Button btnCloseDialogFcm = (Button) dialogFCM.findViewById(R.id.btnCloseDialogFcm);
                btnCloseDialogFcm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogFCM.cancel();
                    }
                });
                dialogFCM.show();




//                myNewActivity = new Intent(context, FirebaseMessageActivity.class);
//                startActivity(myNewActivity);
            }
        };

        intentFilter = new IntentFilter("OPEN_NEW_ACTIVITY");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!checkInternetConnection()){
            return;
        }
    }


    protected void checkPermissions(){

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE_FOR_FINE_LOCATION);
            return;
        }

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_FOR_FINE_LOCATION);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE_FOR_WRITE_EXTERNAL_STORAGE);
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CODE_FOR_CAMERA);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE_FOR_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    myLocationServiceManager.connect();
                    checkPermissions();
                }
                break;
            case PERMISSIONS_REQUEST_CODE_FOR_WRITE_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                }
                break;
            case PERMISSIONS_REQUEST_CODE_FOR_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissions();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void render(){

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swifeRefresh);

        if(mSwipeRefreshLayout != null){

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    collection.clear();
                    recyclerView.removeAllViewsInLayout();
                    fetchData(1, "", params);

                }
            });
        }


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

    public void callGoogleMapDirection(JSONObject start, JSONObject end) throws JSONException {

        String googleURLQuery = "https://maps.google.com/?saddr="+start.getDouble("lat")+","+start.getDouble("lng")+"&daddr="+end.getDouble("latitude")+","+end.getDouble("longitude")+"&mode=d";
        Uri gmmIntentUri = Uri.parse(googleURLQuery);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }

    }

    public void onBackPressed(View view) {
        finish();
//        super.onBackPressed();
    }

    public void loadNextDataFromApi(int offset) {
        fetchData(++offset, "", params);
    }

    public void callback(JSONObject response, int statusCode, String method){

        switch (statusCode){
            case 200:
                try {

                    jsonArray = response.getJSONArray("data");

                    for(int i = 0; i < jsonArray.length(); i++ ){
                        collection.add(jsonArray.get(i));
                    }

                    recyclerViewAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:

                break;
        }
//        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void callback(JSONArray response, int statusCode, String method){

    }

    public void uploaded(){

    }

    public void destroy(JSONObject response, int statusCode){

    }


    public void fetchData(int page, String url, RequestParams params){
        System.out.println(page);

        if(url.isEmpty()){
            url = route + "?page=" + page;
        }

        System.out.println(url);
        pageLoading(true);
        HttpRequest.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                pageLoading(false);
                callback(response, statusCode, "GET");
                if(mSwipeRefreshLayout != null){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                pageLoading(false);
                if(mSwipeRefreshLayout != null){
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, String data, Throwable throwable){
                pageLoading(false);
                if(mSwipeRefreshLayout != null){
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    public void getRequest(String url, RequestParams params){
        pageLoading(true);
        HttpRequest.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                pageLoading(false);
                callback(response, statusCode, "GET");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                pageLoading(false);
                callback(response, statusCode, "GET");
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                callback(response, statusCode, "GET");
            }
        });
    }

    public void postRequest(String url, RequestParams params){
        pageLoading(true);
        HttpRequest.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                pageLoading(false);
                callback(response, statusCode, "POST");
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                pageLoading(false);
                callback(response, statusCode, "POST");
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String data, Throwable throwable) {
                pageLoading(false);
                System.out.println(data);
            }
        });
    }



    public void deleteRequest(String url, RequestParams params){
        HttpRequest.delete(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                destroy(response, statusCode);
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                System.out.println(response);
                destroy(response, statusCode);
            }
        });
    }

    public void putRequest(String url, RequestParams params){
        pageLoading(true);

        HttpRequest.put(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                pageLoading(false);
                callback(response, statusCode, "PUT");
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                pageLoading(false);
                callback(response, statusCode, "PUT");
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String data, Throwable throwable) {
                pageLoading(false);
                System.out.println(data);
            }
        });
    }

    public void upload(String url, RequestParams params){

        final Button fileBrowser = (Button) findViewById(R.id.file_browser);
        if(fileBrowser != null){
            fileBrowser.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        }else{
            pageLoading(true);
        }


        HttpRequest.post(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {

                appToast("فایل با موفقیت ارسال شد.");
                uploaded();

                if(fileBrowser != null) {
                    fileBrowser.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }else{
                    pageLoading(false);
                }
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                appToast("در ارسال فایل خطایی رخ داده است.");

                if(fileBrowser != null) {
                    fileBrowser.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }else{
                    pageLoading(false);
                }
            }
        });
    }



    public boolean checkInternetConnection(){

        Boolean check = new ConnectionDetector(this).isConnectedToInternet();
        System.out.println(check);

        if(!check){

            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_no_connection);

            Button wifi = (Button) dialog.findViewById(R.id.wifi_intent);
            wifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                }
            });

            Button mobile = (Button) dialog.findViewById(R.id.mobile_intent);
            mobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.phone","com.android.phone.NetworkSetting");
                    startActivity(intent);
                }
            });
            dialog.show();

            return false;
        }

        return true;
    }

    public void pageLoading(Boolean activation){

        if(loadingView instanceof Dialog){

            if(activation){
                loadingView.show();
            }else{
                loadingView.cancel();
            }
        }
    }

    public void advanceFilter(View view){

        advanceFilter = new Dialog(this);
        advanceFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        advanceFilter.setCancelable(false);
        advanceFilter.setContentView(R.layout.filter_layout);

        final List categories = new ArrayList();

        RecyclerView recyclerViewFilterCategories = (RecyclerView) advanceFilter.findViewById(R.id.filter_categories_list);
        final AdapterFilterCategories adapterFilterCategories = new AdapterFilterCategories(this, categories);
        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(this);

        recyclerViewFilterCategories.setAdapter(adapterFilterCategories);
        recyclerViewFilterCategories.setLayoutManager(categoryLayoutManager);

        HttpRequest.get("categories", params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                for(int n = 0;n < response.length(); n++){
                    try {
                        categories.add(response.get(n));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(categories.size() > 0){
                    adapterFilterCategories.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){

            }
        });

        AppButton btnCloseFilter = (AppButton) advanceFilter.findViewById(R.id.btnHideFilterCategories);
        btnCloseFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceFilter.cancel();
            }
        });

        advanceFilter.show();

    }

    public void selectedCategory(View view) throws JSONException {

        AppCompatImageView appCompatImageView = (AppCompatImageView) view.findViewById(R.id.categoryItemCircleCheckbox);
        appCompatImageView.setImageResource(R.drawable.ic_dot_and_circle);

        JSONObject selectedCategory = (JSONObject) view.getTag();
        params.put("category_id", selectedCategory.getInt("id"));

        collection.clear();
        recyclerView.removeAllViews();
        recyclerViewAdapter.notifyDataSetChanged();

        fetchData(1, "", params);

        render();

        advanceFilter.cancel();

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
//            broadcastReceiver = null;
//            registerReceiver(broadcastReceiver, intentFilter);
        }
        super.onPause();
    }

    public void appToast(String content) {
        LayoutInflater inflater=getLayoutInflater();

        View customToastroot =inflater.inflate(R.layout.my_toast, null);
        TextViewIranSans text = (TextViewIranSans ) customToastroot.findViewById(R.id.toast_text);
        text.setText(content);

        Toast customtoast = new Toast(context);

        customtoast.setView(customToastroot);
//        customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
        customtoast.setDuration(Toast.LENGTH_LONG);
        customtoast.show();
    }

    public void signIn(View view){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void signUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}
