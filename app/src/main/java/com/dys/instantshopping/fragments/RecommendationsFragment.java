package com.dys.instantshopping.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.RecommendationsListAdapter;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.Recommendation;
import com.dys.instantshopping.serverapi.GroupController;
import com.dys.instantshopping.serverapi.RecommendationsController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sagi on 04/05/2016.
 */
public class RecommendationsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_recommendations, container, false);

        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(getActivity());
        Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(p.getProperty("ServerApiUrl")).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create())).build();
        final RecommendationsController recommendationsApi = retrofit.create(RecommendationsController.class);
        final Group currentGroup = (Group) AppCache.get("currentGroup");

        Call<Map<String, Double>> call = recommendationsApi.GetRecommendations(currentGroup.getId().toString());
        call.enqueue(new Callback<Map<String, Double>>() {
            @Override
            public void onResponse(Call<Map<String, Double>> call, Response<Map<String, Double>> response) {
                Map<String, Double> recommendations = response.body();
                List<Recommendation> rec = new ArrayList<Recommendation>();
                for ( String key : recommendations.keySet() ) {
                    double value = recommendations.get(key);

                    for(Product currProduct:currentGroup.getCurrentList().getProductsList())
                    {
                        if(currProduct.getName().equals(key)){
                            value -= currProduct.getAmount();
                        }
                    }

                    if(value > 0)
                        rec.add(new Recommendation(key,value));
                }

                RecommendationsListAdapter adapter = new RecommendationsListAdapter(getActivity(),rec);
                ListView listView = (ListView)fragmentView.findViewById(R.id.recommendationsListView);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Map<String, Double>> call, Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת ההמלצות מהשרת", Toast.LENGTH_LONG).show();
            }
        });

        return fragmentView;
    }
}
