package com.kodlabs.doktorumyanimda.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Functions {

    public static String getIpAddress() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
        return address.getHostAddress();
    }

    public static String getClientIpAddress(HttpServletRequest request) {
        Map<String, String> headers = requestHeaders(request);
        String ip = null;
        if (headers.containsKey("x-forwarded-for")) {
            ip = headers.get("x-forwarded-for");
        }
        if (TextUtils.isEmpty(ip)) {
            try {
                ip = getIpAddress();
            } catch (UnknownHostException e) {
            }
        }
        return ip;
    }

    public static String getBrowserOrDevice(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        String deviceHeader = request.getHeader("device-info");

        if (deviceHeader != null && !deviceHeader.isEmpty()) {
            return deviceHeader;
        } else if (userAgent != null && !userAgent.isEmpty()) {
            return parseUserAgent(userAgent);
        } else {
            return "Unknown";
        }
    }

    public static String parseUserAgent(String userAgent) {
        if (userAgent == null)
            return "Unknown";

        String os = "Unknown OS";
        String browser = "Unknown Browser";

        // OS information
        if (userAgent.contains("Windows NT 10.0"))
            os = "Windows 10";
        else if (userAgent.contains("Windows NT 6.3"))
            os = "Windows 8.1";
        else if (userAgent.contains("Windows NT 6.1"))
            os = "Windows 7";
        else if (userAgent.contains("Mac OS X"))
            os = "macOS";
        else if (userAgent.contains("Android"))
            os = "Android";
        else if (userAgent.contains("iPhone"))
            os = "iOS";

        // Browser information
        if (userAgent.contains("Edg/")) {
            browser = extractVersion(userAgent, "Edg/");
            browser = "Edge " + browser;
        } else if (userAgent.contains("Chrome/")) {
            browser = extractVersion(userAgent, "Chrome/");
            browser = "Chrome " + browser;
        } else if (userAgent.contains("Firefox/")) {
            browser = extractVersion(userAgent, "Firefox/");
            browser = "Firefox " + browser;
        } else if (userAgent.contains("Safari/") && userAgent.contains("Version/")) {
            browser = extractVersion(userAgent, "Version/");
            browser = "Safari " + browser;
        }

        return os + " - " + browser;
    }

    private static String extractVersion(String ua, String keyword) {
        int index = ua.indexOf(keyword);
        if (index == -1)
            return "Unknown";
        String version = ua.substring(index + keyword.length());
        int spaceIndex = version.indexOf(" ");
        if (spaceIndex != -1) {
            version = version.substring(0, spaceIndex);
        }
        return version;
    }

    private static Map<String, String> requestHeaders(HttpServletRequest request) {

        Map<String, String> result = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            result.put(key, value);
        }
        return result;
    }

    private static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static String generateCode() {
        return String.format("%04d", new Random().nextInt(9999));
    }

    public static int generateIntUUID() {
        Random random = new Random();
        String id;
        do {
            id = String.format("%08d", random.nextInt(99999999));
            id = String.valueOf(Integer.parseInt(id));
        } while (id.length() != 8);
        return Integer.parseInt(id);
    }

    public static boolean isCorrectID(String id) {

        return id.matches("^[A-ZİĞÜŞÖÇ]{2,4}[0-9]{4,6}$");
    }

    public static String mailConvertSecurityFormat(String mail) {
        if (TextUtils.isEmpty(mail)) {
            return null;
        }
        if (!Patterns.EMAIL.matcher(mail).matches()) {
            return null;
        }
        int length = mail.substring(0, mail.indexOf("@")).length();
        if (length == 1) {
            return mail;
        }
        int showLimit = 2;
        if (length == 2) {
            showLimit = 1;
        }
        StringBuilder newMail = new StringBuilder(mail.substring(0, showLimit));
        for (int i = showLimit; i < length; i++) {
            newMail.append('*');
        }
        newMail = new StringBuilder(newMail.toString().concat(mail.substring(mail.indexOf('@'))));
        return newMail.toString();
    }

    public static String phoneConvertSecurityFormat(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return null;
        }
        if (!Patterns.PHONE.matcher(phone).matches()) {
            return null;
        }
        int length = phone.length();
        int hideSize = length - 5 - 2;
        if (hideSize > 0) {
            String newPhone = phone.substring(0, 5);
            for (int i = 0; i < hideSize; i++) {
                newPhone = newPhone.concat("*");
            }
            newPhone = newPhone.concat(phone.substring(length - 2));
            return newPhone;
        } else {
            return phone;
        }
    }

    public static String getFileContent(String path) {
        StringBuilder sb = new StringBuilder();
        final File file = new File(path);
        try {
            Scanner read = new Scanner(file, "UTF-8");
            while (read.hasNext()) {
                sb.append(read.nextLine());
            }
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String streamToString(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        return response.toString();
    }

    public static boolean dateControl(String date) {
        if (TextUtils.isEmpty(date)) {
            return false;
        }
        if (Patterns.DATE.matcher(date).matches()) {
            String[] parseDate = date.split("/");
            int dateDay = Integer.parseInt(parseDate[0]);
            int dateMonth = Integer.parseInt(parseDate[1]);
            if (dateDay <= 31 && dateDay >= 1 && dateMonth >= 1 && dateMonth <= 12) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date d = dateFormat.parse(date);
                    if (d.getTime() <= Calendar.getInstance().getTime().getTime()) {
                        return true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static String dateStrToYearStr(String date) {
        if (TextUtils.isEmpty(date)) {
            return null;
        }
        if (date.matches("^[1-2]+[0-9]{3}$")) {
            return date;
        }
        String newDate = date;
        if (newDate.contains("/")) {
            newDate = newDate.replace("/", " ");
        }
        if (newDate.contains(".")) {
            newDate = newDate.replace(".", " ");
        }
        if (newDate.contains(" ")) {
            String[] dateParse = newDate.trim().split(" ");
            if (dateParse.length == 3 && dateParse[2].length() == 4) {
                return dateParse[2];
            }
        }
        return null;
    }

    public static String nameFormat(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        String newName = name.toLowerCase(Locale.forLanguageTag("tr")).trim();
        String result;
        if (newName.contains(" ")) {
            String[] nameParse = newName.split(" ");
            if (nameParse.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (String n : nameParse) {
                    if (n.contains(" ")) {
                        continue;
                    }
                    String firstLetter = String.valueOf(n.charAt(0)).toUpperCase(Locale.forLanguageTag("tr"));
                    sb.append(firstLetter.concat(n.substring(1)).concat(" "));
                }
                result = sb.toString().trim();
            } else {
                result = newName;
            }
        } else {
            String firstLetter = String.valueOf(newName.charAt(0)).toUpperCase(Locale.forLanguageTag("tr"));
            result = firstLetter.concat(newName.substring(1));
        }
        return result;
    }

    public static String surnameFormat(String surname) {
        if (TextUtils.isEmpty(surname)) {
            return "";
        }
        return surname.toUpperCase(Locale.forLanguageTag("tr")).trim();
    }

    public static String fullDoctorNameShortConvert(String fullName) {
        if (TextUtils.isEmpty(fullName)) {
            return "";
        }
        String result = fullName;
        if (fullName.contains("Dr.")) {
            int index = fullName.indexOf("Dr.") + 3;
            result = fullName.substring(0, index).replace(" ", "");
            String names = fullName.substring(index + 1);
            if (names.contains(" ")) {
                String[] namesParse = names.split(" ");
                if (namesParse.length > 0) {
                    for (String nameParse : namesParse) {
                        result = result.concat(nameParse.substring(0, 1).toUpperCase());
                    }
                }
            }
        }
        return result;
    }

    public static String fullPatientNameSortConvert(String fullName) {
        if (TextUtils.isEmpty(fullName)) {
            return "";
        }
        if (fullName.contains(" ")) {
            String sortName = "";
            String[] parseName = fullName.split(" ");
            for (String pn : parseName) {
                if (!TextUtils.isEmpty(pn.trim())) {
                    sortName = sortName.concat(pn.substring(0, 1).toUpperCase(Locale.forLanguageTag("tr")));
                }
            }
            return sortName;
        }
        return fullName.substring(0, 1).toUpperCase(Locale.forLanguageTag("tr"));
    }

    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        return format.format(calendar.getTime());
    }

    // ? Old generation SHA-1
    // public static String toSHA1(String data) {
    // try {
    // MessageDigest crypt = MessageDigest.getInstance("SHA-1");
    // crypt.reset();
    // crypt.update(data.getBytes(StandardCharsets.UTF_8));
    // return new BigInteger(1, crypt.digest()).toString(16);
    // } catch (Exception e) {
    // return null;
    // }
    // }

    // Generates a SHA-256 hash of the input data.
    public static String toSHA256(String data) {
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-256");
            crypt.reset();
            crypt.update(data.getBytes(StandardCharsets.UTF_8));
            return new BigInteger(1, crypt.digest()).toString(16);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName))
                    return cookie.getValue();
            }
        }
        return "";
    }

    public static Cookie addCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(15 * 24 * 60 * 60);
        cookie.setSecure(false);
        return cookie;
    }

    public static String nameFirstCharacterUpperCase(String data) {
        if (data == null || data.isEmpty())
            return data;
        StringBuilder result = new StringBuilder();
        if (data.contains(" ")) {
            String[] names = data.split(" ");
            for (String name : names) {
                name = name.toLowerCase(Locale.forLanguageTag("tr"));
                result.append(name.substring(0, 1).toUpperCase(Locale.forLanguageTag("tr"))).append(name.substring(1))
                        .append(" ");
            }
        } else {
            data = data.toLowerCase(Locale.forLanguageTag("tr"));
            result = new StringBuilder(
                    data.substring(0, 1).toUpperCase(Locale.forLanguageTag("tr")) + data.substring(1));
        }
        return result.toString();
    }

    public static String encodeBase64(byte[] data) {
        if (data == null || data.length == 0)
            return null;
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decodeBase64(String data) {
        if (TextUtils.isEmpty(data))
            return null;
        return Base64.getDecoder().decode(data.replaceAll("\\R", ""));
    }

    public static Byte[] toByteArrayObjects(byte[] byteSource) {
        Byte[] bytes = new Byte[byteSource.length];
        for (int i = 0; i < byteSource.length; i++) {
            bytes[i] = byteSource[i];
        }
        return bytes;
    }

    public static String timeStampToLocalDateTime(Timestamp timestamp, String utc) {
        if (timestamp == null) {
            return null;
        }
        if (TextUtils.isEmpty(utc)) {
            DateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");
            serverFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return serverFormat.format(timestamp);
        }
        DateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'Z'");
        convertFormat.setTimeZone(TimeZone.getTimeZone(utc));
        return convertFormat.format(timestamp);
    }
}