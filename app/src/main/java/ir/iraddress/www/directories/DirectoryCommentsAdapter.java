package ir.iraddress.www.directories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ir.iraddress.www.R;
import ir.iraddress.www.extend.TextViewIranSans;
import ir.iraddress.www.extend.TextViewIranSansBold;

/**
 * Created by shahram on 2/23/17.
 */

public class DirectoryCommentsAdapter extends RecyclerView.Adapter<DirectoryCommentHolder> {

    LayoutInflater inflater;
    Context context;
    List collection;

    public DirectoryCommentsAdapter(Context context, List collection){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.collection = collection;
    }

    @Override
    public DirectoryCommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectoryCommentHolder(inflater.inflate(R.layout.viewholder_directory_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(DirectoryCommentHolder holder, int position) {
        try {

            JSONObject comment = (JSONObject) collection.get(position);
            System.out.println(comment);
            TextViewIranSans client = (TextViewIranSans) holder.comment.findViewById(R.id.comment_owner_time);
            client.setText(comment.getString("created_at") + " توسط " + comment.getJSONObject("owner").getString("fullName"));

            TextViewIranSans text = (TextViewIranSans) holder.comment.findViewById(R.id.comment_text);
            text.setText(Html.fromHtml(comment.getString("text")));

            TextViewIranSansBold like = (TextViewIranSansBold) holder.comment.findViewById(R.id.comment_like);
            like.setText(comment.getString("like"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return collection.size();
    }
}
