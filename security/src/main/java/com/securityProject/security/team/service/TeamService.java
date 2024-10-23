package com.securityProject.security.team.service;

import com.securityProject.security.team.controller.TeamRequest;
import com.securityProject.security.team.model.Team;
import com.securityProject.security.team.repo.TeamRepository;
import com.securityProject.security.user.model.User;
import com.securityProject.security.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    // Helper method to get the authenticated user from the JWT token
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming the email is stored in the JWT token
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ResponseEntity<Team> createTeam(TeamRequest request) {
        // Get the authenticated user from the JWT
        User owner = getAuthenticatedUser();

        // Create the team with the authenticated user as the owner
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

        // Only allow the owner of the team to update it
        User authenticatedUser = getAuthenticatedUser();
        if (!team.getOwner().getId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
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

        // Only allow the owner of the team to delete it
        User authenticatedUser = getAuthenticatedUser();
        if (!team.getOwner().getId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this team");
        }

        teamRepository.delete(team);
        return ResponseEntity.ok("Team Deleted Successfully");
    }
}