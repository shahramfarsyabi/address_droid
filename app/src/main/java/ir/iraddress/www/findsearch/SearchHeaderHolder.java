package ir.iraddress.www.findsearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/20/17.
 */

public class SearchHeaderHolder extends RecyclerView.ViewHolder {
    public LinearLayout linearLayout;

    public SearchHeaderHolder(View itemView) {
        super(itemView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.search_header);
    }
}
