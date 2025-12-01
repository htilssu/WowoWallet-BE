package com.wowo.wowo.contexts.user.application.usecase

import com.wowo.wowo.contexts.user.application.dto.RegisterUserCommand
import com.wowo.wowo.contexts.user.application.dto.UserDTO
import com.wowo.wowo.contexts.user.domain.entity.User
import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.contexts.user.domain.valueobject.Password
import com.wowo.wowo.contexts.user.domain.valueobject.Username
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.exception.BusinessRuleViolationException
import com.wowo.wowo.shared.valueobject.Email
import com.wowo.wowo.shared.valueobject.PhoneNumber
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Use Case: Register a new user
 */
@Service
@Transactional
class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val eventPublisher: DomainEventPublisher
) {
    fun execute(command: RegisterUserCommand): UserDTO {
        // Business rule: Username must be unique
        val username = Username(command.username)
        if (userRepository.existsByUsername(username)) {
            throw BusinessRuleViolationException("Username already exists: ${command.username}")
        }

        // Business rule: Email must be unique
        val email = Email(command.email)
        if (userRepository.existsByEmail(email)) {
            throw BusinessRuleViolationException("Email already exists: ${command.email}")
        }

        // Create user aggregate
        val password = Password.fromRaw(command.password)
        val phoneNumber = command.phoneNumber?.let { PhoneNumber(it) }

        val user = User.register(username, password, email, phoneNumber)

        // Persist user
        val savedUser = userRepository.save(user)

        // Publish domain events
        eventPublisher.publish(savedUser.getDomainEvents())
        savedUser.clearDomainEvents()

        return UserDTO.fromDomain(savedUser)
    }
}

