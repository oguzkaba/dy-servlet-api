package com.kodlabs.doktorumyanimda.utils;

import com.google.gson.Gson;

public final class Common {
    public static final Gson gson = new Gson();
    public static final boolean isLocal = false; // Set to false for production
    public static final String contentSource = isLocal ? "D:" : "";
    public static final String appName = "Doktorum Yanımda";
    public static final String oneSignalAppID = "cebc0888-b542-4f03-b63e-003c37ea48b1";
    public static final String oneSignalApiKey = "os_v2_app_z26arcfvijhqhnr6aa6dp2siwhewxrrolj5eu3m6cnkya5p6rz7ecflf62k2fn2rxok2vihsbeu4x7m74lfqbjqo5w6qxib7tpdqevq";

    public static final String cryptKey = "2a46e69e-d511-4fbb-9b9f-5fc20fffc735";

    /* Min-Max Values */
    public static final int minorMaxValue = 100;
    public static final int minorMinValue = 50;
    public static final int majorMaxValue = 180;

    public static final int majorMinValue = 90;
    public static final int pulseMaxValue = 120;
    public static final int pulseMinValue = 50;

    /* Average Value */
    public static final int minorAverageValue = 85;
    public static final int majorAverageValue = 135;
    public static final int pulseAverageValue = 90;
}
