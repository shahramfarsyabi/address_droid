package ir.iraddress.www.clients;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import ir.iraddress.www.R;

/**
 * Created by shahram on 3/10/17.
 */

public class ClientHolder extends RecyclerView.ViewHolder {

    public LinearLayout linearLayout;

    public ClientHolder(View itemView) {

        super(itemView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.client_card_view);
    }
}
