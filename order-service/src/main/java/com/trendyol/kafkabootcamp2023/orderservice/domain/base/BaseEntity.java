package com.trendyol.kafkabootcamp2023.orderservice.domain.base;

import java.io.Serializable;
import java.util.Objects;

public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -3851519619341314461L;

    public abstract Serializable getId();

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (getId() == null || ((BaseEntity) other).getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ((BaseEntity) other).getId());
    }

}