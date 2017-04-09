package ir.iraddress.www.whereIsHere;

import android.support.v7.widget.CardView;
import android.view.View;

import ir.iraddress.www.MainViewHolder;
import ir.iraddress.www.R;

/**
 * Created by shahram on 4/9/17.
 */

public class whereIsHereHolder extends MainViewHolder {

    public CardView cardView;
    public whereIsHereHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.where_is_here_viewholder);
    }
}
