package com.dys.instantshopping.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dys.instantshopping.MyGroups;
import com.dys.instantshopping.NewGroupActivity;
import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.FacebookFriendPickerAdapter;
import com.dys.instantshopping.adapters.GroupListAdapter;
import com.dys.instantshopping.models.FacebookFriendPickerModel;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.objects.Product;
import com.dys.instantshopping.objects.ShoppingList;
import com.dys.instantshopping.serverapi.GroupController;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.AssetsPropertyReader;
import com.dys.instantshopping.utilities.ImageParser;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sagi on 04/05/2016.
 */
public class GroupSettingsFragment extends Fragment {

    Group currentGroup;
    View fragmentView;
    ArrayList<FacebookFriendPickerModel> friends = new ArrayList<FacebookFriendPickerModel>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_group_settings, container, false);
        init();
        return fragmentView;
    }

    private void init(){
        currentGroup = (Group) AppCache.get("currentGroup");
        populateFields();
    }

    public void save(View view){

    }

    public void reset(View view){
        init();
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

                                                    if(currentGroup.getParticipents().contains(friendId))
                                                        model.setSelected(true);

                                                    friends.add(model);
                                                }catch (JSONException e){
                                                    Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת רשימת החברים מפייסבוק", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                ).executeAsync();
                            }
                        } catch (JSONException e){
                            Toast.makeText(getActivity().getApplicationContext(), "ארעה שגיאה בקבלת רשימת החברים מפייסבוק", Toast.LENGTH_SHORT).show();
                        }

                        Collections.sort(friends);

                        ListView listView = (ListView) fragmentView.findViewById(R.id.editParticipantsListView);
                        FacebookFriendPickerAdapter adapter = new FacebookFriendPickerAdapter(getActivity(), R.layout.facebook_friend_picker_item, friends);
                        listView.setAdapter(adapter);
                        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    }
                }
        ).executeAsync();
    }

    private void populateFields(){
        EditText nameEditText = ((EditText)fragmentView.findViewById(R.id.editGroupName));
        ImageView groupPicture = (ImageView)fragmentView.findViewById(R.id.editGroupPictureButton);

        nameEditText.setText(currentGroup.getName());
        groupPicture.setImageBitmap(ImageParser.base64ToBitmap(currentGroup.getImageURL()));

        populateFacebookFriendsList();
    }




}
