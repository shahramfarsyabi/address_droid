package ir.iraddress.www.notifications;

import android.support.v7.widget.CardView;
import android.view.View;

import ir.iraddress.www.MainViewHolder;
import ir.iraddress.www.R;

/**
 * Created by shahram on 4/11/17.
 */

public class NotificationHolder extends MainViewHolder {

    CardView cardView;

    public NotificationHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.notification_cardview);
    }
}
