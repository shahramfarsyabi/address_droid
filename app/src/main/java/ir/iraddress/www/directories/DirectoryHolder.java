package ir.iraddress.www.directories;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/13/17.
 */

public class DirectoryHolder extends RecyclerView.ViewHolder {

    public CardView cardView;

    public DirectoryHolder(View itemView) {
        super(itemView);

        cardView = (CardView) itemView.findViewById(R.id.viewholder_directory_item);
    }
}
