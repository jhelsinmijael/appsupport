package com.mijael.appsupport.utils.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mijael.appsupport.R;
import com.mijael.appsupport.utils.Constants;

public class SimpleNotification {

    private static SimpleNotification instance;

    /**
     * use for notifications for android <= 7.1 api 25
     * */
    private static NotificationManagerCompat notificationManagerCompat;

    /**
     * use for notifications for android >= 8 api 26
     * */
    private static NotificationManager notificationManager;

    /**
     *
     * */

    private static  @RawRes int sound;

    private static  @DrawableRes int largeIconRes;

    private static  @DrawableRes int smallIcon;

    private Context context;

    private static String title;

    private static String text;

    private static NotificationChannel notificationChannel;

    private static int notificationId;

    private static PendingIntent pendingIntent;

    private static Intent intentForPendingIntent;

    private static Bitmap largeIcon;

    private static boolean autocancel;

    private static boolean vibrate;

    private static long[] vibrationPattern;

    private static boolean lights;



    public static SimpleNotification getInstance(final Context context){

        if (instance==null){
            synchronized (SimpleNotification.class){
                if (instance==null){
                    instance = new SimpleNotification();
                    instance.context = context;
                }
            }
        }

        return instance;

    }


    private SimpleNotification() {
    }


    private static void create(Context context){
        notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationOreo(){

        create(context);
        Uri uri = Uri.parse("android.resource://"+context.getPackageName()+"/" + sound);
        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        notificationChannel.setSound(uri, att);
        notificationChannel.enableVibration(vibrate);
        notificationChannel.enableLights(lights);
        notificationChannel.setVibrationPattern(vibrationPattern);

        if (intentForPendingIntent!=null)
            pendingIntent = PendingIntent.getActivity(context, 0, intentForPendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (largeIcon==null)
            largeIcon = BitmapFactory.decodeResource(context.getResources(), largeIconRes);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(smallIcon)
                .setLargeIcon(largeIcon)
                .setChannelId(notificationChannel.getId())
                .setAutoCancel(autocancel)
                .build();

        if (pendingIntent!=null)
            notification.contentIntent = pendingIntent;

        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(notificationId , notification);
        Constants.screenOn(context);

    }


    private void showNotificationCompat(){

        create(context);

        if (intentForPendingIntent!=null)
            pendingIntent = PendingIntent.getActivity(context, 0, intentForPendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (largeIcon==null)
            largeIcon = BitmapFactory.decodeResource(context.getResources(), largeIconRes);

        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(text)
                .setSmallIcon(smallIcon)
                .setLargeIcon(largeIcon)
                .setAutoCancel(autocancel)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_MAX)
                .setVibrate(vibrationPattern)
                .build();

        if (pendingIntent!=null)
            notification.contentIntent = pendingIntent;

        notificationManagerCompat.notify(notificationId, notification);
        Constants.screenOn(context);


    }


    public void showNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationOreo();
        }else {
            showNotificationCompat();
        }

    }


    public static void removeNotification(Context context, int id){

        create(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancel(id);
        }else {
            notificationManagerCompat.cancel(id);
        }

    }


    public Builder builder(){
        return new Builder();
    }


    public static class Builder{

        /**
         *
         * */

        private @RawRes int sound = R.raw.marimba_arpegio;

        private @DrawableRes int largeIconRes;

        private @DrawableRes int smallIcon;

        private String title;

        private String text;

        private NotificationChannel notificationChannel;

        private int notificationId = 100;

        private PendingIntent pendingIntent;

        private Intent intentForPendingIntent;

        private Bitmap largeIcon;

        private boolean autocancel = false;

        private boolean vibrate = false;

        private long[] vibrationPattern = new long[]{1000,1000};

        private boolean lights = false;


        public Builder setSound(@RawRes int sound) {
            this.sound = sound;
            return this;
        }

        public Builder setLargeIconRes(@DrawableRes int largeIconRes) {
            this.largeIconRes = largeIconRes;
            return this;
        }

        public Builder setSmallIcon(@DrawableRes int smallIcon) {
            this.smallIcon = smallIcon;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setNotificationChannel(NotificationChannel notificationChannel) {
            this.notificationChannel = notificationChannel;
            return this;
        }

        public Builder setNotificationId(int notificationId) {
            this.notificationId = notificationId;
            return this;
        }

        public Builder setPendingIntent(PendingIntent pendingIntent) {
            this.pendingIntent = pendingIntent;
            return this;
        }

        public Builder setLargeIcon(Bitmap largeIcon) {
            this.largeIcon = largeIcon;
            return this;
        }

        public Builder setAutocancel(boolean autocancel) {
            this.autocancel = autocancel;
            return this;
        }

        public Builder setIntentForPendingIntent(Intent intentForPendingIntent) {
            this.intentForPendingIntent = intentForPendingIntent;
            return this;
        }

        public Builder setVibrationPattern(long[] vibrationPattern) {
            this.vibrationPattern = vibrationPattern;
            return this;
        }

        public Builder setVibrate(boolean vibrate) {
            this.vibrate = vibrate;
            return this;
        }

        public Builder setLights(boolean lights) {
            this.lights = lights;
            return this;
        }


        public SimpleNotification build(){

            SimpleNotification.sound = sound;
            SimpleNotification.largeIconRes = largeIconRes;
            SimpleNotification.smallIcon = smallIcon;
            SimpleNotification.title = title;
            SimpleNotification.text = text;
            SimpleNotification.notificationChannel = notificationChannel;
            SimpleNotification.notificationId = notificationId;
            SimpleNotification.pendingIntent = pendingIntent;
            SimpleNotification.intentForPendingIntent = intentForPendingIntent;
            SimpleNotification.largeIcon = largeIcon;
            SimpleNotification.autocancel = autocancel;
            SimpleNotification.vibrate = vibrate;
            SimpleNotification.vibrationPattern = vibrationPattern;
            SimpleNotification.lights = lights;

            return instance;

        }

    }


}
