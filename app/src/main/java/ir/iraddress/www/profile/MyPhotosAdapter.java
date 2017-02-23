package ir.iraddress.www.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyPhotosAdapter extends RecyclerView.Adapter<MyPhotosHolder> {

    public LayoutInflater inflater;
    public Context context;

    public MyPhotosAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public MyPhotosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyPhotosHolder(inflater.inflate(R.layout.viewholder_profile_photos, parent, false));
    }

    @Override
    public void onBindViewHolder(MyPhotosHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
