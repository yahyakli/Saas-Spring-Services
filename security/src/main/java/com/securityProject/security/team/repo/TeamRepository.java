package com.securityProject.security.team.repo;

import com.securityProject.security.team.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamRepository extends JpaRepository<Team, String> {

}