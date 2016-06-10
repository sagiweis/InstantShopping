package com.dys.instantshopping.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.models.FacebookFriendPickerModel;
import com.dys.instantshopping.utilities.ImageDownloader;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Sagi on 25/04/2016.
 */
public class FacebookFriendPickerAdapter extends ArrayAdapter<FacebookFriendPickerModel> {

    private final List<FacebookFriendPickerModel> list;
    private final Activity context;

    public FacebookFriendPickerAdapter(Activity context,int resource, List<FacebookFriendPickerModel> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected ImageView image;
        protected CheckBox checkbox;
    }

    @Override
public int getCount(){
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.facebook_friend_picker_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.facebookFriendPickerCheckBox);
            viewHolder.image = (ImageView) view.findViewById(R.id.facebookFriendPickerImageView);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    FacebookFriendPickerModel element = (FacebookFriendPickerModel) viewHolder.checkbox.getTag();
                    element.setSelected(buttonView.isChecked());
                }
            });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        //holder.image.setImageURI(android.net.Uri.parse(list.get(position).getImageURL()));
        new ImageDownloader(holder.image).execute(list.get(position).getImageURL());
        holder.checkbox.setText(list.get(position).getName());
        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;
    }



}
