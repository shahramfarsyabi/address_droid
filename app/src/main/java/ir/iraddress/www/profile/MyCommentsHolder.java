package ir.iraddress.www.profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyCommentsHolder extends RecyclerView.ViewHolder {

    public LinearLayout comment;

    public MyCommentsHolder(View itemView) {
        super(itemView);
        comment = (LinearLayout) itemView.findViewById(R.id.viewholder_profile_comment);
    }
}
