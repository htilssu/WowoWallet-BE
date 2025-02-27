package com.wowo.wowo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "group_fund")
public class GroupFund {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_fund_id_seq")
    @SequenceGenerator(name = "group_fund_id_seq", sequenceName = "group_fund_id_seq",
                       allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 256)
    @Column(name = "image")
    private String image;

    @Size(max = 100)
    @Column(name = "type")
    private String type;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "target", nullable = false)
    private Long target;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @OneToOne(mappedBy = "groupFund", cascade = CascadeType.ALL)
    private GroupFundWallet wallet;

    @Column(name = "is_locked", nullable = false, columnDefinition = "boolean default false")
    private boolean isLocked = false;

    @OneToMany(mappedBy = "group")
    private Set<FundMember> fundMembers = new LinkedHashSet<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    public boolean isMember(User user) {
        return getFundMembers().stream()
                .anyMatch(member -> member.getMember()
                        .equals(user));
    }
}
