package com.dys.instantshopping.serverapi;

import com.dys.instantshopping.objects.Market;
import com.dys.instantshopping.objects.ProductOrderReport;
import com.dys.instantshopping.objects.ShoppingList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Sagi on 08/06/2016.
 */
public interface SuperMarketsOrderController {
    @POST("SuperMarketsOrder/AddNewProductOrder")
    Call<ResponseBody> AddNewProductOrder(@Body ProductOrderReport report);

    @GET("SuperMarketsOrder/GetOrderedList")
    Call<ShoppingList> GetOrderedList(@Query("groupID") String groupID, @Query("marketID") String marketID);

}
