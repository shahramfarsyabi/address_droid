package ir.iraddress.www.extend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewIranSans extends TextView {


    public TextViewIranSans(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.setTypeface(face);
    }

    public TextViewIranSans(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.setTypeface(face);
    }

    public TextViewIranSans(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }

}