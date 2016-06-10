package com.dys.instantshopping.serverapi;

import com.dys.instantshopping.objects.Group;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sagi on 28/05/2016.
 */
public interface GroupController {
    @POST("group/addgroup")
    Call<ResponseBody> AddGroup(@Body Group group);

    @GET("group/getmygroups")
    Call<ArrayList<Group>> GetMyGroups(@Query("userId") String userId);

    @GET("group/MoveListToHistory")
    Call<Group> MoveListToHistory(@Query("objectId") String objectId);
}
