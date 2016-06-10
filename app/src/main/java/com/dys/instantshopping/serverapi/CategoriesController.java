package com.dys.instantshopping.serverapi;

import com.dys.instantshopping.objects.Category;
import com.dys.instantshopping.objects.Group;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sagi on 08/06/2016.
 */
public interface CategoriesController {
    @GET("categories/GetCategories")
    Call<List<Category>> GetCategories();
}
