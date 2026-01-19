package com.kodlabs.doktorumyanimda.config;

import com.iyzipay.Options;

public class IyzicoConfig {

    // Sandbox (Test) Anahtarları - Canlıya geçerken bunları gerçek anahtarlar ile
    // değiştirirsin
    private static final String API_KEY = "sandbox-8RANXWbRvcpIYpDgVh7RndhAC6mmo0R5";
    private static final String SECRET_KEY = "sandbox-HOdWrxtzYxXmpY7TBe4cmyjHnBOicZ3b";
    private static final String BASE_URL = "https://sandbox-api.iyzipay.com";

    public static final String INTERNAL_TOKEN = "kodlabs-secret-token-2026";
    public static final String SPRING_BOOT_BASE_URL = "http://localhost:9086";

    public static Options getOptions() {
        Options options = new Options();
        options.setApiKey(API_KEY);
        options.setSecretKey(SECRET_KEY);
        options.setBaseUrl(BASE_URL);
        return options;
    }
}