package com.kodlabs.doktorumyanimda.utils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class ContactVerification {
    private static final Map<String, List<String>> verifications = new HashMap<>();
    private static final Map<String, List<Timer>> verificationsTimer = new HashMap<>();
    public static void setVerification(String address, String code){
        verifications.computeIfAbsent(address, k -> new ArrayList<>()).add(code);
        startVerifyRemoveTimer(address, code);
    }
    public static boolean codeIsVerify(String address, String code){
        if(verifications.containsKey(address)){
            List<String> codes = verifications.get(address);
            if(codes.contains(code)){
                return codes.remove(code);
            }
        }
        return false;
    }
    private static void removeVerification(String address, String code){
        if(verifications.containsKey(address)){
            List<String> codes = verifications.get(address);
            codes.remove(code);
        }
    }
    private static void startVerifyRemoveTimer(String address, String code){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeVerification(address, code);
                verificationsTimer.computeIfAbsent(address, k -> new ArrayList<>()).remove(timer);
                timer.cancel();
                timer.purge();
            }
        }, TimeUnit.MINUTES.toMillis(3));
        verificationsTimer.computeIfAbsent(address, k -> new ArrayList<>()).add(timer);
    }
}
