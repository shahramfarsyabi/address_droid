package ir.iraddress.www.categories;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.directories.DirectoriesActivity;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;

public class SubCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LayoutInflater inflater;
    Context context;
    List collection;
    Typeface typeface;
    Activity activity;

    public SubCategoriesAdapter(Context context, Activity activity, List collection){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = collection;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType){
            case 0:
                return new SubCategoryHeaderHolder(inflater.inflate(R.layout.viewholder_sub_categories_header, parent, false));

            default:
                return new SubCategoryHolder(inflater.inflate(R.layout.viewholder_sub_categories, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case 0:

                SubCategoryHeaderHolder subCategoryHeaderHolder = (SubCategoryHeaderHolder) holder;
                Button nearOnMap = (Button) subCategoryHeaderHolder.linearLayout.findViewById(R.id.near_on_map);
                nearOnMap.setTypeface(typeface);

                SliderLayout sliderLayout = (SliderLayout) subCategoryHeaderHolder.linearLayout.findViewById(R.id.slider);
                Display display = activity.getWindowManager().getDefaultDisplay();
                android.view.ViewGroup.LayoutParams layoutParams = sliderLayout.getLayoutParams();
                layoutParams.width = display.getWidth();
                layoutParams.height = (int) ((display.getWidth()*56.25)/100);
                sliderLayout.setLayoutParams(layoutParams);
                String[] fakePhoto = {"http://www.iraddress.ir/files/sliders/Slide1.jpg", "http://www.iraddress.ir/files/sliders/Slide2.jpg", "http://www.iraddress.ir/files/sliders/Slide3.jpg", "http://www.iraddress.ir/files/sliders/Slide4.jpg"};

                for(int n = 0; n < fakePhoto.length; n++){

                    DefaultSliderView textSliderView = new DefaultSliderView(context);
                    // initialize a SliderLayout
                    textSliderView
                            .image(fakePhoto[n])
                            .setScaleType(BaseSliderView.ScaleType.Fit);

                    sliderLayout.addSlider(textSliderView);

                }

                break;

            default:
                SubCategoryHolder subCategoryHolder = (SubCategoryHolder) holder;
                try {
                    final JSONObject object = (JSONObject) collection.get((position-1));

                    TextViewIranSansBold title = (TextViewIranSansBold) subCategoryHolder.cardView.findViewById(R.id.sub_category_title);
                    title.setText(object.getString("title"));

                    subCategoryHolder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                Intent intent = new Intent(context, DirectoriesActivity.class);
                                intent.putExtra("route", "categories/"+object.getInt("id")+"/directories");
                                intent.putExtra("toolbar_title", object.getString("title"));
                                intent.putExtra("category_id", object.getInt("id"));
                                context.startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (collection.size()+1);
    }
}
