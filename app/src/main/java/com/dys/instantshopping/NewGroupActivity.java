package com.dys.instantshopping;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.dys.instantshopping.adapters.FacebookFriendPickerAdapter;
import com.dys.instantshopping.models.FacebookFriendPickerModel;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class NewGroupActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    ArrayList<FacebookFriendPickerModel> friends = new ArrayList<FacebookFriendPickerModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        //populateContactsList();
        populateFacebookFriendsList();
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
}
