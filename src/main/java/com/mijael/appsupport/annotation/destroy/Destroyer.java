package com.mijael.appsupport.annotation.destroy;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Lifecycle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.mijael.appsupport.ui.activity.BaseActivity;
import com.mijael.appsupport.ui.fragment.BaseFragment;
import com.mijael.appsupport.ui.listener.MyLifecycleObserver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Destroyer {

    private static final String LOG_TAG = Destroyer.class.getSimpleName();
    private static ArrayList<Subscriber> subscribers = new ArrayList<>();
    private static HashMap<String, CountDownTimer> countDownTimerHashMaps = new HashMap<>();
    private static int clon_time = 0;


    public static void subscribe(BaseActivity activity) {
        Log.i(LOG_TAG, "subscribe");

        subscribers.add(new Subscriber(activity, String.valueOf(Calendar.getInstance().getTimeInMillis())));

        for (Subscriber s : subscribers) {

            getFields(s);

        }

    }


    public static void unsubscribe(BaseActivity activity) {
        Log.i(LOG_TAG, "unsubscribe");

        Subscriber s=null;

        for (Subscriber subscriber : subscribers) {

            if (subscriber.getActivity().equals(activity)) {

                s = subscriber;

                try {
                    countDownTimerHashMaps.get(activity.getClass().getSimpleName()).cancel();
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    countDownTimerHashMaps.remove(activity.getClass().getSimpleName());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }

        removeSubscriber(s);

    }


    public static void subscribe(BaseFragment fragment) {
        Log.i(LOG_TAG, "subscribe");

        subscribers.add(new Subscriber(fragment, String.valueOf(Calendar.getInstance().getTimeInMillis())));

        for (Subscriber s : subscribers) {

            getFields(s);

        }

    }


    public static void unsubscribe(BaseFragment fragment) {
        Log.i(LOG_TAG, "unsubscribe");

        Subscriber s=null;

        for (Subscriber subscriber : subscribers) {

            if (subscriber.getFragment().equals(fragment)) {

                s = subscriber;

                try {
                    countDownTimerHashMaps.get(fragment.getClass().getSimpleName()).cancel();
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                    countDownTimerHashMaps.remove(fragment.getClass().getSimpleName());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }

        removeSubscriber(s);

    }


    private static void removeSubscriber(Subscriber s){

        if (s==null)
            return;

        if (s.getFragment()==null){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                subscribers.removeIf(subscriber -> {

                    if (subscriber.getActivity()==null)
                        return false;

                    return subscriber.getActivity().equals(s.getActivity());

                });

            }else {

                int indexToDelete=-1;
                for (int i = 0; i< subscribers.size(); i++){

                    if (subscribers.get(i).getActivity()==null)
                        continue;

                    if (subscribers.get(i).getActivity().equals(s.getActivity())){
                        indexToDelete = i;
                    }
                }
                if (indexToDelete!=-1){
                    subscribers.remove(indexToDelete);
                }
            }

        }else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                subscribers.removeIf(subscriber -> {

                    if (subscriber.getFragment()==null)
                        return false;

                    return subscriber.getFragment().equals(s.getFragment());

                });

            }else {

                int indexToDelete=-1;
                for (int i = 0; i< subscribers.size(); i++){

                    if (subscribers.get(i).getFragment()==null)
                        continue;

                    if (subscribers.get(i).getFragment().equals(s.getFragment())){
                        indexToDelete = i;
                    }
                }
                if (indexToDelete!=-1){
                    subscribers.remove(indexToDelete);
                }
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

        boolean hasFieldWithAnnotation = false;

        Field[] fields = new Field[0];

        try {
            fields = Class.forName(className).getFields();
        } catch (Exception e) {
            //fields = new Field[0];
            e.printStackTrace();
        }

        if (fields.length == 0)
            return;

        for (final Field field : fields) {

            if (subscriber.getFragment() != null) {

                if (filterWidgetsFragment(field, subscriber)) {
                    hasFieldWithAnnotation = true;
                }

                continue;
            }

            if (filterWidgetsActivity(field, subscriber)) {
                hasFieldWithAnnotation = true;
            }

        }

        if (hasFieldWithAnnotation) {
            Log.i(LOG_TAG, "Field found");
        } else {
            if (subscriber.getFragment()==null){
                Log.i(LOG_TAG, subscriber.getActivity().getClass().getSimpleName());
            }else {
                Log.i(LOG_TAG, subscriber.getFragment().getClass().getSimpleName());
            }
            Log.i(LOG_TAG, "Field not found. Your dialog has modifier public? \n@Destroy \npublic AlertDialog alert;");
            throw new RuntimeException("Field not found. Your dialog has modifier public? \n@Destroy \npublic AlertDialog alert;");
        }

    }


    private static boolean filterWidgetsActivity(@NonNull Field field, final Subscriber s) {

        final Destroy destroy = field.getAnnotation(Destroy.class);

        if (destroy != null) {

            final int times = destroy.time();
            final Type type = destroy.type();

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

                    switch (type) {

                        case ALERT_DIALOG:

                            try {
                                ((AlertDialog) field.get(s.getActivity())).dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;

                        case PROGRESS_DIALOG:

                            try {
                                ((ProgressDialog) field.get(s.getActivity())).dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;

                        case SWIPE_REFRESH:

                            try {
                                ((SwipeRefreshLayout) field.get(s.getActivity())).setRefreshing(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;

                    }

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

                        Log.i(LOG_TAG, "handler");

                        if (subscribers.size() == 0)
                            return;

                        switch (type) {

                            case ALERT_DIALOG:

                                try {
                                    ((AlertDialog) field.get(s.getActivity())).dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;

                            case PROGRESS_DIALOG:

                                try {
                                    ((ProgressDialog) field.get(s.getActivity())).dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                break;

                            case SWIPE_REFRESH:

                                try {
                                    ((SwipeRefreshLayout) field.get(s.getActivity())).setRefreshing(false);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;

                        }

                        for (Subscriber item : subscribers) {

                            if (item.getToken() == null)
                                return;

                            if (item.getToken().equals(s.getToken())) {

                                if (s.getActivity() != null) {

                                    if (s.getActivity().getLifecycle().getCurrentState().equals(Lifecycle.State.RESUMED)) {

                                        Toast.makeText(s.getActivity(), "No hay respuesta", Toast.LENGTH_SHORT).show();

                                    }

                                }

                            }
                        }

                        removeSubscriber(s);

                    }
                }.start();
                countDownTimerHashMaps.put(s.getActivity().getClass().getSimpleName(), countDownTimer);

            });

            return true;

        } else {
            return false;
        }

    }


    private static boolean filterWidgetsFragment(@NonNull Field field, final Subscriber s) {

        final Destroy destroy = field.getAnnotation(Destroy.class);

        if (destroy != null) {

            final int times = destroy.time();
            final Type type = destroy.type();

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

                    switch (type) {

                        case ALERT_DIALOG:

                            try {
                                ((AlertDialog) field.get(s.getFragment())).dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;

                        case PROGRESS_DIALOG:

                            try {
                                ((ProgressDialog) field.get(s.getFragment())).dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;

                        case SWIPE_REFRESH:

                            try {
                                ((SwipeRefreshLayout) field.get(s.getFragment())).setRefreshing(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            break;

                    }

                    removeSubscriber(s);

                }
            });

            if (s.getFragment().getActivity()!=null) {

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

                            Log.i(LOG_TAG, "handler");

                            if (subscribers.size() == 0)
                                return;

                            switch (type) {

                                case ALERT_DIALOG:

                                    try {
                                        ((AlertDialog) field.get(s.getFragment())).dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    break;

                                case PROGRESS_DIALOG:

                                    try {
                                        ((ProgressDialog) field.get(s.getFragment())).dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    break;

                                case SWIPE_REFRESH:

                                    try {
                                        ((SwipeRefreshLayout) field.get(s.getActivity())).setRefreshing(false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    break;

                            }

                            for (Subscriber item : subscribers) {

                                if (item.getToken() == null)
                                    return;

                                if (item.getToken().equals(s.getToken())) {

                                    if (s.getFragment() != null && s.getFragment().getActivity() != null) {

                                        if (s.getFragment().getLifecycle().getCurrentState().equals(Lifecycle.State.RESUMED)) {

                                            Toast.makeText(s.getFragment().getActivity(), "No hay respuesta", Toast.LENGTH_SHORT).show();

                                        }

                                    }

                                }
                            }

                            removeSubscriber(s);

                        }
                    }.start();
                    countDownTimerHashMaps.put(s.getFragment().getClass().getSimpleName(), countDownTimer);

                });

            }
            return true;

        } else {
            return false;
        }

    }


}
