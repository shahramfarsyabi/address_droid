package ir.iraddress.www.profile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;

/**
 * Created by shahram on 2/17/17.
 */

public class ConnectionsActivity extends MainController {


    public void onCreate(Bundle savedInstanceState){

        extras = getIntent().getExtras();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        TextViewIranSans textViewIranSans = (TextViewIranSans) findViewById(R.id.toolbar_title_connections);
        switch(extras.getString("type")){
            case "followers":
                textViewIranSans.setText("دنبال شونده ها");
                break;

            default :
                textViewIranSans.setText("دنبال کننده ها");
                break;
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_connections);
        recyclerViewAdapter = new ConnectionsAdapter(this);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

    }
}
