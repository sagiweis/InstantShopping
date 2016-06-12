package com.dys.instantshopping.serverapi;

import com.dys.instantshopping.objects.Market;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sagi on 11/06/2016.
 */
public interface RecommendationsController {
    @GET("Recommendations/GetRecommendations")
    Call<Map<String, Double>> GetRecommendations(@Query("groupId") String groupId);
}

