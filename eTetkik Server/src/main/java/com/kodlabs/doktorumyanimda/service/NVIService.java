package com.kodlabs.doktorumyanimda.service;

import com.kodlabs.doktorumyanimda.model.nvi.NVIEntity;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.Patterns;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

public class NVIService {
    private  static NVIService instance = null;
    private final String requestBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
            "  <soap12:Body>\n" +
            "    <TCKimlikNoDogrula xmlns=\"http://tckimlik.nvi.gov.tr/WS\">\n" +
            "      <TCKimlikNo>%s</TCKimlikNo>\n" +
            "      <Ad>%s</Ad>\n" +
            "      <Soyad>%s</Soyad>\n" +
            "      <DogumYili>%s</DogumYili>\n" +
            "    </TCKimlikNoDogrula>\n" +
            "  </soap12:Body>\n" +
            "</soap12:Envelope>";

    private NVIService(){}
    public static NVIService getInstance(){
        if(instance == null){
            instance = new NVIService();
        }
        return instance;
    }
    public boolean verifyNvi(NVIEntity entity) throws IOException {
        String birdYear =  Functions.dateStrToYearStr(entity.getBirdDate());
        if(TextUtils.isEmpty(birdYear)){
            throw new IOException("Invalid bird date");
        }
        URL url = new URL("https://tckimlik.nvi.gov.tr/Service/KPSPublic.asmx?op=TCKimlikNoDogrula");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/soap+xml");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        String body = String.format(requestBody, entity.getTcNumber(), entity.getName(), entity.getSurname(), birdYear);
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        connection.setFixedLengthStreamingMode(bodyBytes.length);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(bodyBytes);
        outputStream.flush();
        if(connection.getResponseCode() == 200){
            String response = Functions.streamToString(connection.getInputStream());
            Matcher matcher = Patterns.xmlParsePattern("TCKimlikNoDogrulaResult").matcher(response);
            if(matcher.find()){
                String find = matcher.group();
                return Boolean.parseBoolean(find);
            }else{
                return false;
            }
        }else{
            String error = Functions.streamToString(connection.getErrorStream());
            Matcher msgMatcher = Patterns.xmlParsePattern("faultstring").matcher(error);
            if(msgMatcher.find()){
                System.out.println(msgMatcher.group());
            }else{
                System.out.println(error);
            }
            return false;
        }
    }
}
