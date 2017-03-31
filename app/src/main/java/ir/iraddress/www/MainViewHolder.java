package ir.iraddress.www;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by shahram on 3/17/17.
 */

public class MainViewHolder extends RecyclerView.ViewHolder {

    Typeface typeface;
    Context context;

    public MainViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        typeface = Typeface.createFromAsset(context.getAssets(),"fonts/ttf/IRANSansWeb.ttf");
        findTextView(itemView);
    }



    public void findTextView(View parent){


        if (parent instanceof ViewGroup) {

            for(int n = 0; n < ((ViewGroup) parent).getChildCount(); n++ ){

                if(((ViewGroup) parent).getChildAt(n) instanceof TextView){

                    TextView textView = (TextView) ((ViewGroup) parent).getChildAt(n);
                    textView.setTypeface(typeface);
                }

                if(((ViewGroup) parent).getChildAt(n) instanceof EditText){

                    EditText textView = (EditText) ((ViewGroup) parent).getChildAt(n);
                    textView.setTypeface(typeface);
                }

                if(((ViewGroup) parent).getChildAt(n) instanceof TextInputLayout){

                    TextInputLayout textView = (TextInputLayout) ((ViewGroup) parent).getChildAt(n);
                    textView.setTypeface(typeface);
                }

                if(((ViewGroup) parent).getChildAt(n) instanceof ViewGroup){
                    findTextView(((ViewGroup) parent).getChildAt(n));
                }
            }

        }


    }


}
