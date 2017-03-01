package ir.iraddress.www.extend;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import ir.iraddress.www.R;


public class AppButton extends Button {

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        Typeface faceNormal =Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansWeb.ttf");
        Typeface faceBold =Typeface.createFromAsset(context.getAssets(), "fonts/ttf/IRANSansBold.ttf");

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AppButton,
                0, 0);

        try {
            int fontType = a.getInteger(R.styleable.AppButton_faFont, 2);
            switch (fontType){
                case 1:
                    this.setTypeface(faceBold);
                    break;

                case 2:
                    this.setTypeface(faceNormal);
                    break;

            }
        } finally {
            a.recycle();
        }

    }
}
