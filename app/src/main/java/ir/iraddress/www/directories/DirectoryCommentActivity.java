package ir.iraddress.www.directories;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ir.iraddress.www.MainController;
import ir.iraddress.www.R;


public class DirectoryCommentActivity extends MainController {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    public void sendCommentToServer(View view){

        TextInputEditText comment = (TextInputEditText) findViewById(R.id.comment_content);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/ttf/IRANSansWeb.ttf");
        TextView textView = new TextView(this);

        if(comment.getText().length() > 0){
            Toast.makeText(this, "نظر شما ثبت و پس از تایید منتشر خواهد شد", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "لطفا نظر خود را بنویسید و سپس دکمه ارسال را فشار دهید", Toast.LENGTH_SHORT).show();
        }
    }
}
