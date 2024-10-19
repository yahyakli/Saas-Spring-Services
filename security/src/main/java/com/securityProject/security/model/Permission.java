package com.securityProject.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

@Entity
public class Permission {
    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

}