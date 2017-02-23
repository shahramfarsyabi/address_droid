package ir.iraddress.www.categories;

import android.content.Context;
import android.content.Intent;
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
 * Created by shahram on 2/19/17.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryHolder> {

    public LayoutInflater inflater;
    public Context context;
    public List collection;

    public CategoriesAdapter(Context context, List collection){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = collection;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(inflater.inflate(R.layout.viewholder_main_categories, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {

        final JSONObject object = (JSONObject) collection.get(position);

        try {

            TextViewIranSans textViewIranSans = (TextViewIranSans) holder.category.findViewById(R.id.main_category_title);
            textViewIranSans.setText(object.getString("title"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(context, CategoryActivity.class);
                    intent.putExtra("main_category_id", object.getInt("id"));
                    intent.putExtra("main_category_title", object.getString("title"));
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
