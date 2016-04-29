package com.dys.instantshopping.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.utilities.ImageParser;

import java.util.ArrayList;

/**
 * Created by Sagi on 20/04/2016.
 */
public class ImageLabelAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Group> list;

    public ImageLabelAdapter(Context c, ArrayList<Group> l) {
        mContext = c;
        list = l;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView label;
        LinearLayout layout;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

            label = new TextView(mContext);

            layout = new LinearLayout(mContext);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
            layout.setPadding(25,25,25,25);
            layout.addView(imageView);
            layout.addView(label);
        } else {
            layout = (LinearLayout) convertView;
            imageView = (ImageView) layout.getChildAt(0);
            label = (TextView) layout.getChildAt(1);
        }

        imageView.setImageBitmap(ImageParser.base64ToBitmap(list.get(position).getImageURL()));

        label.setText(list.get(position).getName());



        return layout;
    }
}