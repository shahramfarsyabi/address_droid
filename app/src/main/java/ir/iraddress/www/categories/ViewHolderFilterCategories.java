package ir.iraddress.www.categories;

import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.MainViewHolder;
import ir.iraddress.www.R;

/**
 * Created by shahram on 3/17/17.
 */

public class ViewHolderFilterCategories extends MainViewHolder {

    LinearLayout categoryItem;
    public ViewHolderFilterCategories(View itemView) {
        super(itemView);
        categoryItem = (LinearLayout) itemView.findViewById(R.id.viewholder_categories_filter_item);
    }
}
