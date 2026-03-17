package com.kodlabs.doktorumyanimda.utils;

import java.util.regex.Pattern;

public final class Patterns {
    public static final Pattern TC_NO = Pattern.compile("^\\d{11}$");
    public static final Pattern EMAIL = Pattern.compile("^[a-zA-Z\\d]+[a-zA-Z\\d_\\.%+-]*@[a-zA-Z\\d]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)?$");
    public static final Pattern USERNAME = Pattern.compile("^[A-Za-z\\d]{3,11}$");
    public static final Pattern PHONE = Pattern.compile("^\\+?[1-9]\\d{0,3}[1-9]\\d{9}$");
    public static final Pattern DOCTOR_CODE = Pattern.compile("^[A-Z]{2}[a-zA-Z]{2,3}\\d{4,7}$");
    public static final Pattern DOCTOR_CODE_V2 = Pattern.compile("^[A-Za-z]{2,3}\\d{10}$");
    public static final Pattern DEVICE_UUID = Pattern.compile("^[a-z\\d\\-]{36,45}$");

    public static final Pattern DATE = Pattern.compile("^\\d{1,2}/\\d{1,2}/[1-9]\\d{3}$");
    public static final Pattern DATETIME = Pattern.compile("^\\d{2}:\\d{2}:\\d{2} \\d{1,2}/\\d{1,2}/[1-9]\\d{3}$");

    public static Pattern xmlParsePattern(String xmlTag){
        Pattern pattern = Pattern.compile(String.format("(?<=<%s.*>)(.*)(?=</%s>)", xmlTag, xmlTag));
        return pattern;
    }
}
