package com.mijael.appsupport.annotation.onetime;

import android.app.Activity;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mijael.appsupport.ui.activity.BaseActivity;
import com.mijael.appsupport.ui.fragment.BaseFragment;
import com.mijael.appsupport.ui.listener.MyLifecycleObserver;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OneTimer {

    private static final String LOG_TAG = OneTimer.class.getSimpleName();
    private static ArrayList<Subscriber> subscribers = new ArrayList<>();
    private static HashMap<Integer, CountDownTimer> countDownTimerHashMaps = new HashMap<>();
    private static int clon_time = 0;


    public static void onDestroy(View... views) {

        for (Map.Entry<Integer, CountDownTimer> countDownTimerHashMap : countDownTimerHashMaps.entrySet()) {

            for (View view : views) {

                try {
                    countDownTimerHashMaps.get(view.getId()).cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        try {
            subscribers.clear();
            countDownTimerHashMaps.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * subscribe return boolean(isAleredySubscribed-> return true)
     */
    public static boolean subscribe(BaseActivity activity, View view) {

        boolean isAlredySubscribed = false;

        for (Subscriber subscriber : subscribers) {

            if (subscriber.getView().getId() == view.getId()) {
                isAlredySubscribed = true;
            }

        }

        if (isAlredySubscribed) {
            Log.i(LOG_TAG, "is alredy suscribe");
            return false;
        }

        view.setEnabled(false);

        subscribers.add(new Subscriber(activity, view));

        getFields(subscribers.get(subscribers.size() - 1));

        Log.i(LOG_TAG, "suscribe");
        return true;

    }

    /**
     * unsubscribe remove View wigder from list of widgets
     */
    public static void unsubscribe(BaseActivity activity, View view) {
        Log.i(LOG_TAG, "unsuscribe");


        view.setEnabled(true);

        Subscriber subscriberToRemove = null;

        for (Subscriber subscriber : subscribers) {

            if (subscriber.getView().getId() == view.getId()) {

                if (view.getId() == subscriber.getView().getId()) {
                    //subscribers.remove(subscriber);
                    subscriberToRemove = subscriber;
                }

                try {
                    countDownTimerHashMaps.get(view.getId()).cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    countDownTimerHashMaps.remove(view.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        if (subscriberToRemove != null)
            subscribers.remove(subscriberToRemove);


    }


    /**
     * subscribe return boolean(subscribe success-> return true)
     * if subscribe fail because alredy subscriber -> return false
     */
    public static boolean subscribe(BaseFragment fragment, View view) {

        boolean isAlredySubscribed = false;

        for (Subscriber subscriber : subscribers) {

            if (subscriber.getView().getId() == view.getId()) {
                isAlredySubscribed = true;
            }

        }

        if (isAlredySubscribed) {
            Log.i(LOG_TAG, "is alredy suscribe");
            return false;
        }

        view.setEnabled(false);

        subscribers.add(new Subscriber(fragment, view));

        getFields(subscribers.get(subscribers.size() - 1));

        Log.i(LOG_TAG, "suscribe");
        return true;

    }

    /**
     * unsubscribe remove View wigder from list of widgets
     */
    public static void unsubscribe(Fragment fragment, View view) {
        Log.i(LOG_TAG, "unsuscribe");


        view.setEnabled(true);

        Subscriber subscriberToRemove = null;

        for (Subscriber subscriber : subscribers) {

            if (subscriber.getView().getId() == view.getId()) {

                if (view.getId() == subscriber.getView().getId()) {
                    //subscribers.remove(subscriber);
                    subscriberToRemove = subscriber;
                }

                try {
                    countDownTimerHashMaps.get(view.getId()).cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    countDownTimerHashMaps.remove(view.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        if (subscriberToRemove != null)
            subscribers.remove(subscriberToRemove);

    }


    //delete all subscriber in fragment or activity
    private static void removeSubscriber(Subscriber s) {

        if (s == null)
            return;

        if (s.getFragment() == null) {

            List<Integer> indexToDelete = new ArrayList<>();
            for (int i = 0; i < subscribers.size(); i++) {

                if (subscribers.get(i).getActivity() == null)
                    continue;

                if (subscribers.get(i).getActivity().equals(s.getActivity())) {
                    indexToDelete.add(i);
                }

                try {
                    countDownTimerHashMaps.get(subscribers.get(i).getView().getId()).cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    countDownTimerHashMaps.remove(subscribers.get(i).getView().getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            //removing al view for activity suscriber
            for (int i : indexToDelete) {
                subscribers.remove(i);
            }


        } else {

            List<Integer> indexToDelete = new ArrayList<>();
            for (int i = 0; i < subscribers.size(); i++) {

                if (subscribers.get(i).getFragment() == null)
                    continue;

                if (subscribers.get(i).getFragment().equals(s.getFragment())) {
                    indexToDelete.add(i);
                }

                try {
                    countDownTimerHashMaps.get(subscribers.get(i).getView().getId()).cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    countDownTimerHashMaps.remove(subscribers.get(i).getView().getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //removing al view for activity suscriber
            for (int i : indexToDelete) {
                subscribers.remove(i);
            }


        }

        Log.i(LOG_TAG, subscribers.toString());
        Log.i(LOG_TAG, countDownTimerHashMaps.toString());

    }


    private static void getFields(Subscriber subscriber) {

        final String className;

        if (subscriber.getFragment() != null) {

            className = subscriber.getFragment().getClass().getName();

        } else {

            className = subscriber.getActivity().getClass().getName();

        }

        Field[] fields;

        try {
            fields = Class.forName(className).getFields();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fields = new Field[0];
        }

        boolean hasView = false;

        for (final Field field : fields) {

            if (subscriber.getFragment() != null) {
                try {
                    if (filterWidgetsFragment(field, subscriber)) {
                        hasView = true;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                continue;
            }

            try {
                if (filterWidgetsActivity(field, subscriber)) {
                    hasView = true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        if (hasView) {

        } else {

            if (subscriber.getFragment() != null) {

                throw new RuntimeException("fragment " + subscriber.getFragment().getClass() +
                        ", public view with anotation @OneTime not found \n" +
                        "view " + subscriber.getView().getId());

            } else if (subscriber.getActivity() != null) {

                throw new RuntimeException("activity " + subscriber.getActivity().getClass() +
                        ", public view with anotation @OneTime not found \n" +
                        "view " + subscriber.getView().getId());

            } else {

                throw new RuntimeException("public view with anotation @OneTime not found");

            }

        }

    }


    /**
     * @return boolen -> true if has found View with @OneTime annotation
     */
    private static boolean filterWidgetsActivity(@NonNull Field field, final Subscriber s)
            throws IllegalAccessException, IllegalArgumentException {

        final OneTime oneTime = field.getAnnotation(OneTime.class);

        if (oneTime != null) {

            final int times = oneTime.time();

            if (((View) field.get(s.getActivity())).getId() == s.getView().getId()) {

                s.getActivity().setLifecycleObserver(new MyLifecycleObserver() {
                    @Override
                    public void onCreate() {

                    }

                    @Override
                    public void onPause() {

                    }

                    @Override
                    public void onResume() {

                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                        removeSubscriber(s);

                    }
                });

                s.getActivity().runOnUiThread(() -> {
                    Log.i(LOG_TAG, "runOnUiThread");

                    clon_time = times;
                    CountDownTimer countDownTimer = new CountDownTimer(times, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Log.i(LOG_TAG, String.valueOf(clon_time -= 1000));
                        }

                        @Override
                        public void onFinish() {

                            s.getView().setEnabled(true);

                            int index = 0;
                            int positionToDelete = -1;
                            for (Subscriber subscriber : subscribers) {

                                if (subscriber.getView().getId() == s.getView().getId()) {
                                    //subscribers.remove(subscriber);
                                    positionToDelete = index;
                                }
                                index++;
                            }

                            if (positionToDelete != -1) {
                                subscribers.remove(positionToDelete);
                            }

                            Log.i(LOG_TAG, subscribers.toString());

                        }
                    }.start();

                    countDownTimerHashMaps.put(s.getView().getId(), countDownTimer);

                });

                return true;

            }

            return false;
        } else {
            return false;
        }

    }


    /**
     * @return boolen -> true if has found View with @OneTime annotation
     */
    private static boolean filterWidgetsFragment(@NonNull Field field, final Subscriber s)
            throws IllegalAccessException, IllegalArgumentException {

        final OneTime oneTime = field.getAnnotation(OneTime.class);

        if (oneTime != null) {

            final int times = oneTime.time();

            if (((View) field.get(s.getFragment())).getId() == s.getView().getId()) {

                s.getFragment().setLifecycleObserver(new MyLifecycleObserver() {
                    @Override
                    public void onCreate() {

                    }

                    @Override
                    public void onPause() {

                    }

                    @Override
                    public void onResume() {

                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                        removeSubscriber(s);

                    }
                });

                if (s.getFragment()!=null && s.getFragment().getActivity()!=null) {

                    s.getFragment().getActivity().runOnUiThread(() -> {
                        Log.i(LOG_TAG, "runOnUiThread");

                        clon_time = times;
                        CountDownTimer countDownTimer = new CountDownTimer(times, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.i(LOG_TAG, String.valueOf(clon_time -= 1000));
                            }

                            @Override
                            public void onFinish() {

                                s.getView().setEnabled(true);

                                int index = 0;
                                int positionToDelete = -1;
                                for (Subscriber subscriber : subscribers) {

                                    if (subscriber.getView().getId() == s.getView().getId()) {
                                        //subscribers.remove(subscriber);
                                        positionToDelete = index;
                                    }
                                    index++;
                                }

                                if (positionToDelete != -1) {
                                    subscribers.remove(positionToDelete);
                                }

                                Log.i(LOG_TAG, subscribers.toString());

                            }
                        }.start();

                        countDownTimerHashMaps.put(s.getView().getId(), countDownTimer);

                    });

                }

                return true;
            }

            return false;

        } else {
            return false;
        }

    }


}
