package ir.iraddress.www.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/17/17.
 */

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionHolder> {

    public LayoutInflater inflater;
    public Context context;

    public ConnectionsAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ConnectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConnectionHolder(inflater.inflate(R.layout.viewholder_connection, parent, false));
    }

    @Override
    public void onBindViewHolder(ConnectionHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
