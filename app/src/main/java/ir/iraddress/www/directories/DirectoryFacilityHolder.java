package ir.iraddress.www.directories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;

/**
 * Created by shahram on 3/4/17.
 */

public class DirectoryFacilityHolder extends RecyclerView.ViewHolder {

    public LinearLayout facility;

    public DirectoryFacilityHolder(View itemView) {
        super(itemView);
        facility = (LinearLayout) itemView.findViewById(R.id.viewholder_directory_facility);
    }
}
