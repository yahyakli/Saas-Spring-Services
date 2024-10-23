package com.securityProject.security.team.controller;

import com.securityProject.security.team.model.Team;
import com.securityProject.security.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Team> createTeam(@RequestBody TeamRequest request) {
        return teamService.createTeam(request);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")  // Allow any authenticated user to get teams
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        if (teams.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{teamId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Team> getTeamById(@PathVariable String teamId) {
        return teamService.getTeamById(teamId);
    }

    @PutMapping("/{teamId}")
    @PreAuthorize("hasRole('ADMIN') or @teamSecurityService.isTeamOwner(#teamId)")  // Allow only team owners or admins to update
    public ResponseEntity<Team> updateTeam(@PathVariable String teamId, @RequestBody TeamRequest request) {
        return teamService.updateTeam(teamId, request);
    }

    @DeleteMapping("/{teamId}")
    @PreAuthorize("hasRole('ADMIN') or @teamSecurityService.isTeamOwner(#teamId)")  // Allow only team owners or admins to delete
    public ResponseEntity<String> deleteTeam(@PathVariable String teamId) {
        return teamService.deleteTeam(teamId);
    }
}