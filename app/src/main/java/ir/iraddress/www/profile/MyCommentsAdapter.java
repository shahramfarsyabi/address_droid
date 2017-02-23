package ir.iraddress.www.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/16/17.
 */

public class MyCommentsAdapter extends RecyclerView.Adapter<MyCommentsHolder> {
    public LayoutInflater inflater;
    public Context context;

    public MyCommentsAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public MyCommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyCommentsHolder(inflater.inflate(R.layout.viewholder_profile_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(MyCommentsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
