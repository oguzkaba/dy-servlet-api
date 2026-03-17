package com.kodlabs.doktorumyanimda.utils;

import com.google.gson.Gson;
import javax.servlet.ServletContext;

public final class Common {
    public static final Gson gson = new Gson();
    public static final boolean isLocal = false;
    public static final String contentSource = isLocal ? "D:" : "";
    public static final String appName = "Doktorum Yanımda";

    // Secretler artık static field değil, inject edilecek
    public static String oneSignalAppID;
    public static String oneSignalApiKey;
    public static String cryptKey;

    // Min-Max Values
    public static final int minorMaxValue = 100;
    public static final int minorMinValue = 50;
    public static final int majorMaxValue = 180;
    public static final int majorMinValue = 90;
    public static final int pulseMaxValue = 120;
    public static final int pulseMinValue = 50;

    // Average Values
    public static final int minorAverageValue = 85;
    public static final int majorAverageValue = 135;
    public static final int pulseAverageValue = 90;

    public static void init(ServletContext ctx) {
        oneSignalAppID = ctx.getInitParameter("onesignal.app.id");
        oneSignalApiKey = ctx.getInitParameter("onesignal.api.key");
        cryptKey = ctx.getInitParameter("crypt.key");
    }
}