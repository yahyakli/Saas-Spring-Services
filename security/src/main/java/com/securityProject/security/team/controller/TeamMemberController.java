package com.securityProject.security.team.controller;


import com.securityProject.security.team.model.TeamMember;
import com.securityProject.security.team.model.TeamRole;
import com.securityProject.security.team.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams/{teamId}/members")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @PostMapping
    public ResponseEntity<TeamMember> addMember(
            @PathVariable String teamId,
            @RequestParam String userId,
            @RequestParam TeamRole role) {
        TeamMember teamMember = teamMemberService.addMember(teamId, userId, role);
        return ResponseEntity.ok(teamMember);
    }

    @DeleteMapping("/{teamMemberId}")
    public ResponseEntity<Void> removeMember(@PathVariable String teamMemberId) {
        teamMemberService.removeMember(teamMemberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TeamMember>> getMembers(@PathVariable String teamId) {
        List<TeamMember> members = teamMemberService.getMembers(teamId);
        return ResponseEntity.ok(members);
    }
}