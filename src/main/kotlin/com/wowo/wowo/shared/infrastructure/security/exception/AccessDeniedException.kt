package com.wowo.wowo.shared.infrastructure.security.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception thrown when user does not have permission to access
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
class AccessDeniedException(
    message: String = "You do not have permission to access this resource"
) : RuntimeException(message)
