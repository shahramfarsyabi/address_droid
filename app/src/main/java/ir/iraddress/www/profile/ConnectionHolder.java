package ir.iraddress.www.profile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/17/17.
 */

public class ConnectionHolder extends RecyclerView.ViewHolder {

    public LinearLayout person;

    public ConnectionHolder(View itemView) {

        super(itemView);
        person = (LinearLayout) itemView.findViewById(R.id.connection_box);
    }
}
