package ir.iraddress.www.directories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;

public class DirectoryFacilitiesAdapter extends RecyclerView.Adapter<DirectoryFacilityHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List collection;

    public DirectoryFacilitiesAdapter(Context context, List facilities){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = facilities;
    }

    @Override
    public DirectoryFacilityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectoryFacilityHolder(inflater.inflate(R.layout.viewholder_directory_facility, parent, false));
    }

    @Override
    public void onBindViewHolder(DirectoryFacilityHolder holder, int position) {


        try {

            JSONObject object = (JSONObject) collection.get(position);
            TextViewIranSans facility = (TextViewIranSans) holder.facility.findViewById(R.id.facility);
            facility.setText(object.getString("title"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
