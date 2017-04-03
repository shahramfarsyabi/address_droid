package ir.iraddress.www.new_directory;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import ir.iraddress.www.ExpandableListAdapter;
import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoryActivity;
import ir.iraddress.www.extend.AppButton;
import ir.iraddress.www.helper.HttpRequest;


public class NewDirectoryActivity extends MainController {

    JSONArray selectedCategories = new JSONArray();
    JSONArray selectedFacilities = new JSONArray();
    JSONObject selectedLocation = new JSONObject();
    JSONObject formData = new JSONObject();
    RequestParams newDirectory = new RequestParams();

    static final int PICKED_MARKER = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_directory);
        context = this;
    }

    public void showExpandableCategoriesList(View view){

        selectedCategories = new JSONArray();
        final Dialog expandableDialog = new Dialog(this);
        expandableDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        expandableDialog.setContentView(R.layout.dialog_expandable_categories);

        final AppButton btnSubmitCategories = (AppButton) expandableDialog.findViewById(R.id.btnSubmitCategories);
        AppButton btnCancel = (AppButton) expandableDialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandableDialog.dismiss();
            }
        });

        RequestParams requestParams = new RequestParams();
        requestParams.put("withSubCategories", Boolean.TRUE);

        HttpRequest.get("categories", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {

                ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(context, response);
                ExpandableListView expandableListView = (ExpandableListView) expandableDialog.findViewById(R.id.lvExp);
                expandableListView.setAdapter(expandableListAdapter);

                btnSubmitCategories.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            selectedCategories = new JSONArray();
                            findChecked((ViewGroup) expandableDialog.getWindow().getDecorView().getRootView(), selectedCategories);
                            expandableDialog.dismiss();
//                            Toast.makeText(context, selectedCategories.toString(), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });



                expandableDialog.show();

            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){

            }
        });

    }

    public void findChecked(ViewGroup parent, JSONArray list) throws JSONException {

        for(int n = 0; n < parent.getChildCount() ; n++){

            if(parent.getChildAt(n) instanceof CheckBox){
                CheckBox checkBox = (CheckBox) parent.getChildAt(n);

                JSONObject item = (JSONObject) checkBox.getTag();

                if(checkBox.isChecked()){
                    list.put(item.getInt("id"));
                }
            }

            if(parent.getChildAt(n) instanceof ViewGroup){
                findChecked((ViewGroup) parent.getChildAt(n), list);
            }
        }
    }

    public void findFormText(ViewGroup parent, JSONObject form) throws JSONException {

        for(int n = 0; n < parent.getChildCount() ; n++){

            if(parent.getChildAt(n) instanceof TextInputEditText){

                TextInputEditText editText = (TextInputEditText) parent.getChildAt(n);

                String key = editText.getTag().toString();
                String formDataEl = editText.getText().toString();

                if(!formDataEl.isEmpty()){
                    newDirectory.put(key, formDataEl);
                    form.put(key, formDataEl);
                }
            }

            if(parent.getChildAt(n) instanceof ViewGroup){
                findFormText((ViewGroup) parent.getChildAt(n), form);
            }
        }
    }

    public void showMapDialog(View view){
        Intent intent = new Intent(this, NewMapDirectory.class);
        startActivityForResult(intent, PICKED_MARKER);
    }

    public void showFacilitiesList(View view){

        selectedFacilities = new JSONArray();

        final Dialog facilitiesDialog = new Dialog(this, R.style.MyDialogSize);
        facilitiesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        facilitiesDialog.setContentView(R.layout.dialog_facilities);

        final RecyclerView recyclerViewFacilities = (RecyclerView) facilitiesDialog.findViewById(R.id.recycler_view_facilities);

        RequestParams requestParams = new RequestParams();
        requestParams.put("withSubCategories", Boolean.TRUE);

        final AppButton btnSubmitFacilities = (AppButton) facilitiesDialog.findViewById(R.id.btnSubmitFacilities);
        AppButton btnCancel = (AppButton) facilitiesDialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facilitiesDialog.dismiss();
            }
        });

        HttpRequest.get("facilities", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {

                FacilitiesAdapter facilitiesAdapter = new FacilitiesAdapter(context, response);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

                recyclerViewFacilities.setAdapter(facilitiesAdapter);
                recyclerViewFacilities.setLayoutManager(linearLayoutManager);

                btnSubmitFacilities.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            selectedFacilities = new JSONArray();
                            findChecked((ViewGroup) facilitiesDialog.getWindow().getDecorView().getRootView(), selectedFacilities);
                            facilitiesDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                facilitiesDialog.show();

            }

            @Override
            public void onFailure(int statusCode , cz.msebera.android.httpclient.Header[] headers, Throwable throwable , JSONObject response){
                facilitiesDialog.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICKED_MARKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    selectedLocation = new JSONObject(data.getExtras().getString("point"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void submitDirectory(View view) throws JSONException {
        newDirectory = new RequestParams();
        findFormText((ViewGroup) getWindow().getDecorView().getRootView(), formData);

        if(selectedCategories.length() <= 0) {
            Toast.makeText(context, "لطفا دسته های مرتبط را انتخاب نمایید .", Toast.LENGTH_SHORT).show();
            return;
        }

        if(selectedLocation.length() <= 0) {
            Toast.makeText(context, "لطفا موقعیت مکانی را مشخص کنید .", Toast.LENGTH_SHORT).show();
            return;
        }

        newDirectory.put("categories", selectedCategories);
        newDirectory.put("facilities", selectedFacilities);
        newDirectory.put("location", selectedLocation);

        postRequest("directories", newDirectory);
    }

    public void callback(JSONObject response, int statusCode){

        switch (statusCode){
            case 200:
//                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                try {

                    finish();
                    Intent intent = new Intent(this, DirectoryActivity.class);
                    intent.putExtra("directory_id", response.getInt("id"));
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case 422:
                Iterator<String> iter = response.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    System.out.println(key);

                    int id = 0;
                    switch(key){
                        case "title":
                            id = R.id.directory_title;
                            break;
                        case "address":
                            id = R.id.directory_address;
                            break;
                        case "phone":
                            id = R.id.directory_phone;
                            break;
                        case "email":
                            id = R.id.directory_email;
                            break;
                        case "description":
                            id = R.id.directory_description;
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

                break;
        }

    }

}
