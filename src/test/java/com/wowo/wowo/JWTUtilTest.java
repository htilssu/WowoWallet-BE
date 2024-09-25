//package com.ewallet.ewallet;
//
//import com.ewallet.ewallet.models.User;
//import com.ewallet.ewallet.util.JwtUtil;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//@DisplayName("JWT Util Test")
//public class JWTUtilTest {
//
//    @Test
//    @DisplayName("Generate Token Passed")
//    public void testGenerateToken() {
//        String token = JwtUtil.generateToken(new User());
//        System.out.println(token);
//    }
//
//    @Test
//    @DisplayName("Decode Token Passed")
//    public void testDecodeToken() {
//        String token = JwtUtil.generateToken(new User());
//
//    }
//
//    @Test
//    @DisplayName("Decode Token Not Passed")
//    public void testDecodeTokenNotPass() {
//        String token = JwtUtil.generateToken(new User("1", "user", "password", "email", "role"));
//        token += "1";
//
//
//        assertNull(JwtUtil.decodeToken(token));
//    }
//}
