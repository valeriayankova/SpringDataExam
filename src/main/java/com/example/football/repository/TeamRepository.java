package com.example.football.repository;

import com.example.football.models.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    boolean existsByName(String name);

    TeamEntity findByName(String name);
}
