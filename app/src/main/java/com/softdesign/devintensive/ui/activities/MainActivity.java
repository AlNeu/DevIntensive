package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindViews;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "MainActivity";

    private DataManager mDataManager;

    private ImageView mImageCallPhone;
    private ImageView mImageSendMail;
    private ImageView mImageGetGitLink;
    private ImageView mImageGetVKLink;

    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private int mCurrentEditMode = 0;
    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVK, mUserGit, mUserBio;
    private List<EditText> mUserInfoViews;

    private ImageView mUserLogoRound;

    private RelativeLayout mProfilePlace;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout.LayoutParams mAppBarParams = null;
    private AppBarLayout mAppBarLayout;
    private ImageView mProfileImage;

    private File mPhotoFile = null;
    private Uri mSelectedImage = null;


    //Основной метод - при создании инициализируем переменные, находим вью
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate()");


        //mUserLogoRound = (ImageView)findViewById(R.id.user_logo_img);
        //Bitmap logoBitmap = new RoundedAvatarDrawable(BitmapFactory.decodeResource(this.getResources(), R.drawable.logo60)).getBitmap();
        //mUserLogoRound.setImageBitmap(logoBitmap);

        mDataManager = DataManager.getInstance();

        mImageCallPhone = (ImageView) findViewById(R.id.call_phone_img);
        mImageCallPhone.setOnClickListener(this);
        mImageSendMail = (ImageView) findViewById(R.id.send_mail_img);
        mImageSendMail.setOnClickListener(this);
        mImageGetGitLink = (ImageView) findViewById(R.id.get_git_link_img);
        mImageGetGitLink.setOnClickListener(this);
        mImageGetVKLink = (ImageView) findViewById(R.id.get_vk_link_img);
        mImageGetVKLink.setOnClickListener(this);

       // @BindViews({ R.id.call_phone_img, R.id.send_mail_img, R.id.vk_et, R.id.get_git_link_img, R.id.get_vk_link_img }) List<ImageView> mImageAction;

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_layout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mNavigationDrawer = (DrawerLayout) findViewById(R.id.navigation_drawer);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mUserVK = (EditText) findViewById(R.id.vk_et);
        mUserGit = (EditText) findViewById(R.id.githab_et);
        mUserBio = (EditText) findViewById(R.id.bio_et);

        mProfilePlace = (RelativeLayout) findViewById(R.id.profile_placeholder);
        mProfilePlace.setOnClickListener(this);

        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);

        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        //mAppBarLayout.setOnClickListener(this);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mUserVK);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserBio);


        setupToolbar();
        setupDrawer();
        loadUserInfoValue();

        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.header_bg) //TODO сделать плейсходер и transform + crop
                .into(mProfileImage);

        //Восстановление данных на активити при пересоздании
        if (savedInstanceState == null) {
            //Активити запускаеся впервые
            //showSnackbar("Активити запускается впервые", mCoordinatorLayout);

        } else {
            //showSnackbar("Активити уже запускалось", mCoordinatorLayout);
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);

        }
    }

    //???
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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

    //Перехватываем нажатия типа онКлик на разные вью
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick()");

        switch (v.getId()) {

            case R.id.fab:
                //Здесь действие по кнопке с указанным id
                //showSnackbar("click fab", mCoordinatorLayout);
                if (mCurrentEditMode == 0) {
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else {
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;
            case R.id.profile_placeholder:
                //Здесь действие по кнопке с указанным id
                //showProgress();
                //runWithDelay();
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;

            //Блок вызова неявных интентов
            case R.id.call_phone_img:
                callPhone(mUserPhone.getText().toString());
                break;
            case R.id.send_mail_img:
                sendMail(mUserMail.getText().toString());
                break;
            case R.id.get_git_link_img:
                brouseLink(mUserGit.getText().toString());
                break;
            case R.id.get_vk_link_img:
                brouseLink(mUserVK.getText().toString());
                break;


            /*case R.id.app_bar :
                //Здесь действие по кнопке с указанным id
                showProgress();
                runWithDelay();
                break;*/

            //case R.id. :
            //Здесь действие по кнопке с указанным id
            //break;


        }

    }

    //СПомещаем в состояние в бандл
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState(outState)");
        //outState.putInt("key",1);
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);


    }


    //Запуск с задержкой, через объект хендлер
    private void runWithDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 3000);
    }

    //Подменяем стандартный тулбар екшен баром ???
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //Устанавливаем выезжающее навигационное меню
    private void setupDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
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
    //Меняем режим редактирования при нажатии на флоат актион баттон
    private void changeEditMode(int mode) {
        if (mode == 1) {
            mFab.setImageResource(R.drawable.ic_edit_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
                showProfilerPlaceholder();
                lockToolbar();
                mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.TRANSPARENT);
                mCollapsingToolbarLayout.setTitle(" ");

            }
            mUserPhone.requestFocus();
        } else {
            mFab.setImageResource(R.drawable.ic_check_black_24dp);
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                hideProfilerPlaceholder();
                saveUserInfoValue();
                unlockToolbar();
                mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
            }

        }

    }

    /**
     * Получение результата из другой активити
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
        }


    }

    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);

    }

    //Загружаем сохраненные в долговременном хранилище данные
    private void loadUserInfoValue() {
        List<String> userData = mDataManager.getPreferencesManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }

    }

    //Сохраням данные в долговременном хранилище
    private void saveUserInfoValue() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }

    //Перехватываем нажатие и задвигаем навигационное меню
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Загрузка фотографий из внутреннего хранилища, дергаем чужое активити ?
    private void loadPhotoFromGallery() {

        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");

        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.choose_file)), ConstantManager.REQUEST_GALLERY_PICTURE);


    }

    //Загрузка фотографий с камеры, дергаем чужое активити
    private void loadPhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            //File photoFile = null;
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mPhotoFile != null) {
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantManager.CAMERA_REQUEST_PERMITION_CODE);

            Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать требуемые разрешения", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openApplictionSettings();
                        }
                    }).show();


        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ConstantManager.CAMERA_REQUEST_PERMITION_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 05.07.2016 если разшение получено то обработать его

            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // TODO: 05.07.2016 если разшение получено то обработать его

            }

        }
    }

    //Скрыть Плейсхолдел
    private void hideProfilerPlaceholder() {
        mProfilePlace.setVisibility(View.GONE);
    }

    //Показать плейсхолдел
    private void showProfilerPlaceholder() {
        mProfilePlace.setVisibility(View.VISIBLE);
    }

    //Заблокировать тулбар для редактирования
    private void lockToolbar() {
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);

    }

    //Разблокировать тулбар для просмотра
    private void unlockToolbar() {
        //mAppBarLayout.setExpanded(false,true);
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectedItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_storage), getString(R.string.user_profile_dialog_cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.user_profile_dialog_title));
                builder.setItems(selectedItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichItem) {
                        switch (whichItem) {
                            case 0: //Загрузить из галлереи
                                //showToast("Загрузить из галлереи");
                                loadPhotoFromGallery();
                                break;
                            case 1: //Загрузить из камеры
                                //showToast("Загрузить из камеры");
                                loadPhotoFromCamera();
                                break;
                            case 2: //Отмена
                                //showSnackbar("Отмена", mCoordinatorLayout);
                                dialog.cancel();
                                break;

                        }
                    }
                });

                return builder.create();

            default:
                return null;

        }

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        String imageFileName = "photo_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        ContentValues value = new ContentValues();
        value.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        value.put(MediaStore.Images.Media.DATA, image.getAbsolutePath());

        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value);

        return image;

    }


    public void openApplictionSettings() {
        Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

        startActivityForResult(appSettingIntent, ConstantManager.PERMISSION_REQUESR_SETTING_CODE);
    }

    public void callPhone(String textUri) {
            try {
                if (textUri.equals("")){
                    showToast("Адрес пустой");
                    return;
                } else {
                    Uri phoneNumber = Uri.parse("tel:" + textUri);
                    Intent callPhoneIntent = new Intent(Intent.ACTION_CALL, phoneNumber);
                    //callPhoneIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, textUri);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        showSnackbar("Нет разрешения на звонки", mCoordinatorLayout);
                        return;
                    } else {
                        startActivity(callPhoneIntent);
                    }
                }
            } catch (Exception e) {
                showSnackbar(e.toString(), mCoordinatorLayout);
                }
    }

    public void sendMail(String textUri){

        /*intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mail@mail.com","mail2@mail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT,"subject");
        intent.putExtra(Intent.EXTRA_TEXT, "mail content");
        startActivity(Intent.createChooser(intent, "title of dialog"));*/

        try{
            if (textUri.equals("")){
                showToast("Адрес пустой");
                return;
            }else{
                Uri mailAddress = Uri.parse("mailto:" + textUri);
                //Intent sendMailIntent = new Intent(Intent.ACTION_SEND);
                Intent sendMailIntent = new Intent(Intent.ACTION_SENDTO);
                sendMailIntent.setType("text/plain");
                //sendMailIntent.setData(mailAddress);
                sendMailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{textUri});
                sendMailIntent.putExtra(Intent.EXTRA_SUBJECT, "test subject");
                sendMailIntent.putExtra(Intent.EXTRA_TEXT, "test text");
                startActivity(sendMailIntent);
            }
        }catch (Exception e) {
            showSnackbar(e.toString(), mCoordinatorLayout);
        }
    }

    public void brouseLink(String textUri){

        try {
            if (textUri.equals("")){
                showToast("Адрес пустой");
                showSnackbar("Адрес пустой", mCoordinatorLayout);
                return;
            } else {
                Uri address = Uri.parse(textUri);
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openLinkIntent);
            }
        } catch (Exception e){
            showSnackbar(e.toString(), mCoordinatorLayout);
        }

    }



}