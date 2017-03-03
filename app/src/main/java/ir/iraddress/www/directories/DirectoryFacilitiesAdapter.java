package ir.iraddress.www.directories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;

/**
 * Created by shahram on 3/4/17.
 */

public class DirectoryFacilitiesAdapter extends RecyclerView.Adapter<DirectoryFacilityHolder> {

    LayoutInflater inflater;
    Context context;

    public DirectoryFacilitiesAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public DirectoryFacilityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectoryFacilityHolder(inflater.inflate(R.layout.viewholder_directory_facility, parent, false));
    }

    @Override
    public void onBindViewHolder(DirectoryFacilityHolder holder, int position) {
        TextViewIranSans facility = (TextViewIranSans) holder.facility.findViewById(R.id.facility);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
