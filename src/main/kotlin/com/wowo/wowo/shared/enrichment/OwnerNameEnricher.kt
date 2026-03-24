package com.wowo.wowo.shared.enrichment

import com.wowo.wowo.contexts.groupfund.domain.*
import com.wowo.wowo.contexts.groupfund.infrastructure.persistence.*
import com.wowo.wowo.contexts.user.domain.repository.*
import com.wowo.wowo.contexts.user.domain.valueobject.*
import com.wowo.wowo.shared.domain.*
import org.springframework.stereotype.*
import java.util.*

@Component
class OwnerNameEnricher(
    val userRepository: UserRepository, private val groupFundRepositoryAdapter: GroupFundRepositoryAdapter
) : IEnricher<IHasOwner<String>> {


    override fun enrich(data: IHasOwner<String>): IHasOwner<String> {
        when (data.ownerType) {
            OwnerType.USER -> {
                val user = userRepository.findById(UserId(UUID.fromString(data.ownerId)))
                if (user != null) {
                    data.ownerName = user.username.value
                }
            }

            OwnerType.FUND_GROUP -> {
                val groupFund = groupFundRepositoryAdapter.findById(GroupId(UUID.fromString(data.ownerId)))
                if (groupFund != null) {
                    data.ownerName = groupFund.name
                }
            }

            else -> {}
        }


        return data
    }
}

