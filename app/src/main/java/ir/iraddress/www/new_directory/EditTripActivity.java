package ir.iraddress.www.new_directory;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoryActivity;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.profile.MyPhotosActivity;
import ir.iraddress.www.profile.ProfileMainActivity;

public class EditTripActivity extends ProfileMainActivity implements DatePickerDialog.OnDateSetListener {

    TextViewIranSans date;
    TextViewIranSans location;
    ImageView dateSelected;
    ImageView locationSelected;
    EditText title;
    EditText description;
    JSONObject trip;

    static final int PICKED_MARKER = 1;
    JSONObject selectedLocation = new JSONObject();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        date = (TextViewIranSans) findViewById(R.id.trip_date);
        dateSelected = (ImageView) findViewById(R.id.date_selected);
        locationSelected = (ImageView)  findViewById(R.id.location_selected);
        description = (EditText) findViewById(R.id.trip_description);
        title = (EditText) findViewById(R.id.trip_title);
        location = (TextViewIranSans) findViewById(R.id.trip_location);
        loading = (ProgressBar) findViewById(R.id.loading_file_uploader);

        try {
            getRequest("users/"+user.getInt("id")+"/trips/"+extras.getInt("trip_id"), params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void btnSubmitTrip(View view) throws JSONException {

        if(title.getText().equals(getString(R.string.trip_title))){
            Toast.makeText(this, getString(R.string.trip_title_required), Toast.LENGTH_LONG).show();
            return;
        }

        if(date.getText().equals(getString(R.string.trip_select_date))){
            Toast.makeText(this, getString(R.string.trip_date_required), Toast.LENGTH_LONG).show();
            return;
        }

        if(location.getText().equals(getString(R.string.trip_select_location))){
            Toast.makeText(this, getString(R.string.trip_location_required), Toast.LENGTH_LONG).show();
            return;
        }

        if(description.getText().equals(getString(R.string.trip_description))){
            Toast.makeText(this, getString(R.string.trip_descripton_required), Toast.LENGTH_LONG).show();
            return;
        }

        JSONObject formData = new JSONObject();
        formData.put("title", title.getText());
        formData.put("description", description.getText());
        formData.put("date", date.getText());

        RequestParams trip = new RequestParams();
        trip.put("title", title.getText());
        trip.put("content", description.getText());
        trip.put("date", date.getText());
        trip.put("location", selectedLocation.toString());

        putRequest("users/"+user.getInt("id")+"/trips/"+extras.getInt("trip_id"), trip);

    }

    public void callback(JSONObject response, int statusCode, String method){

        switch (statusCode){
            case 200:

                switch(method){
                    case "GET":

                        try {
                            trip = response;

                            title.setText(response.getString("title"));
                            date.setText(response.getString("date"));
                            description.setText(response.getString("content"));
                            selectedLocation = response.getJSONObject("location");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    case "PUT":
                        Toast.makeText(context, "تغییرات با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
                        break;
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
                            id = R.id.trip_title;
                            break;
                        case "content":
                            id = R.id.trip_description;
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


    public void showDatePicker(View view){

        PersianCalendar persianCalendar = new PersianCalendar();

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()
        );

        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String dateString = year+"/"+(monthOfYear+1)+"/"+dayOfMonth;
        date.setText(dateString);
        dateSelected.setVisibility(View.VISIBLE);
    }

    public void showMapDialog(View view) throws JSONException {
        Intent intent = new Intent(this, NewMapDirectory.class);

        if(trip != null){
         intent.putExtra("location", trip.getJSONObject("location").toString());
        }

        startActivityForResult(intent, PICKED_MARKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICKED_MARKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    selectedLocation = new JSONObject(data.getExtras().getString("point"));

                    if(selectedLocation.has("lat") && selectedLocation.has("lng")){
                        locationSelected.setVisibility(View.VISIBLE);
                        location.setText(getString(R.string.trip_location_selected));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                System.out.println("Uri: " + uri.toString());
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                System.out.println(picturePath);

                RequestParams params = new RequestParams();
                try {

                    params.put("image", new File(picturePath));
                    params.put("trip_id", extras.get("trip_id"));
                    try {
                        upload("users/"+user.getInt("id")+"/trips/"+extras.getInt("trip_id")+"/images", params);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void onClickSelectFromGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    public void onClickTripImages(View view){
        Intent intent = new Intent(this, MyPhotosActivity.class);
        intent.putExtra("type", "trip");
        intent.putExtra("trip_id", extras.getInt("trip_id"));
        startActivity(intent);
    }


}
