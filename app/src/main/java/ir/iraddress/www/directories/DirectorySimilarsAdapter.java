package ir.iraddress.www.directories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/23/17.
 */

public class DirectorySimilarsAdapter extends RecyclerView.Adapter<DirectorySimilarHolder> {

    public Context context;
    public LayoutInflater inflater;

    public DirectorySimilarsAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public DirectorySimilarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectorySimilarHolder(inflater.inflate(R.layout.viewholder_directory_similar, parent, false));
    }

    @Override
    public void onBindViewHolder(DirectorySimilarHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
