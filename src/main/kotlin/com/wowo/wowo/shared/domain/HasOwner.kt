package com.wowo.wowo.shared.domain


interface IHasOwner<IdType> {
    var ownerId: IdType
    var ownerType: OwnerType
    var ownerName: String?
}
