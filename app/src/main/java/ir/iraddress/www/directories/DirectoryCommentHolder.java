package ir.iraddress.www.directories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.R;

/**
 * Created by shahram on 2/23/17.
 */

public class DirectoryCommentHolder extends RecyclerView.ViewHolder {

    public LinearLayout comment;

    public DirectoryCommentHolder(View itemView) {
        super(itemView);
        comment = (LinearLayout) itemView.findViewById(R.id.directory_comment_holde);
    }
}
