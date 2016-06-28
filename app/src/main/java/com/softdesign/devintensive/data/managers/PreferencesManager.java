package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensivApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 27.06.2016.
 */
public class PreferencesManager {
    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {ConstantManager.PHONE_USER_KEY, ConstantManager.EMAIL_USER_KEY, ConstantManager.VK_USER_KEY, ConstantManager.GIT_USER_KEY, ConstantManager.BIO_USER_KEY};


    public PreferencesManager(){
        this.mSharedPreferences = DevintensivApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
           }
        editor.apply();
    }



    public List<String> loadUserProfileData(){

        List<String> userFields = new ArrayList<>();

        userFields.add(mSharedPreferences.getString(ConstantManager.PHONE_USER_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.EMAIL_USER_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.VK_USER_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.GIT_USER_KEY, null));
        userFields.add(mSharedPreferences.getString(ConstantManager.BIO_USER_KEY, null));

        return userFields;

    }
}
