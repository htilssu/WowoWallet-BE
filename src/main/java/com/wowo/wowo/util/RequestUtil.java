package com.wowo.wowo.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class RequestUtil {

    public static CompletableFuture<Void> sendRequest(String urlString, String method) {
        URL url;
        try {
            url = new URI(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            connection.getResponseCode();
        } catch (URISyntaxException | IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
