package com.wowo.wowo.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@MappedSuperclass
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BalanceEntity {

    @Min(0)
    @ColumnDefault("0")
    @Column(name = "balance", nullable = false)
    private Long balance = 0L;
    @Version
    @Column(name = "version")
    private Long version;
}
