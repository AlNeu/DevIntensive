package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.network.req.UserLoginReq;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensivApplication;
import com.softdesign.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener {


    private CoordinatorLayout mCoordinatorLayout;
    private EditText mLogin, mPassword;
    private Button mSignIn;
    private TextView mRememberPassword;

    private DataManager mDataManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);
        mLogin = (EditText)findViewById(R.id.text_login);
        mPassword = (EditText) findViewById(R.id.text_pass);
        mSignIn = (Button)findViewById(R.id.button_enter);
        mSignIn.setOnClickListener(this);
        mRememberPassword = (TextView)findViewById(R.id.text_forgot_pass);
        mRememberPassword.setOnClickListener(this);
        mDataManager = DataManager.getInstance();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_enter:
                //showSnackbar("Вход", mCoordinatorLayout);
                SignIn();

                break;
            case R.id.text_forgot_pass:
                rememberPassword();
                break;
        }

    }

    private void SignIn() {
        if (NetworkStatusChecker.isNetworkAviable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(new Date().toString(), new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString()));
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Неверный логин или пароль!", mCoordinatorLayout);
                    } else {
                        showSnackbar("Что то сломалось! Код " + (response.code()), mCoordinatorLayout);
                    }

                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    //TODO обработать ошибки вызова авторизации и данных из апи
                }
            });
        }else{
            showSnackbar("Сеть не доступна, попробуйте позже!", mCoordinatorLayout);
        }
    }

    private void rememberPassword(){
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);

    }

    private void loginSuccess(UserModelRes userModel){

        //showSnackbar(userModel.body().getData().getToken(), mCoordinatorLayout);
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserValues(userModel);
        saveUserContact(userModel);


        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);


    }

    private void saveUserContact(UserModelRes userModel) {
        SharedPreferences sharPref =   DevintensivApplication.getSharedPreferences();
        List<String> userContacts = new ArrayList<>();
        userContacts.add(sharPref.getString(ConstantManager.PHONE_USER_KEY, userModel.getData().getUser().getContacts().getPhone().toString()));
        userContacts.add(sharPref.getString(ConstantManager.EMAIL_USER_KEY, userModel.getData().getUser().getContacts().getEmail().toString()));
        userContacts.add(sharPref.getString(ConstantManager.VK_USER_KEY, userModel.getData().getUser().getContacts().getVk().toString()));
        userContacts.add(sharPref.getString(ConstantManager.GIT_USER_KEY, null));
        userContacts.add(sharPref.getString(ConstantManager.BIO_USER_KEY, null));

        mDataManager.getPreferencesManager().saveUserProfileData(userContacts);

    }

    private void saveUserValues(UserModelRes userModel){
        int[] userValues = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects()
        };

        mDataManager.getPreferencesManager().saveUserProfileValues(userValues);

    }

}
