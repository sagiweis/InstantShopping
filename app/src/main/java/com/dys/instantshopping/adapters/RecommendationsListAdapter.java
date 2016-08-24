package com.dys.instantshopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.Recommendation;
import com.dys.instantshopping.objects.ShoppingList;
import com.dys.instantshopping.utilities.AppCache;

import java.util.List;

/**
 * Created by Dor Albagly on 5/5/2016.
 */
public class RecommendationsListAdapter extends BaseAdapter{
    private Context context;
    private List<Recommendation> list;

    public RecommendationsListAdapter(Context c,  List<Recommendation> list) {
        context = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.recommendations_list_row, null);
        }

        TextView name = (TextView) convertView.findViewById(R.id.productName);
        TextView amount = (TextView) convertView.findViewById(R.id.productAmount);
        name.setText(this.list.get(position).getName());
        amount.setText(String.valueOf(this.list.get(position).getAmount()));
        return convertView;
    }
}
