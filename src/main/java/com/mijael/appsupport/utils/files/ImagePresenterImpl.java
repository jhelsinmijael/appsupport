package com.mijael.appsupport.utils.files;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.mijael.appsupport.R;
import com.mijael.appsupport.utils.SimpleAlertDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ImagePresenterImpl implements ImagePresenter {

    private String ROOT_DIRECTORY ;
    private String ROOT_DIRECTORY_FOTOS;
    private Activity context;
    private ImageListener view;
    private final String LOG_TAG = ImagePresenterImpl.class.getSimpleName();
    public static final int REQUEST_PERMISSIONS = 2;
    public static final int REQUEST_CODE_SELECT_PHOTO = 1;
    public static final int REQUEST_CODE_IMAGE_CAPTURE_WITH_NATIVE_API = 2;
    private final String ERROR_IMG = "No se ha podido procesar la imagen";
    private final String LOADING_IMG = "Cargando imagen";
    private final String[] permissions = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public ImagePresenterImpl(Activity context, ImageListener view) {
        this.context = context;
        this.view = view;
        this.ROOT_DIRECTORY = Environment.getExternalStorageDirectory() + "/"+context.getResources().getString(R.string.app_name)+"/";
        this.ROOT_DIRECTORY_FOTOS = ROOT_DIRECTORY.concat("photos/");

        doCheckDirectory();

    }


    @Override
    public void onImageLoading(boolean loading, String message) {
        view.onImageLoading(loading, message);
    }


    @Override
    public void onImageSuccess(File file) {
        view.onImageSuccess(file);
    }


    @Override
    public void onImageFail(String message) {
        view.onImageFail(message);
    }


    @Override
    public void doGetImageCamera() {

        if (!hasPermission(Manifest.permission.CAMERA)){
            Log.i(LOG_TAG, "missing permission CAMERA");

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)) {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);

            }

            return;
        }
        if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.i(LOG_TAG, "missing permission WRITE_EXTERNAL_STORAGE");

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            }

            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {

            File photo = new File(context.getExternalFilesDir(null), "pic.jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName()+".provider", photo));
            context.startActivityForResult( takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE_WITH_NATIVE_API);
        }

    }


    @Override
    public void doGetImageGallery() {

        if (!hasPermission(Manifest.permission.CAMERA)){
            Log.i(LOG_TAG, "missing permission CAMERA");

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)) {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);

            }

            return;
        }
        if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Log.i(LOG_TAG, "missing permission WRITE_EXTERNAL_STORAGE");

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            }

            return;
        }
        if (!hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            Log.i(LOG_TAG, "missing permission READ_EXTERNAL_STORAGE");

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);

            } else {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            }
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            context.startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), REQUEST_CODE_SELECT_PHOTO);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE_WITH_NATIVE_API && resultCode == Activity.RESULT_OK) {

            onImageLoading(true, "cargango");

            uploadPhotoCapturedWhiteNativeApi();


        }else if (requestCode == REQUEST_CODE_SELECT_PHOTO && resultCode == Activity.RESULT_OK){

            if (data==null){
                onImageFail(ERROR_IMG);
                return;
            }

            Uri selectedImage = data.getData();

            if (selectedImage==null){
                onImageFail(ERROR_IMG);
                return;
            }

            InputStream is;
            try {
                is = context.getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                guardarFotoEnMemoriaNative(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(LOG_TAG, "onRequestPermissionsResult");

        for (String permission: permissions){

            if (hasPermission(permission)){

                if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    doCheckDirectory();

            }

            if (!hasPermission(permission)){

                SimpleAlertDialog.withPositiveButton(context, "Conceda los permisos", "abrir", (dialog, which) -> {

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);

                });

                break;
            }

        }

    }


    private void uploadPhotoCapturedWhiteNativeApi(){

        File file = new File(context.getExternalFilesDir(null), "pic.jpg");

        if (file.exists()){

            ExifInterface exif;

            try {
                exif = new ExifInterface(file.getPath());
            } catch (IOException e) {
                exif = null;
                e.printStackTrace();
            }

            if (exif!=null){

                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                int rotationInDegrees = exifToDegrees(rotation);

                Matrix matrix = new Matrix(); if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}

                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

                Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Calendar.getInstance().getTime());
                String imageFileName = "JPEG_" + timeStamp + "_".concat(".jpg");
                File storageDir = new File(ROOT_DIRECTORY_FOTOS);

                try (FileOutputStream out = new FileOutputStream(file)){
                    file = new File(storageDir, imageFileName);
                    adjustedBitmap.compress(Bitmap.CompressFormat.JPEG , 100, out);
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            onImageSuccess(file);

        }else {

            onImageFail(ERROR_IMG);

        }

        onImageLoading(false, "");

    }


    private void guardarFotoEnMemoriaNative(final Bitmap foto){

        Log.i("FOTO", "guardarFotoenMemoria");

        new AsyncTask<Object, File, File>() {
            //private ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //pDialog = new ProgressDialog(context);
                //pDialog.setTitle(R.string.app_name);
                //pDialog.setMessage(getResources().getString(R.string.guardando_foto));
                //pDialog.setIndeterminate(true);
                //pDialog.setCancelable(false);
                //pDialog.show();
                onImageLoading(true, LOADING_IMG);
            }

            @Override
            protected File doInBackground(Object... object) {

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_".concat(".jpg");
                File storageDir = new File(ROOT_DIRECTORY_FOTOS);
                File image = null;
                FileOutputStream out=null;

                try {
                    image = new File(storageDir, imageFileName);
                    out = new FileOutputStream(image);
                    foto.compress(Bitmap.CompressFormat.JPEG , 100, out);
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (out!=null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


                return image;
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                onImageLoading(false,"");

                if (file.exists()){

                    onImageSuccess(file);

                }else {

                    onImageFail(ERROR_IMG);

                }

                return;

            }

        }.execute();

    }


    private boolean hasPermission(String permission){

        if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {

            return true;

        }else {

            return false;

        }


    }


    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } return 0;
    }


    private void doCheckDirectory() {
        File directory = new File(ROOT_DIRECTORY);
        File directory2 = new File(ROOT_DIRECTORY_FOTOS);
        if (!directory.exists()) {
            if (directory.mkdir()) {
                Log.i(LOG_TAG, "creando directorio: ".concat(directory.getAbsolutePath()));
            }
        }
        if (!directory2.exists()) {
            if (directory2.mkdir()) {
                Log.i(LOG_TAG, "creando directorio: ".concat(directory.getAbsolutePath()));
            }
        }
    }


}
