package com.kodlabs.doktorumyanimda.integrations;

import com.kodlabs.doktorumyanimda.model.integrations.SmsData;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Sms implements IIntegrations{
    public SmsData data;
    public Sms(SmsData data){
        this.data = data;
    }
    private final String subscribe = "03129115435";
    private final String password = "D7@FC79L";
    private final String title = "3129115435";
    @Override
    public boolean sendMessage() {
        if(!TextUtils.isEmpty(data.getPhone())){
            String phone = data.getPhone().trim();
            String dialCode = phone.substring(0, phone.length() - 10);
            if(dialCode.equals("+90")){
                return OTPSms();
            }else{
                return sms();
            }
        }
        return false;
    }
    private boolean OTPSms(){
        try {
            URL url = new URL("https://api.netgsm.com.tr/sms/send/otp");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            String body = String.format(
                    "<?xml version='1.0' encoding='iso-8859-9'?>"
                            .concat("<mainbody>")
                            .concat("<header>")
                            .concat("<usercode>%s</usercode>")
                            .concat("<password>%s</password>")
                            .concat("<msgheader>%s</msgheader>")
                            .concat("</header>")
                            .concat("<body>")
                            .concat("<msg>")
                            .concat("<![CDATA[%s]]>")
                            .concat("</msg>")
                            .concat("<no>%s</no>")
                            .concat("</body>")
                            .concat("</mainbody>"), subscribe, password, title, data.getMsg(), data.getPhone().substring(data.getPhone().length() - 10)
            );
            return sendSmsResult(connection, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean sms(){
        try {
            String phone = data.getPhone().replace("+","00");
            System.out.println(phone);
            URL url = new URL("https://api.netgsm.com.tr/sms/send/xml");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type","text/xml");
            String body = String.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    " <mainbody>\n" +
                    " <header>\n" +
                    " <company dil=\"TR\">Netgsm</company>\n" +
                    " <usercode>%s</usercode>\n" +
                    " <password>%s</password>\n" +
                    " <type>1:n</type>\n" +
                    " <msgheader>%s</msgheader>\n" +
                    " </header>\n" +
                    " <body>\n" +
                    " <msg>\n" +
                    "    <![CDATA[%s]]>\n" +
                    " </msg>\n" +
                    "    <no>%s</no>\n" +
                    " </body>\n" +
                    " </mainbody>", subscribe, password,  title, data.getMsg(), phone);
            return sendSmsResult(connection, body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private boolean sendSmsResult(HttpURLConnection connection, String body) throws IOException{
        final OutputStream os = connection.getOutputStream();
        final OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        osw.write(body);
        osw.flush();
        osw.close();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            responseBuilder.append(line);
        }
        is.close();
        os.close();
        final String response = responseBuilder.toString();
        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            if(!TextUtils.isEmpty(response)){
                System.out.println(response);
                String code = getResponseField(response, "code");
                if(!TextUtils.isEmpty(code)){
                    if(code.equals("0")){
                        String jobID = getResponseField(response, "jobID");
                        System.out.println(jobID);
                        return true;
                    }else{
                        String error = getResponseField(response, "error");
                        System.out.println(error);
                        return false;
                    }
                }else{
                    return true;
                }
            }
        }else{
            return false;
        }
        return false;
    }
    private String getResponseField(String response, String param){
        Pattern pattern = Pattern.compile(String.format("(?<=<%s>)(.*)(?=</%s>)", param, param));
        Matcher matcher = pattern.matcher(response);
        if(matcher.find()){
            return matcher.group(0);
        }
        return "";
    }
}
