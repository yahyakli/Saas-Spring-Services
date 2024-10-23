package com.securityProject.security.team.service;

import com.securityProject.security.team.model.Team;
import com.securityProject.security.team.model.TeamMember;
import com.securityProject.security.team.model.TeamRole;
import com.securityProject.security.team.repo.TeamMemberRepository;
import com.securityProject.security.team.repo.TeamRepository;
import com.securityProject.security.user.model.User;
import com.securityProject.security.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    // Helper method to get the authenticated user from the JWT token
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Assuming the email is stored in the JWT token
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public ResponseEntity<TeamMember> addMember(String teamId, String userId, TeamRole role) {
        Team team = teamRepository.findById(teamId)
                .orElse(null);
        if (team == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Team not found
        }

        // Check if the authenticated user is the owner of the team
        User authenticatedUser = getAuthenticatedUser();
        if (!team.getOwner().getId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // Only team owner can add members
        }

        User user = userRepository.findById(userId)
                .orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // User not found
        }

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(user)
                .role(role)
                .build();

        TeamMember savedTeamMember = teamMemberRepository.save(teamMember);
        return ResponseEntity.ok(savedTeamMember);
    }

    public ResponseEntity<Void> removeMember(String teamMemberId) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElse(null);

        if (teamMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Team member not found
        }

        // Check if the authenticated user is the owner of the team
        User authenticatedUser = getAuthenticatedUser();
        if (!teamMember.getTeam().getOwner().getId().equals(authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Only team owner can remove members
        }

        teamMemberRepository.delete(teamMember);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<TeamMember>> getMembers(String teamId) {
        Team team = teamRepository.findById(teamId)
                .orElse(null);
        if (team == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Team not found
        }

        List<TeamMember> members = teamMemberRepository.findByTeamId(teamId);
        return ResponseEntity.ok(members);
    }
}