package com.wowo.wowo.contexts.transaction.infrastructure.persistence

import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionId
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface TransactionPersistenceMapper {
    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "sourceWalletId", source = "sourceWalletId")
    @Mapping(target = "targetWalletId", source = "targetWalletId")
    @Mapping(target = "amount", expression = "java(transaction.getAmount().getAmount())")
    @Mapping(target = "currency", expression = "java(transaction.getAmount().getCurrency().name())")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", expression = "java(transaction.getStatus())")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    fun toJpaEntity(transaction: Transaction): TransactionJpaEntity

    @Mapping(target = "id", expression = "java(new TransactionId(jpaEntity.getId()))")
    @Mapping(target = "sourceWalletId", source = "sourceWalletId")
    @Mapping(target = "targetWalletId", source = "targetWalletId")
    @Mapping(target = "amount", expression = "java(new Money(jpaEntity.getAmount(), com.wowo.wowo.shared.valueobject.Currency.valueOf(jpaEntity.getCurrency())))")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    fun toDomainEntity(jpaEntity: TransactionJpaEntity): Transaction
}
