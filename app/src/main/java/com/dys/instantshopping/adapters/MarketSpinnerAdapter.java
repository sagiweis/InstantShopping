package com.dys.instantshopping.adapters;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.objects.Market;

import java.util.List;

/**
 * Created by Dor Albagly on 5/9/2016.
 */
public class MarketSpinnerAdapter extends ArrayAdapter<Market> {

    List<Market> mlist;
    Context c;
    Location currentLocation;

    public MarketSpinnerAdapter(Context context, int textViewResourceId, List<Market> markets, Location curr) {
        super(context, textViewResourceId, markets);
        this.c = context;
        this.mlist = markets;
        this.currentLocation = curr;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Market getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*final Market expandedListText = (Market) getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.c
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.maket_list_item, null);
        }*/
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(this.c);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(mlist.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        final Market expandedListText = (Market) getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.c
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.maket_list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.marketName);
        expandedListTextView.setText(expandedListText.getName());
        TextView expandedListTextView2 = (TextView) convertView
                .findViewById(R.id.marketDistance);
        expandedListTextView2.setText((Float.toString(expandedListText.getDistance(currentLocation))));
        expandedListTextView2.setText("");

        return convertView;
    }
}
