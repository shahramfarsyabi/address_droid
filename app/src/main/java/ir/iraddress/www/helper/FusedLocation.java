package ir.iraddress.www.helper;

/**
 * Created by shahram on 2/16/17.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import org.json.JSONException;
import org.json.JSONObject;




public class FusedLocation implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public Context context;
    public Activity activity;
    public JSONObject jsonObject = new JSONObject();
    SharedPreferences sharedPreferences;

    public FusedLocation(Activity activity, Context context) throws JSONException {

        this.activity = activity;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            jsonObject.put("lat", location.getLatitude());
            jsonObject.put("lng", location.getLongitude());
            sharedPreferences.edit().putString("myLocation", jsonObject.toString()).apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            try {

                jsonObject.put("lat", mLastLocation.getLatitude());
                jsonObject.put("lng", mLastLocation.getLongitude());
                sharedPreferences.edit().putString("myLocation", jsonObject.toString()).apply();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("FUSE "+String.valueOf(mLastLocation.getLatitude()));
            System.out.println("FUSE "+String.valueOf(mLastLocation.getLongitude()));
        }
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

    public void onDestroy() {
        mGoogleApiClient.disconnect();
    }

    public JSONObject getLocation(){
        try {
            System.out.println(sharedPreferences.getString("myLocation", ""));
            return new JSONObject(sharedPreferences.getString("myLocation", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JSONObject();
    }

}
