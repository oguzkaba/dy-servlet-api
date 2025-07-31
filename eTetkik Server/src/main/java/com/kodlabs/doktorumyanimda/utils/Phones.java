package com.kodlabs.doktorumyanimda.utils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class Phones {
    private final static List<String> phoneTests = Arrays.asList(
            "+905111111111",
            "+905111111112",
            "+905301111111",
            "+905301111112",
            "+905301111113",
            "+905311111111",
            "+905311111112",
            //"+905316254556",
            "+905054841777"
    );

    public static final List<String> hospitalPhones = new ArrayList<>(Arrays.asList(
            "+905222222220",
            "+905222222221"
    ));

    public static boolean isContains(String phone, boolean isDoctor){
        if(TextUtils.isEmpty(phone)){
            return false;
        }
        if(isDoctor){
            return phoneTests.contains(phone) || hospitalPhones.contains(phone);
        }else{
            return phoneTests.contains(phone);
        }
    }

    private static final Map<String, List<String>> phoneVerifications= new HashMap<>();
    private static final Map<String, Timer> phoneVerificationTimes = new HashMap<>();
    public static void setPhoneVerifications(String phone, String code){
        if(phoneVerifications.containsKey(phone)){
            List<String> codes = phoneVerifications.computeIfAbsent(phone, k -> new ArrayList<>());
            codes.add(code);
        }else{
            List<String> codes = new ArrayList<>();
            codes.add(code);
            phoneVerifications.put(phone, codes);
        }
        phoneVerificationTimes.put(phone.concat("_").concat(code), createPhoneVerificationTimer(phone, code));
    }
    public static boolean phoneVerification(String phone, String code){
        if(phoneVerifications.containsKey(phone)){
            List<String> codes = phoneVerifications.get(phone);
            boolean result = codes.contains(code);
            if(result && codes.remove(code)){
                System.out.printf("Phone: %s Verification success\n", phone);
            }
            return result;
        }
        return false;
    }

    public static void removePhoneVerification(String phone, String code){
        if(phoneVerifications.containsKey(phone)){
            List<String> codes = phoneVerifications.get(phone);
            if(codes.contains(code) && codes.remove(code)){
                Timer codeTimer = phoneVerificationTimes.remove(phone.concat("_").concat(code));
                codeTimer.purge();
                codeTimer.cancel();
                System.out.printf("Phone: %s Verification code removed\n", phone);
            }
        }
    }

    public static Timer createPhoneVerificationTimer(String phone, String code){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removePhoneVerification(phone, code);
            }
        }, TimeUnit.MINUTES.toMillis(3));
        return timer;
    }
}
