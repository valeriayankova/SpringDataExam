package com.example.football.service.impl;

import com.example.football.models.dto.PlayerRootSeedDTO;
import com.example.football.models.entity.PlayerEntity;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYER_CONTENT_PATH = "src/main/resources/files/xml/players.xml";

    private final PlayerRepository playerRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final TownService townService;
    private final TeamService teamService;
    private final StatService statService;

    public PlayerServiceImpl(PlayerRepository playerRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, TownService townService, TeamService teamService, StatService statService) {
        this.playerRepository = playerRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.townService = townService;
        this.teamService = teamService;
        this.statService = statService;
    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYER_CONTENT_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        if (!areImported()) {
            PlayerRootSeedDTO playerRootSeedDTO = xmlParser.fromFile(PLAYER_CONTENT_PATH, PlayerRootSeedDTO.class);

            playerRootSeedDTO.getPlayersSeedDTOs()
                    .forEach(playerSeedDTO -> {
                        boolean isValid = validationUtil.isValid(playerSeedDTO) &&
                                !playerRepository.existsByEmail(playerSeedDTO.getEmail());

                        if (isValid) {
                            sb.append(String.format("Successfully imported Player %s %s - %s%n",
                                    playerSeedDTO.getFirstName(), playerSeedDTO.getLastName(),
                                    playerSeedDTO.getPosition()));

                            PlayerEntity player = modelMapper.map(playerSeedDTO, PlayerEntity.class);
                            player.setTown(townService.findByName(playerSeedDTO.getTown().getName()));
                            player.setTeam(teamService.findByName(playerSeedDTO.getTeam().getName()));
                            player.setStat(statService.findById(playerSeedDTO.getStat().getId()));

                            playerRepository.save(player);
                        } else {
                            sb.append("Invalid Player!").append(System.lineSeparator());
                        }
                    });
        }

        return sb.toString();
    }

    @Override
    public String exportBestPlayers() {
        List<PlayerEntity> bestPlayersByStats = playerRepository.getBestPlayersByStats();

                return bestPlayersByStats.stream()
                .map(p -> String.format("Player - %s %s%n" +
                                "\tPosition - %s%n" +
                                "\tTeam - %s%n" +
                                "\tStadium - %s",
                        p.getFirstName(), p.getLastName(),
                        p.getPosition(), p.getTeam().getName(),
                        p.getTeam().getStadiumName()))
                .collect(Collectors.joining(System.lineSeparator()
                ));
    }
}
