package com.wowo.wowo.contexts.user.application.usecase

import com.wowo.wowo.contexts.user.application.dto.UserDTO
import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import com.wowo.wowo.shared.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Use Case: Get user by ID
 */
@Service
@Transactional(readOnly = true)
class GetUserUseCase(
    private val userRepository: UserRepository
) {
    fun execute(userId: String): UserDTO {
        val user = userRepository.findById(UserId.fromString(userId))
            ?: throw EntityNotFoundException("User not found with ID: $userId")

        return UserDTO.fromDomain(user)
    }
}

