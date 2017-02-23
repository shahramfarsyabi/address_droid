package ir.iraddress.www.categories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/19/17.
 */

public class CategoryHolder extends RecyclerView.ViewHolder {

    public LinearLayout category;
    public CategoryHolder(View itemView) {
        super(itemView);

        category = (LinearLayout) itemView.findViewById(R.id.viewholder_box_main_category);
    }
}
