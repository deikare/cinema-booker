package com.example.cinemabooker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @Column(name = "id")
    protected String entityId = UUID.randomUUID().toString(); //cannot be named id because of https://github.com/spring-projects/spring-hateoas/issues/67

    public AbstractEntity() {
    }

    @JsonIgnore
    public String getId() {
        return entityId;
    }

    public void setId(String id) {
        this.entityId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return Objects.equals(entityId, that.entityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityId);
    }
}
