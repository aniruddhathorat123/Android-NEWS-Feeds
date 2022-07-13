package com.aniruddha.news_feeds;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Volley singleton implementation
 */
public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;

    private VolleySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    //synchronized: only one thread can access this method at time.
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    /**
     * @return
     */
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}