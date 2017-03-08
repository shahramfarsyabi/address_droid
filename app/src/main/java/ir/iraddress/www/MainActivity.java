package ir.iraddress.www;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

import ir.iraddress.www.authentication.SignInActivity;
import ir.iraddress.www.authentication.SignUpActivity;
import ir.iraddress.www.categories.CategoriesActivity;
import ir.iraddress.www.contactus.ContactUsActivity;
import ir.iraddress.www.findsearch.SearchStackActivity;

import ir.iraddress.www.helper.SharedPrefered;
import ir.iraddress.www.mainMenu.MainMenuAdapter;
import ir.iraddress.www.pages.AdvertisingActivity;
import ir.iraddress.www.pages.TermsAndConditionsActivity;
import ir.iraddress.www.profile.ProfileActivity;


public class MainActivity extends MainController implements NavigationView.OnNavigationItemSelectedListener {

    private String[] mainMenu = {"","بهترین های آدرس", "تازه ها", "تخفیف ها", "چی ؟ کجا ؟ چرا", "فستیوال عکس", "تا حالا اینجا بودین", "لاتاری", "سفرهای من" };
    NavigationView navigationView;


    public void onCreate(Bundle savedInstanceState) {

//        getWindow().setStatusBarColor();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            Drawable background = this.getResources().getDrawable(R.drawable.toolbar_address_gradiant);
//            w.setStatusBarColor(getResources().getColor(android.R.color.transparent));
//            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            w.setNavigationBarColor(getResources().getColor(android.R.color.transparent));
//            w.setBackgroundDrawable(background);
////            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
////            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }

//        Intent intent = new Intent();
//        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);
//        startActivity(intent);

        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        myLocationServiceManager.connect();

        try {

            JSONObject locationObject = myLocationServiceManager.getLocation();
            System.out.println("getLocationObject");
            System.out.println(locationObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        typeface = Typeface.createFromAsset(getAssets(), "fonts/ttf/IRANSansWeb.ttf");

        final Context context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        try {
            SharedPrefered sharedPrefered = new SharedPrefered(this, "user");
            if(sharedPrefered.count() > 0){

                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_main_drawer_after_login);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        MainMenuAdapter mainMenuAdapter = new MainMenuAdapter(this, this, mainMenu);

        recyclerView.setAdapter(mainMenuAdapter);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == 0){
                    return 2;
                }
                return 1;
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);

        Button searchBtnStack = (Button) findViewById(R.id.search_page_button);
        searchBtnStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchStackActivity.class);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contactus) {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_terms) {
            Intent intent = new Intent(this, TermsAndConditionsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ads) {
            Intent intent = new Intent(this, AdvertisingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signup){
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signin){
            Intent intent = new Intent(this, SignInActivity.class);
            startActivityForResult(intent, CODE_FOR_LOGIN);
        } else if (id == R.id.nav_profile){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivityForResult(intent, CODE_FOR_LOGOUT);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void searchStack(View view){
        Intent intent = new Intent(this, SearchStackActivity.class);
        startActivity(intent);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){
            case CODE_FOR_LOGOUT:
                if (resultCode == RESULT_OK) {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_main_drawer_before_login);
                }
                break;

            case CODE_FOR_LOGIN:
                if (resultCode == RESULT_OK) {
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.activity_main_drawer_after_login);
                }
                break;
        }

    }

    public void btnOnClickCategories(View view){
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }
}
