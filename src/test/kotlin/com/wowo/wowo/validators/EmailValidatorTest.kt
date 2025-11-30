package com.wowo.wowo.validators

import com.wowo.wowo.shared.valueobject.Email
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@DisplayName("Email Validator Test")
class EmailValidatorTest {

    @Test
    @DisplayName("Basic Email Validator Test")
    fun `basic email validator`() {
        val validEmail = "tolashuu@gmail.com"
        val invalidEmail = "tolashuu@gmail"

        // valid should construct successfully
        assertDoesNotThrow { Email(validEmail) }
        // invalid should throw
        assertThrows(IllegalArgumentException::class.java) { Email(invalidEmail) }
    }

    @Test
    @DisplayName("Advanced Email Validator Test")
    fun `advanced email validator`() {
        val validEmail = "tolashuu@gmial.com"
        val invalidEmail = "tolashuu@gmail"
        val invalidEmail2 = "tolas.com"
        val invalidEmail3 = "tolas@@cdf.com"

        assertDoesNotThrow { Email(validEmail) }
        assertThrows(IllegalArgumentException::class.java) { Email(invalidEmail) }
        assertThrows(IllegalArgumentException::class.java) { Email(invalidEmail2) }
        assertThrows(IllegalArgumentException::class.java) { Email(invalidEmail3) }
    }
}
