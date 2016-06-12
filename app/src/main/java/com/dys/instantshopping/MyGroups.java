package com.dys.instantshopping;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.dys.instantshopping.adapters.ImageLabelAdapter;
import com.dys.instantshopping.serverapi.GroupController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.dys.instantshopping.utilities.ObjectIdTypeAdapter;
import com.facebook.AccessToken;
import com.google.gson.GsonBuilder;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyGroups extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setContentView(R.layout.activity_my_groups);

        //setSupportActionBar(myToolbar);

        forceRTLIfSupported();
        setTitle("הקבוצות שלי");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        Intent myIntent = new Intent(MyGroups.this, NewGroupActivity.class);
                        startActivity(myIntent);
                }
        });

        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(this);
        Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(p.getProperty("ServerApiUrl"))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create()))
                .build();
        GroupController groupApi = retrofit.create(GroupController.class);
        Call<ArrayList<Group>> call = groupApi.GetMyGroups(AccessToken.getCurrentAccessToken().getUserId());
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                final ArrayList<Group> myGroups = response.body();
                GridView gridview = (GridView) findViewById(R.id.groupsGridView);
                gridview.setAdapter(new ImageLabelAdapter(getApplicationContext(),myGroups));

                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        AppCache.put("currentGroup",myGroups.get(position));
                        Intent myIntent = new Intent(MyGroups.this, GroupActivity.class);
                        startActivity(myIntent);
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Group>> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "ארעה שגיאה בקבלת רשימת הקבוצות", Toast.LENGTH_LONG).show();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}
