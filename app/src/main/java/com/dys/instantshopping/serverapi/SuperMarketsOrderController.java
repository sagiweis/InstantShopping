package com.dys.instantshopping.serverapi;

import com.dys.instantshopping.objects.Market;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Sagi on 08/06/2016.
 */
public interface SuperMarketsOrderController {
    @GET("SuperMarketsOrder/AddNewProductOrder")
    Call<ResponseBody> AddNewProductOrder();
}
