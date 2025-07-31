package com.kodlabs.doktorumyanimda.service;

import com.kodlabs.doktorumyanimda.model.recaptcha.Recaptcha;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecaptchaService {
    private final String recaptchaUrl = "https://www.google.com/recaptcha/api/siteverify";
    private final String secretKey = "6LcjO2cgAAAAABtVWJaQZacx1XMxrd8yIR83c0ay";
    private static RecaptchaService instance;
    private RecaptchaService(){}
    public static RecaptchaService getInstance(){
        if(instance == null){
            instance = new RecaptchaService();
        }
        return instance;
    }
    public boolean captchaVerify(String token) throws IOException {
        URL url = new URL(recaptchaUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        String postParams = "secret=" + secretKey + "&response=" + token;
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();
        if(connection.getResponseCode() == 200){
            String response = Functions.streamToString(connection.getInputStream());
            if(!TextUtils.isEmpty(response)){
                Recaptcha recaptcha = Common.gson.fromJson(response.replace("error-codes", "error_codes"), Recaptcha.class);
                if(recaptcha.success){
                    return true;
                }else{
                    if(recaptcha.error_codes != null && recaptcha.error_codes.length > 0){
                        throw new IOException(recaptcha.error_codes[0]);
                    }
                }
            }
            return false;
        }else{
            String error = Functions.streamToString(connection.getErrorStream());
            if(!TextUtils.isEmpty(error)){
                System.out.println(error);
                throw new IOException(error);
            }
            return false;
        }
    }
}
