package com.example.football.service.impl;

import com.example.football.models.dto.TownSeedDTO;
import com.example.football.models.entity.TownEntity;
import com.example.football.repository.TownRepository;
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
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of("src/main/resources/files/json/towns.json"));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();

        if (!areImported()) {
            TownSeedDTO[] townSeedDTOS = gson.fromJson(readTownsFileContent(), TownSeedDTO[].class);

            Arrays.stream(townSeedDTOS)
                    .forEach(townSeedDTO -> {
                        boolean isValid = validationUtil.isValid(townSeedDTO) &&
                                !townRepository.existsByName(townSeedDTO.getName());

                        if (isValid) {
                            townRepository.save(modelMapper.map(townSeedDTO, TownEntity.class));
                            sb.append(String.format("Successfully imported Town %s - %d%n",
                                    townSeedDTO.getName(), townSeedDTO.getPopulation()));
                        } else {
                            sb.append("Invalid Town!")
                                    .append(System.lineSeparator());
                        }

                    });


        }
        return sb.toString();
    }

    @Override
    public TownEntity findByName(String townName) {
        return townRepository.findByName(townName);
    }
}
