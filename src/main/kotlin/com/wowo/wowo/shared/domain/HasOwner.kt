package com.wowo.wowo.shared.domain

import com.wowo.wowo.shared.domain.OwnerType

interface HasOwner<IdType> {
    val ownerId: IdType
    val ownerType: OwnerType
}