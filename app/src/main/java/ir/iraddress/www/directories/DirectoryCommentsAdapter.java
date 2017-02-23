package ir.iraddress.www.directories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/23/17.
 */

public class DirectoryCommentsAdapter extends RecyclerView.Adapter<DirectoryCommentHolder> {

    LayoutInflater inflater;
    Context context;

    public DirectoryCommentsAdapter(Context context){
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public DirectoryCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectoryCommentHolder(inflater.inflate(R.layout.viewholder_directory_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(DirectoryCommentHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
