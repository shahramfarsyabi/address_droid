package ir.iraddress.www.new_directory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;

/**
 * Created by shahram on 3/20/17.
 */

public class FacilitiesAdapter extends RecyclerView.Adapter<FacilityHolder> {

    Context _context;
    JSONArray _collection;
    LayoutInflater inflater;

    public FacilitiesAdapter(Context context, JSONArray collection){
        inflater = LayoutInflater.from(context);
        _collection = collection;
        _context = context;
    }

    @Override
    public FacilityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FacilityHolder(inflater.inflate(R.layout.viewholder_facility, parent, false));
    }

    @Override
    public void onBindViewHolder(FacilityHolder holder, int position) {
        try {
            JSONObject facility = (JSONObject) _collection.get(position);

            CheckBox facilityTitle = (CheckBox) holder.facility.findViewById(R.id.facility_title);
            facilityTitle.setText(facility.getString("title"));
            facilityTitle.setTag(facility);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return _collection.length();
    }
}
