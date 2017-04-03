package ir.iraddress.www.profile;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ir.iraddress.www.MainViewHolder;
import ir.iraddress.www.R;

/**
 * Created by shahram on 4/3/17.
 */

public class MyTripHolder extends MainViewHolder {

    public CardView cardView;

    public MyTripHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.viewholder_trip_item);
    }
}
