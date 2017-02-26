package ir.iraddress.www.helper;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.telephony.CellLocation.requestLocationUpdate;


public class MyLocationServiceManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSIONS_REQUEST_CODE_FOR_FINE_LOCATION = 1001;

    public int delay = 60000;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    public Context context;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    public MyLocationServiceManager(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval((this.delay * 2));
        mLocationRequest.setFastestInterval(this.delay);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("onConnected");
        startLocationUpdates();
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE_FOR_FINE_LOCATION);
            return;

        }else if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_FOR_FINE_LOCATION);
            return;

        }


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                System.out.println("LOCATION UPDATED");

                System.out.println(String.valueOf(location.getLatitude()));
                System.out.println(String.valueOf(location.getLongitude()));

                JSONObject locationObject = new JSONObject();

                try {

                    locationObject.put("lat", String.valueOf(location.getLatitude()));
                    locationObject.put("lng", String.valueOf(location.getLongitude()));


                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    String city = addresses.get(0).getAddressLine(1);
                    locationObject.put("city", city);

                    sharedPreferences.edit().putString("location", locationObject.toString()).apply();

                } catch (JSONException e) {

                    lastLocation();

                    e.printStackTrace();

                } catch (IOException e) {

                    lastLocation();

                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void connect() {
        mGoogleApiClient.connect();
    }

    public JSONObject getLocation() throws JSONException {

        JSONObject jsonObject;

        if(sharedPreferences.contains("location")){

            String locationString = sharedPreferences.getString("location", "");
            if(!locationString.isEmpty()) {
                jsonObject = new JSONObject(locationString);

                return jsonObject;
            }
        }

        lastLocation();

        jsonObject = new JSONObject();
        jsonObject.put("lat", "35.6967326");
        jsonObject.put("lng", "51.2093927");
        jsonObject.put("city", "Tehran");

        return jsonObject;
    }

    private void lastLocation() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLastLocation != null){

            System.out.println("last location");
            System.out.println("FUSE "+String.valueOf(mLastLocation.getLatitude()));
            System.out.println("FUSE "+String.valueOf(mLastLocation.getLongitude()));

            JSONObject locationObject = new JSONObject();

            try {

                locationObject.put("lat", String.valueOf(mLastLocation.getLatitude()));
                locationObject.put("lng", String.valueOf(mLastLocation.getLongitude()));

                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                String city = addresses.get(0).getAddressLine(1);
                locationObject.put("city", city);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sharedPreferences.edit().putString("location", locationObject.toString()).apply();
        }
    }
}
