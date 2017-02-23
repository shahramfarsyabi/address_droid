package ir.iraddress.www.map;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/20/17.
 */

public class MapItemShowLinearHolder extends RecyclerView.ViewHolder {

    public CardView cardView;

    public MapItemShowLinearHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.viewholder_map_item_linear);
    }
}
