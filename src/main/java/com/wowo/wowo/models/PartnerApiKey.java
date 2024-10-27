package com.wowo.wowo.models;

import com.wowo.wowo.annotations.id_generator.ApiKeyIdSequence;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "partner_api_key")
public class PartnerApiKey {

    @Id
    @NotNull
    @ApiKeyIdSequence
    @Column(name = "id")
    private String id;

    @JoinColumn(name = "partner_id", nullable = false)
    @ManyToOne(targetEntity = Partner.class, fetch = FetchType.LAZY)
    private Partner partner;
}
