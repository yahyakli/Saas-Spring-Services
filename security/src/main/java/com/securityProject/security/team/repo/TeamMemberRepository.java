package com.securityProject.security.team.repo;


import com.securityProject.security.team.model.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {
    List<TeamMember> findByTeamId(String teamId);
    List<TeamMember> findByUserId(String userId);
}