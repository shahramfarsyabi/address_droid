package ir.iraddress.www.new_directory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.loopj.android.http.RequestParams;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.profile.ProfileMainActivity;


public class NewTripActivity extends ProfileMainActivity implements DatePickerDialog.OnDateSetListener {

    TextViewIranSans date;
    TextViewIranSans location;
    ImageView dateSelected;
    ImageView locationSelected;
    EditText title;
    EditText description;

    static final int PICKED_MARKER = 1;
    JSONObject selectedLocation = new JSONObject();


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);

        date = (TextViewIranSans) findViewById(R.id.trip_date);
        dateSelected = (ImageView) findViewById(R.id.date_selected);
        locationSelected = (ImageView)  findViewById(R.id.location_selected);
        description = (EditText) findViewById(R.id.trip_description);
        title = (EditText) findViewById(R.id.trip_title);
        location = (TextViewIranSans) findViewById(R.id.trip_location);

    }

    public void btnSubmitTrip(View view) throws JSONException {

        if(title.getText().equals(getString(R.string.trip_title))){
            appToast(getString(R.string.trip_title_required));
            return;
        }

        if(date.getText().equals(getString(R.string.trip_select_date))){
            appToast(getString(R.string.trip_date_required));
            return;
        }

        if(location.getText().equals(getString(R.string.trip_select_location))){
            appToast(getString(R.string.trip_location_required));
            return;
        }

        if(description.getText().equals(getString(R.string.trip_description))){
            appToast(getString(R.string.trip_descripton_required));
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

        postRequest("users/"+user.getInt("id")+"/trips", trip);

    }

    public void callback(JSONObject response, int statusCode, String method){

        switch (statusCode){
            case 200:
//                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                try {

                    finish();
                    Intent intent = new Intent(this, EditTripActivity.class);
                    intent.putExtra("trip_id", response.getInt("id"));
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
                        appToast(errorValidation.get(0).toString());
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

    public void showMapDialog(View view){
        Intent intent = new Intent(this, NewMapDirectory.class);
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
                    locationSelected.setVisibility(View.VISIBLE);
                    location.setText(getString(R.string.trip_location_selected));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
