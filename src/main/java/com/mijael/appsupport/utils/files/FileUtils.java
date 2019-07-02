package com.mijael.appsupport.utils.files;

import android.util.Base64;

import com.google.android.gms.common.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {

    public static String getBase64(File file){

        byte[] bytes;

        try {
            bytes = IOUtils.toByteArray(new FileInputStream(file));
        } catch (IOException e) {
            bytes = new byte[4];
            e.printStackTrace();
        }

        String str = Base64.encodeToString(bytes, Base64.DEFAULT);

        return str;

    }

}
