package ir.iraddress.www.clients;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoriesAdapter;
import ir.iraddress.www.extend.TextViewIranSansBold;
import ir.iraddress.www.profile.PublicProfileActivity;

/**
 * Created by shahram on 3/10/17.
 */

public class ClientsActivity extends MainController {

    Timer timer = new Timer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients);

        route = "users";
        extras = getIntent().getExtras();

        fetchData(1, "", params);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_clients);
        recyclerViewAdapter = new ClientsAdapter(this, this, collection);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.removeAllViewsInLayout();
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        render();

        final TextInputEditText searchInput = (TextInputEditText) findViewById(R.id.search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                timer.cancel();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timer.cancel();
                params = new RequestParams();

                if(s.length() <= 0){
                    collection.clear();
                    recyclerView.removeAllViewsInLayout();
                    fetchData(1, route, params);
                    return;
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {

                collection.clear();
                params = new RequestParams();

                if(s.length() <= 0){
                    return;
                }

                timer = new Timer();
                params.put("query", s.toString());

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                    searchInput.post(new Runnable() {
                        @Override
                        public void run() {

                            recyclerView.removeAllViewsInLayout();
                            fetchData(1, route, params);

                        }
                    });
                    }
                }, 3000);
            }
        });
    }

    public void btnShowProfile(View view) throws JSONException {
        JSONObject client = (JSONObject) view.getTag();

        Intent intent = new Intent(this, PublicProfileActivity.class);
        intent.putExtra("user_id", client.getInt("id"));
        startActivity(intent);
    }
}
