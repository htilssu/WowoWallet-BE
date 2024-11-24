package com.wowo.wowo.model;

import com.wowo.wowo.annotation.id_generator.TransactionIdSequence;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "transaction")
@AllArgsConstructor
@Builder
@NoArgsConstructor
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.JOINED)
public class Transaction {

    @Id
    @TransactionIdSequence
    @Column(name = "id", nullable = false, length = 40)
    private String id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    private String receiverName;
    private String senderName;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private FlowType flowType = FlowType.OUT;

    @Column(length = 300)
    private String message;

    @CreatedDate
    @Column(name = "created", nullable = false)
    private Instant created = Instant.now();

    @Column(name = "updated", nullable = false)
    @LastModifiedDate
    private Instant updated = Instant.now();

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_wallet", nullable = false)
    private Wallet senderWallet;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receive_wallet", nullable = false)
    private Wallet receiveWallet;
}