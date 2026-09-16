package com.example.mapper;

import com.example.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReferenceMapper {

    @Autowired
    private EntityManager entityManager;

    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id == null ? null : entityManager.find(entityClass, id);
    }
}
