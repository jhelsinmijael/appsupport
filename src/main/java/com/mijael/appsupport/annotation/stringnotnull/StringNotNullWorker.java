package com.mijael.appsupport.annotation.stringnotnull;

import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class StringNotNullWorker {

    private final static String LOG_TAG = StringNotNullWorker.class.getSimpleName();

    private static StringNotNullWorker instance;

    private Object target;

    public static StringNotNullWorker getInstance() {

        if (instance == null) {
            synchronized (StringNotNullWorker.class) {
                if (instance == null)
                    instance = new StringNotNullWorker();
            }
        }

        return instance;

    }

    public void deleteStringNull(Object target) {
        Log.i(LOG_TAG, "work");
        this.target = target;
        getFields(target);

    }

    public <T extends IStringNotNull> T getDeleteStringNull(T target) {
        Log.i(LOG_TAG, "work");
        this.target = target;
        getFields(target);
        return target;

    }

    private void getFields(Object target) {

        final String className = target.getClass().getName();

        Field[] fields = new Field[0];

        try {
            fields = Class.forName(className).getDeclaredFields();
        } catch (Exception e) {
            //fields = new Field[0];
            e.printStackTrace();
        }

        if (fields.length == 0) {
            return;
        }

        for (final Field field : fields) {

            filter(field);

        }

    }

    private void filter(@NonNull Field field) {

        Annotation[] annotations = field.getDeclaredAnnotations();

        field.setAccessible(true);

        for (Annotation annotation : annotations) {

            if (annotation != null) {

                if (annotation instanceof StringNotNull) {

                    try {

                        if (((String) field.get(target)) == null) {
                            field.set(target, ((StringNotNull) annotation).replaceIfNull());
                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }

            }

        }

    }

}
