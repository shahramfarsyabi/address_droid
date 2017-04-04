package ir.iraddress.www.profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyPhotosHolder extends RecyclerView.ViewHolder {

    public RelativeLayout image;

    public MyPhotosHolder(View itemView) {
        super(itemView);

        image = (RelativeLayout) itemView.findViewById(R.id.holder_photo);
    }
}
