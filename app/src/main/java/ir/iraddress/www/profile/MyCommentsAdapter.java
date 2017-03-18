package ir.iraddress.www.profile;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;

/**
 * Created by shahram on 2/16/17.
 */

public class MyCommentsAdapter extends RecyclerView.Adapter<MyCommentsHolder> {
    public LayoutInflater inflater;
    public Context context;
    public List collection;
    public MyCommentsAdapter(Context context, List collection){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = collection;
    }

    @Override
    public MyCommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyCommentsHolder(inflater.inflate(R.layout.viewholder_profile_comments, parent, false));
    }

    @Override
    public void onBindViewHolder(MyCommentsHolder holder, int position) {


        try {
            JSONObject object = (JSONObject) collection.get(position);

            AppCompatButton btnRemove = (AppCompatButton) holder.comment.findViewById(R.id.btn_remove_comment);
            JSONObject comment = new JSONObject();

            comment.put("comment_id", object.getInt("id"));
            comment.put("position_id", position);

            btnRemove.setTag(comment);


            TextViewIranSans directoryTitle = (TextViewIranSans) holder.comment.findViewById(R.id.comment_directory_title);
            TextViewIranSans commentContent = (TextViewIranSans) holder.comment.findViewById(R.id.comment_content);


            directoryTitle.setText(object.getString("created_at"));
            directoryTitle.append(" برای "+object.getJSONObject("parent").getString("title"));
            commentContent.setText(Html.fromHtml(object.getString("text")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
