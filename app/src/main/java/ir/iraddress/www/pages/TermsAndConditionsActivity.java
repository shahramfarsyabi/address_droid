package ir.iraddress.www.pages;

import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;

/**
 * Created by shahram on 2/14/17.
 */

public class TermsAndConditionsActivity extends MainController {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_page);

        TextView title = (TextView) findViewById(R.id.toolbar_title_page);
        title.setText("شرایط استفاده از سرویس");

        ImageView image = (ImageView) findViewById(R.id.toolbar_image_page);
        image.setImageResource(R.drawable.gift_box);

        TextView text = (TextView) findViewById(R.id.text_wrapper);
        text.setText(Html.fromHtml("<p>لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. چاپگرها و متون بلکه روزنامه و مجله در ستون و سطرآنچنان که لازم است و برای شرایط فعلی تکنولوژی مورد نیاز و کاربردهای متنوع با هدف بهبود ابزارهای کاربردی می باشد.</p>"));
        text.append(Html.fromHtml("<p>لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. چاپگرها و متون بلکه روزنامه و مجله در ستون و سطرآنچنان که لازم است و برای شرایط فعلی تکنولوژی مورد نیاز و کاربردهای متنوع با هدف بهبود ابزارهای کاربردی می باشد. کتابهای زیادی در شصت و سه درصد گذشته، حال و آینده شناخت فراوان جامعه و متخصصان را می طلبد تا با نرم افزارها شناخت.</p>"));

    }
}
