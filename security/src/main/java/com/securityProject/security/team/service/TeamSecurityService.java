package com.securityProject.security.team.service;

import com.securityProject.security.team.model.Team;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TeamSecurityService {

    private final TeamService teamService;

    public TeamSecurityService(TeamService teamService) {
        this.teamService = teamService;
    }

    public boolean isTeamOwner(String teamId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserId = authentication.getName(); // Assuming the userId is stored in JWT
        Team team = teamService.getTeamById(teamId).getBody();
        return team != null && team.getOwner().getId().equals(currentUserId);
    }
}