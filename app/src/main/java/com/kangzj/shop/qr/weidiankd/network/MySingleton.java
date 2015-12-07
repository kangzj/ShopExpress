package com.kangzj.shop.qr.weidiankd.network;

import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by jasperkang on 2015/12/3.
 */
public class MySingleton {
    private static RequestQueue requestQueue;

    public static RequestQueue getInstance(Activity activity) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(activity);
        }
        return requestQueue;
    }
}
