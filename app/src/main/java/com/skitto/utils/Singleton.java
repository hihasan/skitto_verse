package com.skitto.utils;

import android.content.Context;

import java.util.List;

public class Singleton {
    private Context context;
    private static Singleton ourInstance = new Singleton();

    private Integer navState;

    private Integer OrderId;

    public Context getContext() {
        if (context == null) {
            throw new RuntimeException("Context is null in Singleton singleton. Check if setContext() is called properly.");
        }
        return context;
    }

    private Singleton() {
    }

    public static Singleton getInstance() {
        return ourInstance;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    public static Singleton getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(Singleton ourInstance) {
        Singleton.ourInstance = ourInstance;
    }
}
