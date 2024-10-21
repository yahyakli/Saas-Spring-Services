package com.securityProject.security.team.controller;


import com.securityProject.security.team.model.TeamMember;
import com.securityProject.security.team.repo.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/team-members")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberRepository teamMemberRepository;

    @PostMapping
    public ResponseEntity<TeamMember> addMember(@RequestBody TeamMember teamMember) {
        return ResponseEntity.ok(teamMemberRepository.save(teamMember));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<TeamMember>> getMembersByTeam(@PathVariable String teamId) {
        return ResponseEntity.ok(teamMemberRepository.findByTeamId(teamId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TeamMember>> getTeamsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(teamMemberRepository.findByUserId(userId));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Object> removeMember(@PathVariable String memberId) {
        return teamMemberRepository.findById(memberId)
                .map(member -> {
                    teamMemberRepository.delete(member);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}