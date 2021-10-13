package com.example.football.repository;

import com.example.football.models.entity.StatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepository extends JpaRepository<StatEntity, Long> {

    boolean existsByPassingAndShootingAndEndurance(double passing, double shooting, double endurance);
}
