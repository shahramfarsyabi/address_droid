package ir.iraddress.www;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shahram on 3/20/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    Context _context;
    JSONArray _collection;
    LayoutInflater _inflater;
    Typeface typeface;

    public ExpandableListAdapter(Context context, JSONArray collection) {
        _inflater = LayoutInflater.from(context);
        _context = context;
        _collection = collection;
        typeface = Typeface.createFromAsset(context.getAssets(),"fonts/ttf/IRANSansWeb.ttf");

    }

    @Override
    public int getGroupCount() {
        return _collection.length();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            JSONObject parent = (JSONObject) _collection.get(groupPosition);
            return parent.getJSONArray("sub_categories").length();
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        try {
            return _collection.get(groupPosition);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        try {
            JSONObject parentCategory = (JSONObject) _collection.get(groupPosition);
            JSONObject childCategory = (JSONObject) parentCategory.getJSONArray("sub_categories").get(groupPosition);
            return childCategory;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {

        try {

            JSONObject parentCategory = (JSONObject) _collection.get(groupPosition);
            return parentCategory.getInt("id");

        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        try {

            JSONObject parentCategory = (JSONObject) _collection.get(groupPosition);
            JSONObject childCategory = (JSONObject) parentCategory.getJSONArray("sub_categories").get(groupPosition);
            return childCategory.getInt("id");

        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        try {

            JSONObject parentCategory = (JSONObject) _collection.get(groupPosition);
            String headerTitle = parentCategory.getString("title");

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.dialog_expandable_category_header, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setText(headerTitle);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        try {

            JSONObject parentCategory = (JSONObject) _collection.get(groupPosition);
            JSONObject childCategory = (JSONObject) parentCategory.getJSONArray("sub_categories").get(childPosition);
            String childText = childCategory.getString("title");

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.dialog_expandable_category_item, null);
            }

            CheckBox txtListChild = (CheckBox) convertView.findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            txtListChild.setTypeface(typeface);
            txtListChild.setTag(childCategory);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
