package com.kodlabs.doktorumyanimda.pay.verify;

import com.kodlabs.doktorumyanimda.model.pay.google.GooglePaymentData;
import com.kodlabs.doktorumyanimda.model.pay.google.GooglePaymentVerifyResponse;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GooglePay implements IPaymentVerify{
    GooglePay(){}
    public boolean verify(int fee, String paymentData){
        if(TextUtils.isEmpty(paymentData)){
            return false;
        }
        GooglePaymentData data = Common.gson.fromJson(paymentData, GooglePaymentData.class);
        StringBuilder linkBuilder = new StringBuilder("https://androidpublisher.googleapis.com/androidpublisher/v3/applications/");
        linkBuilder.append(data.getPackageName())
                .append("/purchases/subscriptions/")
                .append(data.getSkuID())
                .append("/tokens/")
                .append(data.getToken());
        try {
            URL url = new URL(linkBuilder.toString());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder responseBuilder = new StringBuilder();
            while((line = reader.readLine()) != null){
                responseBuilder.append(line);
            }
            is.close();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                String response = responseBuilder.toString();
                if(!TextUtils.isEmpty(response)){
                    GooglePaymentVerifyResponse resData = Common.gson.fromJson(response, GooglePaymentVerifyResponse.class);
                    if(resData.getPurchaseState() == 0){
                        return true;
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
