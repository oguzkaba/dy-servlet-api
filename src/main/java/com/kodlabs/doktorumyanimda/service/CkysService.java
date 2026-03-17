package com.kodlabs.doktorumyanimda.service;

import com.google.gson.Gson;
import com.kodlabs.doktorumyanimda.model.ckys.calisma.CkysCalismaBilgisiResponse;
import com.kodlabs.doktorumyanimda.model.ckys.tahsil.CkysTahilBilgisiResponse;
import com.kodlabs.doktorumyanimda.utils.Functions;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CkysService {
    private static CkysService instane;
    private final String[] tahsilElements = new String[]{"soap:Envelope", "soap:Body", "GetTahsilBilgisiResponse"};
    private final String[] workingElements = new String[]{"soap:Envelope", "soap:Body", "GetCalismaBilgisiResponse"};
    private CkysService(){}
    public static CkysService getInstance(){
        if(instane == null) {
            instane = new CkysService();
        }
        return instane;
    }
    public CkysTahilBilgisiResponse tahilBilgisiSorgula(String tcNo, String seriNo, String tescilNo) throws IOException {
        String requestBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                " xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <GetTahsilBilgisi xmlns=\"http://tempuri.org/\">\n" +
                "      <tckn>%s</tckn>\n" +
                "      <seriNo>%s</seriNo>\n" +
                "      <tescilNo>%s</tescilNo>\n" +
                "      <userName>10000038</userName>\n" +
                "      <password>Test2022.</password>\n" +
                "    </GetTahsilBilgisi>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        URL url = new URL("https://ckysweb.saglik.gov.tr/USHTest/Ckys.asmx");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setRequestProperty("Host", "ckysweb.saglik.gov.tr");
        connection.setRequestProperty("SOAPAction", "http://tempuri.org/GetTahsilBilgisi");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        String body = String.format(requestBody, tcNo, seriNo, tescilNo);
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        connection.setFixedLengthStreamingMode(bodyBytes.length);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(bodyBytes);
        outputStream.flush();
        if(connection.getResponseCode() == 200){
            String response = Functions.streamToString(connection.getInputStream());
            JSONObject object = XML.toJSONObject(response);
            for(String elementName: tahsilElements){
                object = object.getJSONObject(elementName);
                if(object == null){
                    return null;
                }
            }
            return new Gson().fromJson(object.toString(), CkysTahilBilgisiResponse.class);
        }
        return null;
    }

    public CkysCalismaBilgisiResponse calismaBilgisiSorgula(String tcNo, String seriNo, String tescilNo) throws IOException {
        String requestBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
                " xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <GetCalismaBilgisi xmlns=\"http://tempuri.org/\">\n" +
                "      <tckn>%s</tckn>\n" +
                "      <seriNo>%s</seriNo>\n" +
                "      <tescilNo>%s</tescilNo>\n" +
                "      <userName>10000038</userName>\n" +
                "      <password>Test2022.</password>\n" +
                "    </GetCalismaBilgisi>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        URL url = new URL("https://ckysweb.saglik.gov.tr/USHTest/Ckys.asmx");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setRequestProperty("Host", "ckysweb.saglik.gov.tr");
        connection.setRequestProperty("SOAPAction", "http://tempuri.org/GetCalismaBilgisi");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        String body = String.format(requestBody, tcNo, seriNo, tescilNo);
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);
        connection.setFixedLengthStreamingMode(bodyBytes.length);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(bodyBytes);
        outputStream.flush();
        if(connection.getResponseCode() == 200){
            String response = Functions.streamToString(connection.getInputStream());
            JSONObject object = XML.toJSONObject(response);
            for(String elementName: workingElements){
                object = object.getJSONObject(elementName);
                if(object == null){
                    return null;
                }
            }
            return new Gson().fromJson(object.toString(), CkysCalismaBilgisiResponse.class);
        }
        return null;
    }
}
