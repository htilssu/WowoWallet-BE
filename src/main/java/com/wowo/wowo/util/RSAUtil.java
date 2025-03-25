package com.wowo.wowo.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    public static RSAPublicKey getPublicKeyFromString() {
        try (HttpClient httpClient = HttpClient.newBuilder()
                .build()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://sso.htilssu.id.vn/v1/sso/certs?type=pem"))
                    .GET()
                    .build();
            final HttpResponse<String> httpResponse = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            final String pem = httpResponse.body();
            String publicKeyPEM = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\"", "")
                    .replaceAll("\\s+", "")
                    .replaceAll("\\\\n", "");
            byte[] encoded = Base64.getDecoder()
                    .decode(publicKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException |
                 URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
