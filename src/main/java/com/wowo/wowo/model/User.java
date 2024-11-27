package com.wowo.wowo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@Builder
@Table(name = "\"user\"", indexes = {
        @Index(name = "search_unique_user", columnList = "id,username,email")
})
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Size(max = 32)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "username", unique = true)
    @Size(max = 255)
    private String username;

    @Column(name = "email", unique = true)
    @Size(max = 255)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserWallet wallet;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @ColumnDefault("0")
    @Column(name = "total_money", nullable = false)
    private Long totalMoney = 0L;

    @Size(max = 255)
    @Column(name = "job")
    private String job;

    @Column(name = "avatar")
    private String avatar;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    private Collection<Application> applications = new ArrayList<>();

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addApplication(Application application) {
        if (application == null) {
            return;
        }
        applications.add(application);
        application.setOwner(this);
    }

    public void removeApplication(Application application) {
        if (application == null) {
            return;
        }
        applications.remove(application);
        application.setOwner(null);
    }
}