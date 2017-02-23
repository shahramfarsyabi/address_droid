package ir.iraddress.www.categories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/20/17.
 */

public class SubCategoryHeaderHolder extends RecyclerView.ViewHolder {
    LinearLayout linearLayout;
    public SubCategoryHeaderHolder(View itemView) {
        super(itemView);

        linearLayout = (LinearLayout) itemView.findViewById(R.id.viewholder_sub_categories_header);
    }
}
