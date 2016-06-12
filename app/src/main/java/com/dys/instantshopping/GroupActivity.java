package com.dys.instantshopping;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dys.instantshopping.fragments.AboutFragment;
import com.dys.instantshopping.fragments.ChooseMarketDialogFragment;
import com.dys.instantshopping.fragments.AddEditProductFragment;
import com.dys.instantshopping.fragments.ChooseMarketDialogFragment;
import com.dys.instantshopping.fragments.GroupListFragment;
import com.dys.instantshopping.fragments.GroupSettingsFragment;
import com.dys.instantshopping.fragments.ListsHistoryFragment;
import com.dys.instantshopping.fragments.RecommendationsFragment;
import com.dys.instantshopping.fragments.ShoppingListFragment;
import com.dys.instantshopping.fragments.ListsHistoryFragment;
import com.dys.instantshopping.fragments.ShoppingListFragment;
import com.dys.instantshopping.objects.HistoryShoppingList;
import com.dys.instantshopping.objects.Market;
import com.dys.instantshopping.objects.ShoppingList;
import com.dys.instantshopping.serverapi.GroupController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.dys.instantshopping.utilities.ImageDownloader;
import com.dys.instantshopping.utilities.ImageParser;
import com.dys.instantshopping.utilities.ObjectIdTypeAdapter;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.GsonBuilder;

import org.bson.types.ObjectId;
import org.json.JSONException;

import java.io.IOException;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Group currentGroup;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        forceRTLIfSupported();

        currentGroup = (Group)AppCache.get("currentGroup");
        setTitle(currentGroup.getName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            Fragment firstFragment = new GroupListFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void setFragment(Fragment fragment){
        //Fragment newFragment = new GroupListFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void prepareGroupInfo(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.navHeaderLayout);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable(new BitmapDrawable(getResources(), ImageParser.base64ToBitmap(currentGroup.getImageURL())));
        } else {
            layout.setBackground(new BitmapDrawable(getResources(), ImageParser.base64ToBitmap(currentGroup.getImageURL())));
        }

        TextView groupName = (TextView)findViewById(R.id.navHeaderGroupName);
        groupName.setText(currentGroup.getName());

        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        params.putInt("height", 200);
        params.putInt("width", 200);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+AccessToken.getCurrentAccessToken().getUserId()+"/picture",
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse photoResponse) {
                        try{
                            String imageURL = photoResponse.getJSONObject().getJSONObject("data").getString("url");
                            ImageView groupPicture = (ImageView)findViewById(R.id.navHeaderProfilePicture);
                            new ImageDownloader(groupPicture).execute(imageURL);
                        }catch (JSONException e){

                        }
                    }
                }
        ).executeAsync();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group, menu);
        prepareGroupInfo();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history){
            setFragment(new ListsHistoryFragment());
        }else if(id == R.id.nav_go_shop){
            new ChooseMarketDialogFragment().show(getSupportFragmentManager(), "בחר סופר");
        }else if(id == R.id.nav_group_list){
            setFragment(new GroupListFragment());
        }else if(id == R.id.nav_about){
            setFragment(new AboutFragment());
        }else if(id == R.id.nav__group_settings){
            setFragment(new GroupSettingsFragment());
        }else if(id == R.id.nav_recommendations){
            setFragment(new RecommendationsFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
    public void setGroupPicture(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "בחר תמונה לקבוצה"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView)findViewById(R.id.editGroupPictureButton);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Close the shopping list and move it to the history
     * @param view
     */
    public void finishPurchase(View view){
        final Group currentGroup = (Group) AppCache.get("currentGroup");
        HistoryShoppingList historyList = new HistoryShoppingList(currentGroup.getCurrentList());
        currentGroup.setCurrentList(new ShoppingList());
        currentGroup.getHistoryLists().add(historyList);
        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(this);
        Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(p.getProperty("ServerApiUrl"))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create()))
                .build();
        GroupController groupApi = retrofit.create(GroupController.class);
        Call<Void> call = groupApi.UpdateGroup(currentGroup);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                AppCache.put("currentGroup",currentGroup);
                setFragment(new GroupListFragment());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "ארעה שגיאה בסגירת הרשימה", Toast.LENGTH_LONG).show();
            }
        });
    }
}
