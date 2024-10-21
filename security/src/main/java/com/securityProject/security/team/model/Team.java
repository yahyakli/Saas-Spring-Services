package com.securityProject.security.team.model;


import com.securityProject.security.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teams")
public class Team {
    @Id
    @UuidGenerator
    private String id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    private Date created_at;
    private Date updated_at;
}
