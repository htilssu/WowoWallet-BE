package com.wowo.wowo.util;

import java.io.IOException;
import java.net.*;

public class RequestUtil {

    public static void sendRequest(String urlString, String method){
        URL url = null;
        try {
            url = new URI(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            connection.getResponseCode();
        } catch (URISyntaxException | IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
