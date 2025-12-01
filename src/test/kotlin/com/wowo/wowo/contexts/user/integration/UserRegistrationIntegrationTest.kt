package com.wowo.wowo.contexts.user.integration

import com.wowo.wowo.contexts.user.application.dto.RegisterUserCommand
import com.wowo.wowo.contexts.user.application.usecase.RegisterUserUseCase
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class UserRegistrationIntegrationTest {

    @Autowired
    lateinit var registerUserUseCase: RegisterUserUseCase

    @Autowired
    lateinit var walletRepository: WalletRepository

    @Test
    fun `when register user then wallet is created`() {
        val username = "integration_user_${System.currentTimeMillis()}"
        val command = RegisterUserCommand(
            username = username,
            password = "Password123!",
            email = "${username}@example.com",
            phoneNumber = "+84901234567"
        )

        val userDTO = registerUserUseCase.execute(command)

        // After commit, the event handler should have created default wallet
        val wallets = walletRepository.findByUserId(userDTO.id)
        assertTrue(wallets.isNotEmpty(), "Expected at least one wallet for user ${userDTO.id}")
    }
}

