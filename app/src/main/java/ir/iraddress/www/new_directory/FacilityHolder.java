package ir.iraddress.www.new_directory;

import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.MainViewHolder;
import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;

/**
 * Created by shahram on 3/20/17.
 */

public class FacilityHolder extends MainViewHolder {

    LinearLayout facility;

    public FacilityHolder(View itemView) {
        super(itemView);

        facility = (LinearLayout) itemView.findViewById(R.id.facility_item);
    }
}
