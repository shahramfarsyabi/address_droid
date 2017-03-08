package ir.iraddress.www.mainMenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import ir.iraddress.www.R;
import ir.iraddress.www.categories.CategoriesActivity;
import ir.iraddress.www.festival.ImageFestivalActivity;
import ir.iraddress.www.lottory.LottoryActivity;

public class MainMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public LayoutInflater inflater;
    public Context context;
    public String[] collection;
    public Typeface typeface;

    public MainMenuAdapter(Context context, String[] menus){
        inflater = LayoutInflater.from(context);
        this.context = context;
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.collection = menus;
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

                break;

            default:

                MainMenuHolder mainMenuHolder = (MainMenuHolder) holder;
                TextView textView = (TextView) mainMenuHolder.menu.findViewById(R.id.menu_title);
                textView.setText(collection[position]);
                textView.setTypeface(typeface);
                ImageView button = (ImageView) mainMenuHolder.menu.findViewById(R.id.menu_image);
                switch (position){
                    case 1:
                        button.setImageResource(R.drawable.menu_1);
                        break;
                    case 2:
                        button.setImageResource(R.drawable.menu_2);
                        break;

                    case 3:
                        button.setImageResource(R.drawable.menu_3);
                        break;

                    case 4:
                        button.setImageResource(R.drawable.menu_4);
                        break;

                    case 5:
                        button.setImageResource(R.drawable.menu_5);
                        break;

                    case 6:
                        button.setImageResource(R.drawable.menu_6);
                        break;

                    case 7:
                        button.setImageResource(R.drawable.menu_7);
                        break;

                    case 8:
                        button.setImageResource(R.drawable.menu_8);
                        break;

                }

                mainMenuHolder.menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        switch(position){
                            case 1:
                                intent = new Intent(context, CategoriesActivity.class);
                                context.startActivity(intent);
                                break;

                            case 5:
                                intent = new Intent(context, ImageFestivalActivity.class);
                                context.startActivity(intent);
                                break;

                            case 7:
                                intent = new Intent(context, LottoryActivity.class);
                                context.startActivity(intent);
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
