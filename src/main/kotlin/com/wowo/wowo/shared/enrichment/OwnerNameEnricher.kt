package com.wowo.wowo.shared.enrichment

import com.wowo.wowo.contexts.user.domain.repository.*
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import com.wowo.wowo.shared.domain.*
import org.springframework.stereotype.*
import java.util.UUID

@Component
class OwnerNameEnricher(val userRepository: UserRepository) : IEnricher<IHasOwner<String>> {


    override fun enrich(data: IHasOwner<String>): IHasOwner<String> {
        when (data.ownerType) {
            OwnerType.USER -> {
                val user = userRepository.findById(UserId(UUID.fromString(data.ownerId)))
                if (user != null) {
                    data.ownerName = user.username.value
                }
            }

            else -> {}
        }


        return data
    }
}

