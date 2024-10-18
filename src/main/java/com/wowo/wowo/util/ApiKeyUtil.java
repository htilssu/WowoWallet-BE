package com.wowo.wowo.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ApiKeyUtil {

    public static String generateApiKey() {
        return Hashing.sha256()
                .hashString(UUID.randomUUID().toString(), StandardCharsets.UTF_8)
                .toString();
    }
}
