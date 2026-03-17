package com.kodlabs.doktorumyanimda.dal.mysql;

import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class MysqlConnection{
    private static Connection instance = null;
    private Connect connect;
    private MysqlConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            getConnectInformation();
           instance = DriverManager.getConnection(connect.getUrl(), getProperties());
        } catch (SQLException | IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
    public static synchronized Connection getInstance(){
        if(instance == null){
            new MysqlConnection();
        }
        return instance;
    }
    private Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("user", connect.user);
        properties.setProperty("password", connect.password);
        properties.setProperty("useUnicode","true");
        properties.setProperty("characterEncoding","UTF-8");
        properties.setProperty("useSSL", "true");
        properties.setProperty("autoReconnect", "true");
        return properties;
    }

    private void getConnectInformation() throws IOException, NullPointerException {
        String context = "";
        if(Common.isLocal){
            context = Common.contentSource;
        }
        Scanner scanner = new Scanner(new File(context.concat("/apps/eTetkik/user.json")), "UTF-8");
        StringBuilder sb = new StringBuilder();
        while(scanner.hasNext()){
            sb.append(scanner.nextLine());
        }
        if(TextUtils.isEmpty(sb.toString())){
            throw new NullPointerException(ErrorMessages.operationFailed);
        }
        scanner.close();
        connect = Common.gson.fromJson(sb.toString(), Connect.class);
    }

    private static class Connect{
        public String host;
        public String user;
        public String password;
        public int port = 3306;
        public String database;

        public String getUrl(){
            return "jdbc:mysql://".concat(host).concat(":").concat(String.valueOf(port)).concat("/").concat(database);
        }
    }
}