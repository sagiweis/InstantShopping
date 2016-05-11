package com.dys.instantshopping.adapters;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;

/**
 * Created by Dor Albagly on 5/4/2016.
 */
public class ListsHistoryExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<Product>> expandableListDetail;

    public ListsHistoryExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<Product>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final Product expandedListText = (Product) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.hlist_item, null);
        }

        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.productName);
        expandedListTextView.setText(expandedListText.getName());
        expandedListTextView.setTextColor(Color.RED);
        TextView expandedListTextView2 = (TextView) convertView
                .findViewById(R.id.productDescription);
        expandedListTextView2.setText(expandedListText.getDescription());
        TextView expandedListTextView3 = (TextView) convertView
                .findViewById(R.id.productAmount);
        expandedListTextView3.setText(String.valueOf(expandedListText.getAmount()));

        if (!expandedListText.isSelected()){
            expandedListTextView.setTextColor(Color.RED);
            expandedListTextView2.setTextColor(Color.RED);
            expandedListTextView3.setTextColor(Color.RED);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.hlist_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
