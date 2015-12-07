package com.kangzj.shop.qr.weidiankd;

/**
 * Created by jasperkang on 2015/12/4.
 */
public final class Config {
    //接入信息
    public static final String APPKEY = "640671";
    public static final String SECRET = "--";
    public static final String BASE_URL = "http://api.vdian.com/api";

    public static final String TOKEN_QUERY = "?grant_type=client_credential&" + "appkey=" + Config.APPKEY + "&secret=" + Config.SECRET;

}
