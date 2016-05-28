package com.dys.instantshopping.serverapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Sagi on 28/05/2016.
 */
public interface GroupController {
    @GET("api/group/addgroup")
    Call<Response> AddGroup(@Part("name") String name, @Part("imageUrl") String imageUrl, @Part("participents") List<String> participents);
}
