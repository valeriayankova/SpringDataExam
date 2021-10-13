package com.example.football.service.impl;

import com.example.football.models.dto.StatsRootSeedDTO;
import com.example.football.models.entity.StatEntity;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StatServiceImpl implements StatService {

    private static final String STAT_CONTENT_PATH = "src/main/resources/files/xml/stats.xml";

    private final StatRepository statRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;

    public StatServiceImpl(StatRepository statRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil) {
        this.statRepository = statRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files.readString(Path.of(STAT_CONTENT_PATH));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        if (!areImported()) {
            StatsRootSeedDTO statsRootSeedDTO = xmlParser.fromFile(STAT_CONTENT_PATH, StatsRootSeedDTO.class);

            statsRootSeedDTO.getStatSeedDTOs()
                    .forEach(statSeedDTO -> {
                        double passing = statSeedDTO.getPassing();
                        double shooting = statSeedDTO.getShooting();
                        double endurance = statSeedDTO.getEndurance();

                        boolean isValid = validationUtil.isValid(statSeedDTO) &&
                                !statRepository
                                        .existsByPassingAndShootingAndEndurance(
                                                passing, shooting, endurance);

                        if (isValid) {
                            sb.append(String.format("Successfully imported Stat %.2f - %.2f - %.2f%n",
                                    passing, shooting, endurance));
                            statRepository.save(modelMapper.map(statSeedDTO, StatEntity.class));
                        } else {
                            sb.append("Invalid Stat!").append(System.lineSeparator());
                        }
                    });
        }

        return sb.toString();
    }

    @Override
    public StatEntity findById(Long id) {
        return statRepository.getById(id);
    }
}
