package com.wowo.wowo.contexts.wallet.domain.valueobject
}
    }
        fun fromString(id: String): WalletId = WalletId(UUID.fromString(id))
    companion object {

    override fun toString(): String = value.toString()
data class WalletId(val value: UUID = UUID.randomUUID()) : ValueObject {
 */
 * Value Object representing Wallet ID
/**

import java.util.*
import com.wowo.wowo.shared.domain.ValueObject


