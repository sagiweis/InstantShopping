package com.dys.instantshopping.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.GroupListAdapter;
import com.dys.instantshopping.objects.Product;

import java.util.ArrayList;

/**
 * Created by Sagi on 04/05/2016.
 */
public class AddEditProductFragment extends DialogFragment {
    View view;
    int index;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        index = -1;
        if(getArguments() != null)
            index = getArguments().getInt("itemIndex");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        view = getActivity().getLayoutInflater().inflate(R.layout.add_product_dialog, null);

        Spinner categorySpinner = (Spinner) view.findViewById(R.id.addProductCategorySpinner);
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("חלב וביצים");
        categories.add("פירות וירקות");
        categories.add("בשר / עוף ודגים");
        categories.add("לחם ומוצרי מאפה");
        categories.add("משקאות ויין");
        categories.add("קפואים");
        categories.add("סלטים ונקניקים");
        categories.add("בריאות ואורגני");
        categories.add("פארם ותינוקות");
        categories.add("ניקיון ובעלי חיים");
        categories.add("חשמל ואלקטרוניקה");
        categories.add("ריהוט לבית ולגן");
        categories.add("כלי בית וחד פעמי");
        categories.add("טקסטיל לבית והלבשה");
        categories.add("מחנאות פנאי ונסיעות");

        ArrayAdapter<String> categorySpinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        categorySpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categorySpinnerArrayAdapter);

        Spinner productSpinner = (Spinner) view.findViewById(R.id.addProductSpinner);
        ArrayAdapter<String> productSpinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>());
        productSpinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(productSpinnerArrayAdapter);

        if(index > -1){
            ListView listView = (ListView) getActivity().findViewById(R.id.groupListView);
            GroupListAdapter listAdapter = ((GroupListAdapter) listView.getAdapter());
            Product productToEdit = listAdapter.getShoppingList().getProductsList().get(index);

            categorySpinner.setSelection(0);
            productSpinner.setSelection(((ArrayAdapter)productSpinner.getAdapter()).getPosition(productToEdit.getName()));

            TextView description = (TextView) view.findViewById(R.id.addProductDescriptionText);
            description.setText(productToEdit.getDescription());

            TextView amount = (TextView) view.findViewById(R.id.addProductAmount);
            amount.setText(String.valueOf(productToEdit.getAmount()));
        }

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                Spinner productSpinner = (Spinner) view.findViewById(R.id.addProductSpinner);
                productSpinner.setClickable(true);

                ArrayList<String> products = new ArrayList<String>();
                if (position == 0) {
                    products.add("חלב");
                    products.add("שוקו");
                    products.add("גבינה לבנה");
                    products.add("קוטג");
                    products.add("יוגורט");
                    products.add("גבינה צהובה");
                    products.add("גבינה צפתית");
                    products.add("ביצים");
                } else if (position == 1) {
                    products.add("אבוקדו");
                    products.add("אשכולית");
                    products.add("בננה");
                    products.add("דובדבן");
                    products.add("פאפיה");
                    products.add("פומלית");
                    products.add("קלמנטינה");
                    products.add("תפוח עץ");
                    products.add("פטריות");
                    products.add("כוסברה");
                    products.add("מלפפון");
                    products.add("עגבנייה");
                    products.add("חסה");
                } else if (position == 2) {
                    products.add("פילה סלמון");
                    products.add("חזה עוף");
                    products.add("קבב בקר");
                    products.add("נקניק");
                    products.add("נקניקיות");
                    products.add("בשר הודו טחון");
                }

                ((ArrayAdapter)productSpinner.getAdapter()).addAll(products);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        builder.setView(view);
        if(index > -1)
            builder.setTitle("ערוך פריט");
        else
            builder.setTitle("הוסף פריט");
        builder.setPositiveButton("אישור",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Spinner spinner = ((Spinner) ((AlertDialog) dialog).findViewById(R.id.addProductSpinner));
                        String amount = (((EditText) ((AlertDialog) dialog).findViewById(R.id.addProductAmount))).getText().toString();
                        String description = (((EditText) ((AlertDialog) dialog).findViewById(R.id.addProductDescriptionText))).getText().toString();
                        String name = spinner.getSelectedItem().toString();

                        ListView listView = (ListView) getActivity().findViewById(R.id.groupListView);
                        GroupListAdapter listAdapter = ((GroupListAdapter) listView.getAdapter());

                        double amountAsDouble = 1.0;
                        if (amount.compareTo("") != 0)
                            amountAsDouble = Double.parseDouble(amount);

                        if(index > -1)
                            listAdapter.getShoppingList().editProduct(index,new Product(name, description, amountAsDouble));
                        else
                            listAdapter.getShoppingList().addProduct(new Product(name, description, amountAsDouble));
                        listAdapter.notifyDataSetChanged();
                    }
                }
        );
        builder.setNegativeButton("ביטול",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
        );
        return builder.create();
    }
}
