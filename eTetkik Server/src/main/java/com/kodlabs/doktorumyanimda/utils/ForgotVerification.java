package com.kodlabs.doktorumyanimda.utils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class ForgotVerification {
    private static final Map<String, List<String>> verifications = new HashMap<>();
    private static final Map<String, List<Timer>> verificationsTimer = new HashMap<>();
    public static void setVerification(String uname, String code){
        verifications.computeIfAbsent(uname, k -> new ArrayList<>()).add(code);
        startVerifyRemoveTimer(uname, code);
    }
    public static boolean codeIsVerify(String uname, String code){
        if(verifications.containsKey(uname)){
            List<String> codes = verifications.get(uname);
            if(codes.contains(code)){
                return codes.remove(code);
            }
        }
        return false;
    }
    private static void removeVerification(String uname, String code){
        if(verifications.containsKey(uname)){
            List<String> codes = verifications.get(uname);
            codes.remove(code);
        }
    }
    private static void startVerifyRemoveTimer(String uname, String code){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeVerification(uname, code);
                verificationsTimer.computeIfAbsent(uname, k -> new ArrayList<>()).remove(timer);
                timer.cancel();
                timer.purge();
            }
        }, TimeUnit.MINUTES.toMillis(3));
        verificationsTimer.computeIfAbsent(uname, k -> new ArrayList<>()).add(timer);
    }
}
