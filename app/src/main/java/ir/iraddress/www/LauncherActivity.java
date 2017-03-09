package ir.iraddress.www;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by shahram on 3/9/17.
 */

public class LauncherActivity extends MainController {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        final Context context = this;
        getWindow().getDecorView().getRootView().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }
}
