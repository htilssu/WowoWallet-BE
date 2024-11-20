package com.wowo.wowo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@Table(name = "\"user\"", indexes = {
        @Index(name = "search_unique_user", columnList = "id,username,email")
})
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class User {

    @Id
    @Size(max = 32)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "username")
    @Size(max = 255)
    private String username;

    @Column(name = "email", unique = true)
    @Size(max = 255)
    private String email;

    @Column(name = "first_name")
    private String firstName;

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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                                   ? ((HibernateProxy) o).getHibernateLazyInitializer()
                                           .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                                      ? ((HibernateProxy) this).getHibernateLazyInitializer()
                                              .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
               ? ((HibernateProxy) this).getHibernateLazyInitializer()
                       .getPersistentClass().hashCode() : getClass().hashCode();
    }
}