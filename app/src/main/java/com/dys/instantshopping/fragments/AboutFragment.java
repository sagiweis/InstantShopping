package com.dys.instantshopping.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dys.instantshopping.R;
import com.dys.instantshopping.adapters.FacebookFriendPickerAdapter;
import com.dys.instantshopping.models.FacebookFriendPickerModel;
import com.dys.instantshopping.objects.Group;
import com.dys.instantshopping.utilities.AppCache;
import com.dys.instantshopping.utilities.ImageParser;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sagi on 04/05/2016.
 */
public class AboutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_about, container, false);
        return fragmentView;
    }
}
