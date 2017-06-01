package ir.iraddress.www.mainMenu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.R;
import ir.iraddress.www.authentication.SignInActivity;
import ir.iraddress.www.authentication.SignUpActivity;
import ir.iraddress.www.directories.DirectoriesActivity;
import ir.iraddress.www.extend.AppButton;
import ir.iraddress.www.festival.ImageFestivalActivity;
import ir.iraddress.www.helper.SharedPrefered;
import ir.iraddress.www.interfaces.SliderViewInterface;
import ir.iraddress.www.lottory.LottoryActivity;
import ir.iraddress.www.models.SliderModel;
import ir.iraddress.www.profile.MyTripsActivity;
import ir.iraddress.www.whereIsHere.whereIsHereActivity;

public class MainMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public LayoutInflater inflater;
    public Context context;
    public String[] collection;
    public Typeface typeface;
    Activity activity;

    public MainMenuAdapter(Context context, Activity activity, String[] menus){
        inflater = LayoutInflater.from(context);
        this.context = context;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.collection = menus;
        this.activity = activity;
    }

    public class HomeTopSectionHolder extends RecyclerView.ViewHolder {

        public LinearLayout linearLayout;
        public HomeTopSectionHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.home_top_section);
        }
    }

    public class MainMenuHolder extends RecyclerView.ViewHolder {

        public LinearLayout menu;
        public MainMenuHolder(View itemView) {
            super(itemView);
            menu = (LinearLayout) itemView.findViewById(R.id.linear_mainmenu_item);
        }
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println(viewType);
        switch(viewType){

            case 0:
                return new HomeTopSectionHolder(inflater.inflate(R.layout.home_top, parent, false));


            default:
                return new MainMenuHolder(inflater.inflate(R.layout.viewholder_item_main_menu, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 0:
                HomeTopSectionHolder homeTopSectionHolder = (HomeTopSectionHolder) holder;

                StaggeredGridLayoutManager.LayoutParams layoutParamsHeader = (StaggeredGridLayoutManager.LayoutParams) ((HomeTopSectionHolder) holder).linearLayout.getLayoutParams();
                layoutParamsHeader.setFullSpan(true);

                final SliderLayout sliderLayout = (SliderLayout) homeTopSectionHolder.linearLayout.findViewById(R.id.slider);

                Display display = activity.getWindowManager().getDefaultDisplay();
                android.view.ViewGroup.LayoutParams layoutParams = sliderLayout.getLayoutParams();
                layoutParams.width = display.getWidth();
                layoutParams.height = (int) ((display.getWidth()*56.25)/100);
                sliderLayout.setLayoutParams(layoutParams);
                sliderLayout.setVisibility(View.GONE);
//                String[] fakePhoto = {"http://www.iraddress.ir/files/sliders/Slide1.jpg", "http://www.iraddress.ir/files/sliders/Slide2.jpg", "http://www.iraddress.ir/files/sliders/Slide3.jpg", "http://www.iraddress.ir/files/sliders/Slide4.jpg"};




                SliderModel sliderModel = new SliderModel();
                RequestParams params = new RequestParams();

                params.add("type", "dynamic_page");
                params.add("page_url", "/");

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


                break;

            default:

                MainMenuHolder mainMenuHolder = (MainMenuHolder) holder;
                TextView textView = (TextView) mainMenuHolder.menu.findViewById(R.id.menu_title);
                textView.setText(collection[position]);
                textView.setTypeface(typeface);
                ImageView button = (ImageView) mainMenuHolder.menu.findViewById(R.id.menu_image);
                switch (position){
                    case 1:
                        button.setImageResource(R.drawable.image1);
                        break;
                    case 2:
                        button.setImageResource(R.drawable.image2);
                        break;

                    case 3:
                        button.setImageResource(R.drawable.image3);
                        break;

                    case 4:
                        button.setImageResource(R.drawable.image4);
                        break;

                    case 5:
                        button.setImageResource(R.drawable.image5);
                        break;

                    case 6:
                        StaggeredGridLayoutManager.LayoutParams layoutParamsMenu = (StaggeredGridLayoutManager.LayoutParams) ((MainMenuHolder) holder).menu.getLayoutParams();
                        layoutParamsMenu.setFullSpan(true);
                        button.setImageResource(R.drawable.image6);
                        break;

                    case 7:
                        button.setImageResource(R.drawable.image7);
                        break;

                    case 8:
                        button.setImageResource(R.drawable.image8);
                        break;

                }

                mainMenuHolder.menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        switch(position){
                            case 1:
                                intent = new Intent(context, DirectoriesActivity.class);
                                intent.putExtra("route", "directories/featured");
                                intent.putExtra("toolbar_title", "بهترین های آدرس");
                                context.startActivity(intent);
                                break;

                            case 2:
                                intent = new Intent(context, DirectoriesActivity.class);
                                intent.putExtra("route", "directories/newest");
                                intent.putExtra("toolbar_title", "تازه ها");
                                context.startActivity(intent);
                                break;

                            case 3:
                                intent = new Intent(context, DirectoriesActivity.class);
                                intent.putExtra("route", "directories/discount");
                                intent.putExtra("toolbar_title", "تخفیف ها");
                                context.startActivity(intent);
                                break;

                            case 4:

                                break;

                            case 5:
                                intent = new Intent(context, ImageFestivalActivity.class);
                                context.startActivity(intent);
                                break;

                            case 6:
                                intent = new Intent(context, whereIsHereActivity.class);
                                context.startActivity(intent);
                                break;

                            case 7:
                                intent = new Intent(context, LottoryActivity.class);
                                context.startActivity(intent);
                                break;

                            case 8:

                                try {

                                    SharedPrefered sharedPrefered = new SharedPrefered(context, "user");
                                    JSONObject user = null;

                                    if(sharedPrefered.count() > 0){
                                        user = sharedPrefered.findByIndex(0);
                                    }

                                    if(user == null){

                                        final Dialog dialogAuth = new Dialog(context, R.style.MyDialogSize);
                                        dialogAuth.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialogAuth.setContentView(R.layout.dialog_authenticate);

                                        AppButton signIn = (AppButton) dialogAuth.findViewById(R.id.btnSignInOnClick);
                                        AppButton signUp = (AppButton) dialogAuth.findViewById(R.id.btnSignUpOnClick);

                                        signIn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(context, SignInActivity.class);
                                                context.startActivity(intent);
                                                dialogAuth.cancel();
                                            }
                                        });

                                        signUp.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(context, SignUpActivity.class);
                                                context.startActivity(intent);
                                                dialogAuth.cancel();
                                            }
                                        });

                                        dialogAuth.show();

                                        return;
                                    }

                                    intent = new Intent(context, MyTripsActivity.class);
                                    context.startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                break;


                        }
                    }
                });


                break;
        }

    }

    @Override
    public int getItemCount() {
        return collection.length;
    }
}
