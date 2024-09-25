package com.wowo.wowo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ServiceStatisticId implements Serializable {

    private static final long serialVersionUID = -834031700420048089L;
    @NotNull
    @Column(name = "month", nullable = false)
    private Integer month;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Column(name = "service", nullable = false)
    private Integer service;

    @Override
    public int hashCode() {
        return Objects.hash(month, year, service);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ServiceStatisticId entity = (ServiceStatisticId) o;
        return Objects.equals(this.month, entity.month) &&
                Objects.equals(this.year, entity.year) &&
                Objects.equals(this.service, entity.service);
    }

}