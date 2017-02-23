package ir.iraddress.www.extend;

/**
 * Created by shahram on 2/15/17.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

class TextInputEditTextIranSans extends TextInputEditText {

    public TextInputEditTextIranSans(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.setTypeface(face);
    }

    public TextInputEditTextIranSans(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.setTypeface(face);
    }

    public TextInputEditTextIranSans(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
    }
}
