package com.example.football.service;

import com.example.football.models.entity.TeamEntity;

import java.io.IOException;

public interface TeamService {
    boolean areImported();

    String readTeamsFileContent() throws IOException;

    String importTeams() throws IOException;

    TeamEntity findByName(String name);
}
