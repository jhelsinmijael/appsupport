package com.mijael.appsupport.utils.files;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

public interface ImagePresenter {

    void onImageLoading(boolean loading, String message);

    void onImageSuccess(File file);

    void onImageFail(String message);

    void doGetImageCamera();

    void doGetImageGallery();

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);


}
