package com.example.football.service.impl;

import com.example.football.models.dto.TeamSeedDTO;
import com.example.football.models.entity.TeamEntity;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final TownService townService;

    public TeamServiceImpl(TeamRepository teamRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, TownService townService) {
        this.teamRepository = teamRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.townService = townService;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/teams.json"));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();

        if (!areImported()) {
            Arrays.stream(gson.fromJson(readTeamsFileContent(), TeamSeedDTO[].class))
                    .forEach(teamSeedDTO -> {
                        boolean isValid = validationUtil.isValid(teamSeedDTO) &&
                                !teamRepository.existsByName(teamSeedDTO.getName());

                        if (isValid) {
                            TeamEntity team = modelMapper.map(teamSeedDTO, TeamEntity.class);
                            team.setTown(townService.findByName(teamSeedDTO.getTownName()));

                            teamRepository.save(team);

                            sb.append(String.format("Successfully imported Team %s - %d%n",
                                    teamSeedDTO.getName(), teamSeedDTO.getFanBase()));
                        } else {
                            sb.append("Invalid Team!").append(System.lineSeparator());
                        }

                    });
        }

        return sb.toString();
    }

    @Override
    public TeamEntity findByName(String name) {
        return teamRepository.findByName(name);
    }
}
