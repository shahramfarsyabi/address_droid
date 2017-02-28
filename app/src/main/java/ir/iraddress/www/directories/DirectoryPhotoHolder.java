package ir.iraddress.www.directories;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ir.iraddress.www.R;


public class DirectoryPhotoHolder extends RecyclerView.ViewHolder {

    public LinearLayout photo;

    public DirectoryPhotoHolder(View itemView) {
        super(itemView);

        photo = (LinearLayout) itemView.findViewById(R.id.viewholder_box_directory_image);
    }
}
