package com.dys.instantshopping.serverapi;

import com.dys.instantshopping.objects.Category;
import com.dys.instantshopping.objects.Market;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Sagi on 08/06/2016.
 */
public interface MarketsController {
    @GET("markets/GetMarkets")
    Call<List<Market>> GetMarkets();
}
