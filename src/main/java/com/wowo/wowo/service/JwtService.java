package com.wowo.wowo.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.model.User;
import com.wowo.wowo.util.ObjectUtil;
import com.wowo.wowo.util.RSAUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@ConfigurationProperties(prefix = "jwt")
public class JwtService {

    private static final int expire = 60 * 24;
    private static final Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKeyFromString());
    private static final JWTVerifier verifier = JWT.require(algorithm)
            .build();
    private static String secret;

    public static String generateToken(User user) {
        //convert user to json
        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withPayload(ObjectUtil.parseJson(user))
                .withExpiresAt(Date.from(Instant.now()
                        .plus(expire, ChronoUnit.MINUTES)))
                .sign(algorithm);

    }

    public static String generateToken(String subject, String payload, int expire) {
        return JWT.create()
                .withSubject(subject)
                .withPayload(payload)
                .withExpiresAt(Date.from(Instant.now()
                        .plus(expire, ChronoUnit.MINUTES)))
                .sign(algorithm);

    }

    /**
     * Tạo token với người dùng được truyền vào và hết hạn sau {@code expire} phút
     *
     * @param user   người dùng
     * @param expire thời gian hết hạn token
     *
     * @return chuỗi token
     */
    public static String generateToken(User user, int expire) {
        //convert user to json
        return JWT.create()
                .withSubject(String.valueOf(user.getId()))
                .withPayload(ObjectUtil.parseJson(user))
                .withExpiresAt(Date.from(Instant.now()
                        .plus(expire, ChronoUnit.MINUTES)))
                .sign(algorithm);

    }

    public static DecodedJWT verifyToken(String token) {
        try {
            return verifier.verify(token);
        } catch (Exception e) {
            return null;
        }
    }
}
