package com.dys.instantshopping.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dys.instantshopping.R;
import com.dys.instantshopping.models.ContactPickerModel;

import java.util.List;

/**
 * Created by Sagi on 24/04/2016.
 */
public class ContactPickerAdapter extends ArrayAdapter<ContactPickerModel> {

    private final List<ContactPickerModel> list;
    private final Activity context;

    public ContactPickerAdapter(Activity context,int resource, List<ContactPickerModel> list) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.contacts_picker_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.contactPickerCheckBox);
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            ContactPickerModel element = (ContactPickerModel) viewHolder.checkbox
                                    .getTag();
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

        holder.checkbox.setText(list.get(position).getName());
        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;
    }

}
