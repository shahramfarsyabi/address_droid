package ir.iraddress.www.helper;

/**
 * Created by shahram on 3/2/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class BackgroundSync extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo()!=null){
            Toast.makeText(context, "Connected to Internet", Toast.LENGTH_LONG).show();
        }
        else Log.i("INTERNET","---------------------> Internet Disconnected. ");
    }
}