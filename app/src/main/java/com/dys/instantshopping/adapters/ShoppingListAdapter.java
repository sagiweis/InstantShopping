package com.dys.instantshopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;

/**
 * Created by Dor Albagly on 5/5/2016.
 */
public class ShoppingListAdapter extends BaseAdapter{
    private Context context;
    private ShoppingList list;

    public ShoppingListAdapter(Context c, ShoppingList l) {
        context = c;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Product expandedListText = (Product) getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.shopping_list_row, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.productName);
        expandedListTextView.setText(expandedListText.getName());
        TextView expandedListTextView2 = (TextView) convertView
                .findViewById(R.id.productDescription);
        expandedListTextView2.setText(expandedListText.getDescription());
        TextView expandedListTextView3 = (TextView) convertView
                .findViewById(R.id.productAmount);

        expandedListTextView3.setText(String.valueOf(expandedListText.getAmount()));

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.slistrow);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedListText.toggleSelected();
                CheckBox c=(CheckBox)v.findViewById(R.id.checkBox);
                c.setChecked(expandedListText.isSelected());
            }
        });
        return convertView;
    }
}
