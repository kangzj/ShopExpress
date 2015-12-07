package com.kangzj.shop.qr.weidiankd.wdapi;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kangzj.shop.qr.weidiankd.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by jasperkang on 2015/12/4.
 */
public class WeidianAPI {
    private static final String TAG = "WeidianAPI";
    private static String TOKEN;
    private static Lock lock = new ReentrantLock();


    private Map<String, String> action = new HashMap<String, String>();


    private static RequestQueue requestQueue;

    private static WeidianAPI api;

    private WeidianAPI(Activity activity) {
        action.put("ORDER_LIST", "vdian.order.list.get");
        requestQueue = Volley.newRequestQueue(activity.getBaseContext());
        refreshToken();
    }

    public synchronized static WeidianAPI getInstance(Activity activity) {
        if (api == null) {
            api = new WeidianAPI(activity);
        }
        return api;
    }

    public static void refreshToken(int status_code) {
        if (status_code != 10013 || status_code != 10007) {
            return;
        }
        refreshToken();
    }

    public static void refreshToken() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                ("https://api.vdian.com/token" + Config.TOKEN_QUERY, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse");
                        try {
                            int status_code = (Integer) ((JSONObject) response.get("status")).get("status_code");
                            if (status_code == 0) {
                                String token = (String) ((JSONObject) response.get("result")).get("access_token");
                                if (null != token && !"".equals(token)) {
                                    WeidianAPI.TOKEN = token;
                                }
                                Log.d(TAG, WeidianAPI.TOKEN);
                            } else {
                                Log.e(TAG, (String) ((JSONObject) response.get("status")).get("status_reason"));
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        } finally {
                            lock.unlock();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
        lock.lock();
        requestQueue.add(jsObjRequest);
    }

    public void getOrderList(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        try {
            lock.lock();
            try {
                Map<String, JSONObject> params = getCommonParams(action.get("ORDER_LIST"));
                params.get("param").put("order_type", "pend");
                params.get("param").put("page_num", "1");
                params.get("param").put("page_size", "200");
                callApi(params, listener, errorListener);
            } finally {
                lock.unlock();
            }
        } catch (Throwable t) {
            Log.e("getOrderList", t.getMessage());
        }
    }

    private void callApi(Map<String, JSONObject> params, Response.Listener listener, Response.ErrorListener errorListener) {
        Request<JSONObject> jsObjRequest = new JsonObjectRequest
                (Config.BASE_URL + mapToQueryString(params), null, listener, errorListener);
        requestQueue.add(jsObjRequest);
    }

    private Map<String, JSONObject> getCommonParams(String method) throws JSONException {
        Map<String, JSONObject> params = new HashMap<String, JSONObject>();
        JSONObject wDPublicParams = new JSONObject();
        wDPublicParams.put("method", method);
        wDPublicParams.put("format", "json");
        wDPublicParams.put("access_token", WeidianAPI.TOKEN);
        params.put("public", wDPublicParams);
        JSONObject wDParams = new JSONObject();
        params.put("param", wDParams);
        return params;
    }

    private String mapToQueryString(Map<String, JSONObject> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (Map.Entry<String, JSONObject> obj : map.entrySet()) {
            try {
                sb.append(obj.getKey() + "=" + URLEncoder.encode(obj.getValue().toString(), "utf-8"));
                sb.append("&");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return sb.toString();
    }

}
