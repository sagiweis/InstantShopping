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
import com.dys.instantshopping.tasks.CreateGroupTask;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.ImageParser;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class NewGroupActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    ArrayList<FacebookFriendPickerModel> friends = new ArrayList<FacebookFriendPickerModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        forceRTLIfSupported();
        populateFacebookFriendsList();
    }

    private void sendGroupToServer(Group group){
        (new CreateGroupTask()).execute();
    }

    public void createGroup(View view){
        String name = ((EditText)findViewById(R.id.newGroupName)).getText().toString();
        Bitmap picture = ((BitmapDrawable)((ImageView)findViewById(R.id.groupPictureButton)).getDrawable()).getBitmap();
        String pictureBase64 = ImageParser.bitmapToBase64(picture);

        //String pictureBase64 = "";
        List<String> participants = new ArrayList<String>();
        for (FacebookFriendPickerModel currFriend : friends) {
            if(currFriend.isSelected())
                participants.add(currFriend.getId());
        }

        if(name != ""){
            Group newGroup = new Group(name, pictureBase64, participants);
            //sendGroupToServer(newGroup);
            ArrayList<Group> myGroups = (ArrayList<Group>)AppCache.get("myGroups");
            myGroups.add(newGroup);
            AppCache.put("myGroups", myGroups);

            Intent myIntent = new Intent(NewGroupActivity.this, MyGroups.class);
            startActivity(myIntent);
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

                                                }
                                            }
                                        }
                                ).executeAsync();
                            }
                        } catch (JSONException e){

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
