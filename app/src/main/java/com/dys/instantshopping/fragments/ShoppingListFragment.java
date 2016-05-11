package com.dys.instantshopping.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.ShoppingListAdapter;
import com.dys.instantshopping.objects.Market;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;

/**
 * Created by Dor Albagly on 5/5/2016.
 */
public class ShoppingListFragment extends Fragment implements ChooseMarketDialogFragment.ChooseMarketDialogListener {

    ListView listView;
    Market market;

    public void setMarket(Market market) {
        this.market = market;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final Activity context = getActivity();
        new ChooseMarketDialogFragment().show(((FragmentActivity)context).getSupportFragmentManager(), "בחר סופר");

        context.setTitle("רשימת הקניות שלי");
        View v = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ShoppingList lst3 = new ShoppingList();
        lst3.addProduct(new Product("apple", "fruits", "Just red apples", 12));
        lst3.addProduct(new Product("חלב", "מוצרי חלב", "3%", 2));
        lst3.addProduct(new Product("בטטה", "ירקות", "גדולות", 5));
        lst3.addProduct(new Product("סבון", "מוצרי טיפוח", "סבון Dove", 2));
        lst3.setGroupName("Retro");
        lst3.setListDate("2.5.2016");

        listView = (ListView) v.findViewById(R.id.slistView);
        listView.setAdapter(new ShoppingListAdapter(context, lst3));


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Product prd = (Product) parent.getItemAtPosition(position);
                Toast.makeText(context,
                        "Clicked on Row: " + prd.getName(),
                        Toast.LENGTH_LONG).show();
                prd.toggleSelected();
            }
        });*/

        return v;
    }

    @Override
    public void onFinishChooseMarketDialog(Market market) {
        this.market = market;
    }
}
