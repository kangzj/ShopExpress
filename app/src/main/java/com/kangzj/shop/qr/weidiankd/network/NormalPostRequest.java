package com.kangzj.shop.qr.weidiankd.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jasperkang on 2015/12/4.
 */
public class NormalPostRequest extends Request<String> {
    private Map<String, String> mMap;
    private Response.Listener<JSONObject> mListener;
    private Response.ErrorListener mErrorListener;

    public NormalPostRequest(String url, Map<String, JSONObject> map, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);
        mListener = listener;
        mMap = new HashMap<>();
        for (Map.Entry<String, JSONObject> obj : map.entrySet()) {
            mMap.put(obj.getKey(), obj.getValue().toString());
        }
    }

    //mMap是已经按照前面的方式,设置了参数的实例
    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return mMap;
    }

    //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));

        }
    }

    @Override
    protected void deliverResponse(String response) {
        try {
            mListener.onResponse(new JSONObject(response));
        } catch (Exception e) {
            mErrorListener.onErrorResponse(new VolleyError("Json Decode Error"));
        }

    }


}
