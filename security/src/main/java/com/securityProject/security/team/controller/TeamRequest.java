package com.securityProject.security.team.controller;

import lombok.Data;

@Data
public class TeamRequest {
    private String name;
    private String description;
    private String ownerId;
}