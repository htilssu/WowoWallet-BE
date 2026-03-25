package com.wowo.wowo.contexts.wallet.application.enricher

import com.wowo.wowo.contexts.groupfund.domain.GroupId
import com.wowo.wowo.contexts.groupfund.infrastructure.persistence.GroupFundRepositoryAdapter
import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import com.wowo.wowo.shared.domain.IHasOwner
import com.wowo.wowo.shared.domain.OwnerType
import com.wowo.wowo.shared.enrichment.IEnricher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component
import java.util.UUID

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

    override suspend fun enrichMany(data: List<IHasOwner<String>>): List<IHasOwner<String>> {
        val ownerBatch = data.groupBy { it.ownerType }.mapValues { it.value.toSet() }
        coroutineScope {
            ownerBatch.map { (ownerType, items) ->
                async {
                    when (ownerType) {
                        OwnerType.USER -> {
                            val userIds = items.map { UserId(UUID.fromString(it.ownerId)) }.toSet().toList()
                            val users = userRepository.findByIds(userIds)
                            val userMap = users.associateBy { it.id }
                            items.forEach { item ->
                                val user = userMap[UserId(UUID.fromString(item.ownerId))]
                                if (user != null) {
                                    item.ownerName = user.username.value
                                }
                            }
                        }

                        OwnerType.FUND_GROUP -> {
                            val groupIds = items.map { GroupId(UUID.fromString(it.ownerId)) }.toSet()
                            val groupFunds = groupIds.mapNotNull { groupFundRepositoryAdapter.findById(it) }
                            val groupFundMap = groupFunds.associateBy { it.id }
                            items.forEach { item ->
                                val groupFund = groupFundMap[GroupId(UUID.fromString(item.ownerId))]
                                if (groupFund != null) {
                                    item.ownerName = groupFund.name
                                }
                            }
                        }

                        else -> {}
                    }
                }
            }.awaitAll()
        }


        return data
    }
}