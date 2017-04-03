package ir.iraddress.www.new_directory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;


public class NewTripActivity extends MainController implements DatePickerDialog.OnDateSetListener {

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
        formData.put("city_id", "");

        Toast.makeText(context, "Test save", Toast.LENGTH_SHORT).show();

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
