package com.wowo.wowo.shared.exception

/**
 * Base exception for domain layer
 */
open class DomainException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class InvalidEntityStateException(message: String) : DomainException(message)
class EntityNotFoundException(message: String) : DomainException(message)
class BusinessRuleViolationException(message: String) : DomainException(message)
class InsufficientBalanceException(message: String) : DomainException(message)
class InvalidOperationException(message: String) : DomainException(message)

