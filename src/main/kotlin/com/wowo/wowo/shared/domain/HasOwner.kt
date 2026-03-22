package com.wowo.wowo.shared.domain

interface HasOwner<IdType> : IHasOwner {
    val ownerId: IdType
    val ownerType: OwnerType
    val ownerName: String?
}

interface IHasOwner {}