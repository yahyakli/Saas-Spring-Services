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
        return teamMemberService.addMember(teamId, userId, role);
    }

    @DeleteMapping("/{teamMemberId}")
    public ResponseEntity<Void> removeMember(@PathVariable String teamMemberId) {
        return teamMemberService.removeMember(teamMemberId);
    }

    @GetMapping
    public ResponseEntity<List<TeamMember>> getMembers(@PathVariable String teamId) {
        return teamMemberService.getMembers(teamId);
    }
}
