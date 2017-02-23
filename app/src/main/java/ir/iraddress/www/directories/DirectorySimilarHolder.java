package ir.iraddress.www.directories;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/23/17.
 */

public class DirectorySimilarHolder extends RecyclerView.ViewHolder {

    public CardView cardView;

    public DirectorySimilarHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardview_similar_item);
    }
}
