package com.yordan.karabelyov.shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseAuditEntity extends BaseEntity {
    public static final String ATTR_CREATED_AT = "createdAt";
    @Serial
    private static final long serialVersionUID = -2176339454270784217L;

    @NotNull
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseAuditEntity that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdAt);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
