package com.wowo.wowo.shared.domain

interface HasOwnerDto<IdType> {
    val ownerId: IdType
    val ownerType: OwnerType
    val ownerName: String?
}