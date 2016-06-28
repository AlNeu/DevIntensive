package com.softdesign.devintensive.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.devintensive.R;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.RoundedAvatarDrawable;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    private DataManager mDataManager;

    private ImageView mImageCallPhone;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private int mCurrentEditMode = 0;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVK, mUserGit, mUserBio;
    private List<EditText> mUserInfoViews;

    private ImageView mUserLogoRound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");

        //mUserLogoRound = (ImageView)findViewById(R.id.user_logo_img);
        //Bitmap logoBitmap = new RoundedAvatarDrawable(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo60)).getBitmap();
        //mUserLogoRound.setImageBitmap(logoBitmap);

        mDataManager = DataManager.getInstance();

        mImageCallPhone = (ImageView)findViewById(R.id.call_phone_img);
        mImageCallPhone.setOnClickListener(this);

        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_layout);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        mNavigationDrawer = (DrawerLayout)findViewById(R.id.navigation_drawer);

        mFab = (FloatingActionButton)findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mUserPhone = (EditText)findViewById(R.id.phone_et);
        mUserMail = (EditText)findViewById(R.id.email_et);
        mUserVK = (EditText)findViewById(R.id.vk_et);
        mUserGit = (EditText)findViewById(R.id.githab_et);
        mUserBio = (EditText)findViewById(R.id.bio_et);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVK);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);


        setupToolbar();
        setupDrawer();
        loadUserInfoValue();

        //Восстановление данных на активити при пересоздании
        if (savedInstanceState == null){
            //Активити запускаеся впервые
            //showSnackbar("Активити запускается впервые", mCoordinatorLayout);

        }else{
            //showSnackbar("Активити уже запускалось", mCoordinatorLayout);
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY,0);
            changeEditMode(mCurrentEditMode);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        saveUserInfoValue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");

        switch (v.getId()){
            case R.id.call_phone_img :
            //Здесь действие по кнопке с указанным id
                showProgress();
                runWithDelay();
            break;
            case R.id.fab :
                //Здесь действие по кнопке с указанным id
                showSnackbar("click fab", mCoordinatorLayout);
                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                }else{
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;
            //case R.id. :
            //Здесь действие по кнопке с указанным id
            //break;


        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState(outState)");
        //outState.putInt("key",1);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);


    }


    private void runWithDelay(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 hideProgress();
            }
        }, 3000);
    }

    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer(){
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        //mUserLogoRound = (ImageView)findViewById(R.id.user_logo_img);
        //Bitmap logoBitmap = new RoundedAvatarDrawable(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo60)).getBitmap();
        //mUserLogoRound.setImageBitmap(logoBitmap);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar(item.getTitle().toString(), mCoordinatorLayout);
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }

    /**
     * доп метод для переключения режимов редактирования данных
     */

    private void changeEditMode(int mode){
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_edit_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
        }else{
            mFab.setImageResource(R.drawable.ic_check_black_24dp);
            for (EditText userValue: mUserInfoViews){
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                saveUserInfoValue();
            }

        }

    }

    private void loadUserInfoValue(){
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }

    }

    private void saveUserInfoValue(){
        List<String> userData = new ArrayList<>();
        for (EditText  userFieldView: mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}