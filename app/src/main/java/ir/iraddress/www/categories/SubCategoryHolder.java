package ir.iraddress.www.categories;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/20/17.
 */

public class SubCategoryHolder extends RecyclerView.ViewHolder {


    public CardView cardView;

    public SubCategoryHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.viewholder_sub_category);
    }
}
