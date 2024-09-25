package com.wowo.wowo.models;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_partner")
public class UserPartner {

    @EmbeddedId
    private UserPartnerId id;

    //TODO [Reverse Engineering] generate columns from DB
}