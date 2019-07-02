package com.mijael.appsupport.utils;

import android.content.Context;
import android.content.Intent;

public class ServiceUtils {

    public static void startService(Context context, Class<?> serviceClass){

        if (!Constants.isMyServiceRunning(context, serviceClass)){

            try {
                context.startService(new Intent(context, serviceClass));
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

}
