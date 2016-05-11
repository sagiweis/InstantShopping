package com.dys.instantshopping.adapters;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.fragments.AddEditProductFragment;
import com.dys.instantshopping.objects.ShoppingList;

/**
 * Created by Sagi on 04/05/2016.
 */
public class GroupListAdapter extends BaseAdapter {

    private Context mContext;
    private ShoppingList list;

    public GroupListAdapter(Context c, ShoppingList l) {
        mContext = c;
        list = l;
    }
public ShoppingList getShoppingList(){
    return this.list;
}

    @Override
    public int getCount() {
        return list.getProductsList().size();
    }

    @Override
    public Object getItem(int position) {
        return list.getProductsList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.group_list_row, null, false);

        TextView name = (TextView) convertView.findViewById(R.id.productName);
        TextView description = (TextView) convertView.findViewById(R.id.productDescription);
        TextView amount = (TextView) convertView.findViewById(R.id.productAmount);
        ImageButton editButton = (ImageButton) convertView.findViewById(R.id.productEdit);

        name.setText(list.getProductsList().get(position).getName());
        description.setText(list.getProductsList().get(position).getDescription());
        amount.setText(String.valueOf(list.getProductsList().get(position).getAmount()));

        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new AddEditProductFragment();
                Bundle args = new Bundle();
                args.putInt("itemIndex", position);
                newFragment.setArguments(args);
                newFragment.show(((Activity) mContext).getFragmentManager(), "dialog");
            }
        });

        return convertView;
    }
}
