package com.wowo.wowo.shared.infrastructure.security.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception thrown when user is not authenticated
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException(
    message: String = "You need to login to access this resource"
) : RuntimeException(message)
