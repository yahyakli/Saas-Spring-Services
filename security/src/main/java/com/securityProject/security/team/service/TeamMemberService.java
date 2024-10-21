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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamMember addMember(String teamId, String userId, TeamRole role) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(user)
                .role(role)
                .build();

        return teamMemberRepository.save(teamMember);
    }

    public ResponseEntity<Void> removeMember(String teamMemberId) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemberId)
                .orElse(null);

        if (teamMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if not found
        }

        teamMemberRepository.delete(teamMember);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<TeamMember>> getMembers(String teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        List<TeamMember> members = teamMemberRepository.findAll();
        return ResponseEntity.ok(members);
    }
}
