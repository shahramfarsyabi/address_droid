package ir.iraddress.www.categories;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;

/**
 * Created by shahram on 3/17/17.
 */

public class AdapterFilterCategories extends RecyclerView.Adapter<ViewHolderFilterCategories> {

    List collection;
    Context context;
    Activity activity;
    LayoutInflater inflater;

    public AdapterFilterCategories(Context context, List categories){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = categories;
    }

    @Override
    public ViewHolderFilterCategories onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderFilterCategories(inflater.inflate(R.layout.viewholder_filter_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderFilterCategories holder, int position) {


        try {
            JSONObject category = (JSONObject) collection.get(position);

            TextViewIranSans categoryTitle = (TextViewIranSans) holder.categoryItem.findViewById(R.id.category_title);
            categoryTitle.setText(category.getString("title"));

            final AppCompatImageView appCompatImageView = (AppCompatImageView) holder.categoryItem.findViewById(R.id.categoryItemCircleCheckbox);
            appCompatImageView.setImageResource(R.drawable.ic_circle_shape_outline);

            holder.categoryItem.setTag(category);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
