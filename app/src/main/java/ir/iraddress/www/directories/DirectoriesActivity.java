package ir.iraddress.www.directories;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;
import ir.iraddress.www.interfaces.SliderViewInterface;
import ir.iraddress.www.models.SliderModel;


public class DirectoriesActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directories);

        route = "directories";
        extras = getIntent().getExtras();

        if(extras.containsKey("route")){
            route = extras.getString("route");
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String locationString =  sharedPreferences.getString("location", "");

        if(!locationString.isEmpty()){
            try {
                JSONObject location = new JSONObject(locationString);
                if(location.has("city")){
                    params.put("city_title", location.getString("city"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String toolbarPageTitle = "بهترین های آدرس";
        if(extras.containsKey("toolbar_title")){

            TextViewIranSansBold toolbarTitle = (TextViewIranSansBold) findViewById(R.id.toolbar_title_directories);
            toolbarPageTitle = extras.getString("toolbar_title");
            toolbarTitle.setText(extras.getString("toolbar_title"));

        }

        fetchData(1, "", params);

        final SliderLayout sliderLayout = (SliderLayout) findViewById(R.id.sliderDirectories);
        sliderLayout.setVisibility(View.VISIBLE);
        Display display = getWindowManager().getDefaultDisplay();
        android.view.ViewGroup.LayoutParams layoutParams = sliderLayout.getLayoutParams();
        layoutParams.width = display.getWidth();
        layoutParams.height = (int) ((display.getWidth()*56.25)/100);
        sliderLayout.setLayoutParams(layoutParams);
        sliderLayout.setVisibility(View.GONE);

        SliderModel sliderModel = new SliderModel();
        RequestParams params = new RequestParams();

        params.add("type", "dynamic_page");
        params.add("page_url", "/"+toolbarPageTitle.replaceAll(" ","-"));

        sliderModel.fetch(params, new SliderViewInterface(){
            @Override
            public void execute(JSONArray sliders) {

                if(sliders.length() > 0){
                    sliderLayout.setVisibility(View.VISIBLE);
                    for(int n = 0; n < sliders.length(); n++){

                        DefaultSliderView textSliderView = new DefaultSliderView(context);
                        // initialize a SliderLayout
                        try {

                            JSONObject slider = (JSONObject) sliders.get(n);
                            System.out.println(slider.getString("src"));

                            textSliderView
                                    .image(slider.getString("src"))
                                    .setScaleType(BaseSliderView.ScaleType.Fit);
                            sliderLayout.addSlider(textSliderView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.directories_recyclerview);
        recyclerViewAdapter = new DirectoriesAdapter(this, this, collection);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);

        render();

    }



}
