package com.securityProject.security.team.controller;



import com.securityProject.security.team.model.Team;
import com.securityProject.security.team.repo.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamRepository teamRepository;

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        return ResponseEntity.ok(teamRepository.save(team));
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        return ResponseEntity.ok(teamRepository.findAll());
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<Team> getTeamById(@PathVariable String teamId) {
        return teamRepository.findById(teamId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<Team> updateTeam(@PathVariable String teamId, @RequestBody Team teamDetails) {
        return teamRepository.findById(teamId)
                .map(team -> {
                    team.setName(teamDetails.getName());
                    team.setDescription(teamDetails.getDescription());
                    return ResponseEntity.ok(teamRepository.save(team));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Object> deleteTeam(@PathVariable String teamId) {
        return teamRepository.findById(teamId)
                .map(team -> {
                    teamRepository.delete(team);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}