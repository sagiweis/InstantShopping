package com.dys.instantshopping.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.objects.ShoppingList;
import com.dys.instantshopping.utilities.ImageParser;

import java.util.ArrayList;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView productName;
        TextView productAmount;
        TextView productDescription;
        Button editProductButton;
        LinearLayout layout;
        if (convertView == null) {

            productName = new TextView(mContext);
            productAmount = new TextView(mContext);
            productDescription = new TextView(mContext);
            editProductButton = new Button(mContext);

            layout = new LinearLayout(mContext);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
            layout.setPadding(5, 5, 5, 5);
            layout.addView(productName);
            layout.addView(productAmount);
            layout.addView(productDescription);
            layout.addView(editProductButton);
        } else {
            layout = (LinearLayout) convertView;
            productName = (TextView) layout.getChildAt(0);
            productAmount = (TextView) layout.getChildAt(1);
            productDescription = (TextView) layout.getChildAt(2);
            editProductButton = (Button) layout.getChildAt(3);
        }

        productName.setText(list.getProductsList().get(position).getName());
        productAmount.setText(String.valueOf(list.getProductsList().get(position).getAmount()));
        productDescription.setText(list.getProductsList().get(position).getDescription());

        return layout;
    }
}
