package com.wowo.wowo;

import com.wowo.wowo.validator.EmailValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Email Validator Test")
public class EmailValidatorTest {

    @Test
    @DisplayName("Basic Email Validator Test")
    public void testEmailValidatorBasic() {
        // test email validator
        String validEmail = "tolashuu@gmail.com";
        String invalidEmail = "tolashuu@gmail";

        assertTrue(EmailValidator.isValid(validEmail));
        assertFalse(EmailValidator.isValid(invalidEmail));
    }


    @Test
    @DisplayName("Advanced Email Validator Test")
    public void testEmailValidatorAdvanced() {
        // test email validator
        String validEmail = "tolashuu@gmial.com";
        String invalidEmail = "tolashuu@gmail";
        String invalidEmail2 = "tolas.com";
        String invalidEmail3 = "tolas@@cdf.com";

        assertTrue(EmailValidator.isValid(validEmail));
        assertFalse(EmailValidator.isValid(invalidEmail));
        assertFalse(EmailValidator.isValid(invalidEmail2));
        assertFalse(EmailValidator.isValid(invalidEmail3));
    }
}
