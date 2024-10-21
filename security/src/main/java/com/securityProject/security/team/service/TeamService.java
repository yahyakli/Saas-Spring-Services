package com.securityProject.security.team.service;

import com.securityProject.security.team.controller.TeamRequest;
import com.securityProject.security.team.model.Team;
import com.securityProject.security.team.repo.TeamRepository;
import com.securityProject.security.user.model.User;
import com.securityProject.security.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Team> createTeam(TeamRequest request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElse(null);

        if (owner == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Team team = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(owner)
                .created_at(new Date())
                .updated_at(new Date())
                .build();

        Team savedTeam = teamRepository.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeam);
    }

    public ResponseEntity<Team> getTeamById(String teamId) {
        Team team = teamRepository.findById(teamId)
                .orElse(null);

        if (team == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(team);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public ResponseEntity<Team> updateTeam(String teamId, TeamRequest request) {
        Team team = teamRepository.findById(teamId)
                .orElse(null);

        if (team == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setUpdated_at(new Date());

        Team updatedTeam = teamRepository.save(team);
        return ResponseEntity.ok(updatedTeam);
    }

    public ResponseEntity<String> deleteTeam(String teamId) {
        Team team = teamRepository.findById(teamId)
                .orElse(null);

        if (team == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found");
        }

        teamRepository.delete(team);
        return ResponseEntity.ok("Team Deleted Successfully");
    }
}
