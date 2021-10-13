package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatsRootSeedDTO {

    @XmlElement(name = "stat")
    private List<StatSeedDTO> statSeedDTOs;

    public List<StatSeedDTO> getStatSeedDTOs() {
        return statSeedDTOs;
    }

    public void setStatSeedDTOs(List<StatSeedDTO> statSeedDTOs) {
        this.statSeedDTOs = statSeedDTOs;
    }
}
