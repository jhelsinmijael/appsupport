package com.mijael.appsupport.utils.files;

import java.io.File;

public interface ImageListener {

    void onImageLoading(boolean loading, String message);

    void onImageSuccess(File file);

    void onImageFail(String message);

}
