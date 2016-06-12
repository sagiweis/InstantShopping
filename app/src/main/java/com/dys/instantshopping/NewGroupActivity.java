package com.dys.instantshopping;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dys.instantshopping.adapters.FacebookFriendPickerAdapter;
import com.dys.instantshopping.models.FacebookFriendPickerModel;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.serverapi.GroupController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.dys.instantshopping.utilities.ImageParser;
import com.dys.instantshopping.utilities.ObjectIdTypeAdapter;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.GsonBuilder;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewGroupActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    ArrayList<FacebookFriendPickerModel> friends = new ArrayList<FacebookFriendPickerModel>();
    Group newGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        forceRTLIfSupported();
        populateFacebookFriendsList();
    }

    public void createGroup(View view){
        String name = ((EditText)findViewById(R.id.newGroupName)).getText().toString();
        Bitmap originalPicture = ((BitmapDrawable)((ImageView)findViewById(R.id.groupPictureButton)).getDrawable()).getBitmap();
        Bitmap picture = Bitmap.createScaledBitmap(originalPicture,400,400,false);
        String pictureBase64 = ImageParser.bitmapToBase64(picture);

        List<String> participants = new ArrayList<String>();
        participants.add(AccessToken.getCurrentAccessToken().getUserId());

        for (FacebookFriendPickerModel currFriend : friends) {
            if(currFriend.isSelected())
                participants.add(currFriend.getId());
        }

        if(name != ""){
            AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(this);
            Properties p = assetsPropertyReader.getProperties("InstantShoppingConfig.properties");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(p.getProperty("ServerApiUrl"))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().registerTypeAdapter(ObjectId.class, new ObjectIdTypeAdapter()).create()))
                    .build();
            GroupController groupApi = retrofit.create(GroupController.class);
            newGroup = new Group(name, pictureBase64, participants);
            Call<ResponseBody> call = groupApi.AddGroup(newGroup);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Intent myIntent = new Intent(NewGroupActivity.this, MyGroups.class);
                    startActivity(myIntent);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "ארעה שגיאה ביצירת הקבוצה", Toast.LENGTH_LONG).show();
                }
            });
    } else{
            Toast.makeText(this, "יש להזין שם קבוצה", Toast.LENGTH_SHORT).show();
        }
    }

    public void setGroupPicture(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "בחר תמונה לקבוצה"), PICK_IMAGE_REQUEST);
    }

    private void populateFacebookFriendsList(){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try{
                            for(int i=0; i<response.getJSONObject().getJSONArray("data").length(); i++){
                                final String friendId = response.getJSONObject().getJSONArray("data").getJSONObject(i).getString("id");
                                final String friendName = response.getJSONObject().getJSONArray("data").getJSONObject(i).getString("name");
                                Bundle params = new Bundle();
                                params.putBoolean("redirect", false);
                                params.putInt("height", 175);
                                params.putInt("width", 175);
                                new GraphRequest(
                                        AccessToken.getCurrentAccessToken(),
                                        "/"+friendId+"/picture",
                                        params,
                                        HttpMethod.GET,
                                        new GraphRequest.Callback() {
                                            public void onCompleted(GraphResponse photoResponse) {
                                                try{
                                                    String imageURL = photoResponse.getJSONObject().getJSONObject("data").getString("url");
                                                    FacebookFriendPickerModel model = new FacebookFriendPickerModel(friendId,friendName,imageURL);
                                                    friends.add(model);
                                                }catch (JSONException e){
                                                    Toast.makeText(getApplicationContext(), "ארעה שגיאה בקבלת רשימת החברים מפייסבוק", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                ).executeAsync();
                            }
                        } catch (JSONException e){
                            Toast.makeText(getApplicationContext(), "ארעה שגיאה בקבלת רשימת החברים מפייסבוק", Toast.LENGTH_SHORT).show();
                        }

                        Collections.sort(friends);
                        ListView listView = (ListView) findViewById(R.id.participantsListView);
                        listView.setAdapter(new FacebookFriendPickerAdapter(NewGroupActivity.this, R.layout.facebook_friend_picker_item, friends));
                        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    }
                }
        ).executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView imageView = (ImageView) findViewById(R.id.groupPictureButton);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
}
