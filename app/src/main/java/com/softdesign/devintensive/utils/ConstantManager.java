package com.softdesign.devintensive.utils;

/**
 * Created by a on 23.06.2016.
 * Интерфейс содержащий константы для использования в программе
 * Interface contain constants for using in application
 */
public interface ConstantManager {
    String TAG_PREFIX = "DEV ";

    String COLOR_MODE_KEY = "COLOR_MODE_KEY";

    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String PHONE_USER_KEY = "PHONE_USER_KEY";
    String EMAIL_USER_KEY = "EMAIL_USER_KEY";
    String VK_USER_KEY = "VK_USER_KEY";
    String GIT_USER_KEY = "GIT_USER_KEY";
    String BIO_USER_KEY = "BIO_USER_KEY";
    String PHOTO_USER_KEY = "PHOTO_USER_KEY";

    int LOAD_PROFILE_PHOTO = 1;

    int REQUEST_GALLERY_PICTURE= 98;
    int REQUEST_CAMERA_PICTURE = 99;


    int PERMISSION_REQUESR_SETTING_CODE = 101;
    int CAMERA_REQUEST_PERMITION_CODE = 102;
}
