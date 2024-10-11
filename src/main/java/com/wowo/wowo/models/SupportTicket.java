package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "support_ticket")
public class SupportTicket {

    @Id
    @Size(max = 15)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "support_ticket_id_seq")
    @SequenceGenerator(name = "support_ticket_id_seq", sequenceName = "support_ticket_id_seq",
                       allocationSize = 1)
    @Column(name = "id", nullable = false, length = 15)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @NotNull
    @Column(name = "status", nullable = false)
    private SupportTicketStatus status = SupportTicketStatus.OPEN;
}