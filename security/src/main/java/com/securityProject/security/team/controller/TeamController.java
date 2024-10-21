package com.securityProject.security.team.controller;

import com.securityProject.security.team.model.Team;
import com.securityProject.security.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody TeamRequest request) {
        return teamService.createTeam(request);
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        if (teams.isEmpty()) {
            return ResponseEntity.status(404).body(null); // Return 404 if no teams are found
        }
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable String teamId) {
        return teamService.getTeamById(teamId);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<Team> updateTeam(
            @PathVariable String teamId,
            @RequestBody TeamRequest request
    ) {
        return teamService.updateTeam(teamId, request);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(@PathVariable String teamId) {
        return teamService.deleteTeam(teamId);
    }
}
