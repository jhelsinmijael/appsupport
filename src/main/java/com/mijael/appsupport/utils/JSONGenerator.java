package com.mijael.appsupport.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Pattern;

public class JSONGenerator {

    private Activity activity;

    private JSONObject jsonObject;

    private boolean hasError = false;

    private boolean requireInternet = false;





    private JSONGenerator(boolean withDefValues) {

        if (withDefValues){

            jsonObject = new JSONObject();

            try {
                jsonObject.put("appid", 10);
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                jsonObject.put("appid", "10");
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {

            jsonObject = new JSONObject();

        }
    }

    private JSONGenerator(boolean withDefValues, Activity activity) {

        this.activity = activity;

        if (withDefValues){

            jsonObject = new JSONObject();

            try {
                jsonObject.put("appid", "20");
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                //jsonObject.put("phoneLogin",new LoginPref(activity).getPhone());
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                //jsonObject.put("idConductor",new LoginPref(activity).getUid());
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {

            jsonObject = new JSONObject();

        }
    }

    public static JSONGenerator getNewInstance(boolean withDefValues){
        return new JSONGenerator(withDefValues);
    }

    public static JSONGenerator getNewInstance(boolean withDefValues, Activity activity){
        return new JSONGenerator(withDefValues, activity);
    }






    public JSONGenerator requireInternet(boolean require){
        requireInternet = require;
        return this;
    }

    public JSONGenerator put(String key, int value){

        try {
            jsonObject.put(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }

        return this;

    }

    public JSONGenerator put(String key, float value){

        try {
            jsonObject.put(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }

        return this;

    }

    public JSONGenerator put(String key, double value){

        try {
            jsonObject.put(key, value);
        }catch (Exception e){
            e.printStackTrace();
        }

        return this;

    }

    public JSONGenerator put(String key, Object value){

        if (value instanceof TextView){

            try {
                jsonObject.put(key, ((TextView)value).getText().toString());
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if (value instanceof List<?>){

            try {
                jsonObject.put(key, new JSONArray(
                        new Gson().toJson(value)
                ));
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {

            try {
                jsonObject.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return this;

    }

    public JSONGenerator put(String key, TextView textView, ValidateOptions validateOptions){

        if (hasError)
            return this;

        if (validateOptions.getMinLength()>0 && validateOptions.getMaxLength()>0) {
            if (validateOptions.getMinLength() > validateOptions.getMaxLength()) {
                throw new RuntimeException(validateOptions.getClass() + " has error: Min length can not be greater than max length \n" +
                        "validateOptions.getMinLength()=" + validateOptions.getMinLength() + ", validateOptions.getMaxLength()=" + validateOptions.getMaxLength());
            }
        }

        if (validateOptions.isRequired()) {

            if (TextUtils.isEmpty(textView.getText())) {
                textView.setError(validateOptions.getErrorMessage());
                textView.requestFocus();
                hasError = true;
                return this;
            }
        }

        if (validateOptions.getLength()>0){

            if (TextUtils.getTrimmedLength(textView.getText()) < validateOptions.getLength()){
                textView.setError("Se requieren "+ validateOptions.getLength()+" dígitos");
                textView.requestFocus();
                hasError = true;
                return this;
            }

        }

        if (validateOptions.isEmail()){

            if (!validateEmail(textView.getText().toString())){
                textView.setError("Formato de correo incorrecto");
                textView.requestFocus();
                hasError = true;
                return this;
            }

        }

        if (validateOptions.getMinLength()>0){

            if (TextUtils.getTrimmedLength(textView.getText()) < validateOptions.getMinLength()){
                textView.setError("Se requieren "+ validateOptions.getMinLength()+" dígitos como mínimo");
                textView.requestFocus();
                hasError = true;
                return this;
            }

        }

        if (validateOptions.getMaxLength()>0){

            if (TextUtils.getTrimmedLength(textView.getText()) > validateOptions.getMaxLength()){
                textView.setError("Se requieren "+ validateOptions.getMaxLength()+" dígitos como máximo");
                textView.requestFocus();
                hasError = true;
                return this;
            }

        }

        try {
            jsonObject.put(key, textView.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;

    }

    public void operate(OnOperateListener listener){

        if (requireInternet){
            if (!isInternetConnected(activity)) {
                Toast.makeText(activity, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
                listener.onOperateError();
                return;
            }
        }

        if (hasError){
            listener.onOperateError();
        }else {
            listener.onOperateSuccess(jsonObject);
        }
    }

    public JSONObject operate(){
        return jsonObject;
    }

    public void operateIfOnline(OnOperateListener listener){

        if (requireInternet){
            if (!isInternetConnected(activity)) {
                Toast.makeText(activity, "Sin conexion a internet", Toast.LENGTH_SHORT).show();
                listener.onOperateError();
                return;
            }
        }

        if (hasError){
            listener.onOperateError();
        }else {
            isInternetOnline(activity, online -> {
                if (online){
                    listener.onOperateSuccess(jsonObject);
                }else {
                    Toast.makeText(activity, "No hay conexión a la red", Toast.LENGTH_SHORT).show();
                    listener.onOperateError();
                }
            });
        }
    }






    private boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    /**
     * @return boolean, if retun true is internet conected
     * @param activity
     * */
    private boolean isInternetConnected(Context activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void isInternetOnline(Activity activity, OnInternetOnlineListener listener) {

        new Thread(() -> {

            try {
                Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

                int val = p.waitFor();
                boolean reachable = (val == 0);
                activity.runOnUiThread(() -> listener.onInternetOnline(reachable));
                return;
                //return reachable;

            } catch (Exception e) {
                e.printStackTrace();
            }
            activity.runOnUiThread(()->listener.onInternetOnline(false));

            //return false;

        }).start();

    }

    public interface OnInternetOnlineListener{

        void onInternetOnline(boolean online);

    }

    public static class ValidateOptions {

        private boolean required = true;
        private boolean email = false;
        private String errorMessage = "Campo requerido";
        private int length = 0;
        private int minLenght = 0;
        private int maxLength = 0;

        public static ValidateOptions getDefault(){
            return new ValidateOptions();
        }

        public boolean isRequired() {
            return required;
        }

        public ValidateOptions setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public ValidateOptions setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public int getLength() {
            return length;
        }

        public ValidateOptions setLength(int length) {
            this.length = length;
            return this;
        }

        public boolean isEmail() {
            return email;
        }

        public ValidateOptions setEmail(boolean email) {
            this.email = email;
            return this;
        }

        public int getMinLength() {
            return minLenght;
        }

        public ValidateOptions setMinLenght(int minLenght) {
            this.minLenght = minLenght;
            return this;
        }

        public int getMaxLength() {
            return maxLength;
        }

        public ValidateOptions setMaxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }
    }

    public interface OnOperateListener{
        void onOperateError();
        void onOperateSuccess(JSONObject jsonObject);
    }

}
