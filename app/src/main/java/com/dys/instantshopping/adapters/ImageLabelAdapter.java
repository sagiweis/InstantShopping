package com.dys.instantshopping.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
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
import java.util.Random;

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

    public void updateGroups(ArrayList<Group> newList){
        list = newList;
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


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.my_groups_list_item, null, false);

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.groupListLayout);
        TextView name = (TextView) convertView.findViewById(R.id.groupName);
        ImageView image = (ImageView) convertView.findViewById(R.id.groupPicture);

        name.setText(list.get(position).getName());
        image.setImageBitmap(ImageParser.base64ToBitmap(list.get(position).getImageURL()));

        return convertView;
    }
}