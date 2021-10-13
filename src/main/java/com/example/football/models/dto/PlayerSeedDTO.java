package com.example.football.models.dto;

import com.example.football.models.entity.enums.PositionEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedDTO {

    @XmlElement(name = "first-name")
    private String firstName;
    @XmlElement(name = "last-name")
    private String lastName;
    @XmlElement(name = "email")
    private String email;
    @XmlElement(name = "birth-date")
    private String birthDate;
    @XmlElement(name = "position")
    private PositionEnum position;
    @XmlElement(name = "town")
    private PlayerTownSeedDTO town;
    @XmlElement(name = "team")
    private PlayerTeamSeedDTO team;
    @XmlElement(name = "stat")
    private PlayerStatSeedDTO stat;

    @NotNull
    @Size(min = 3)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull
    @Size(min = 3)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public PositionEnum getPosition() {
        return position;
    }

    public void setPosition(PositionEnum position) {
        this.position = position;
    }


    public PlayerTownSeedDTO getTown() {
        return town;
    }

    public void setTown(PlayerTownSeedDTO town) {
        this.town = town;
    }


    public PlayerTeamSeedDTO getTeam() {
        return team;
    }

    public void setTeam(PlayerTeamSeedDTO team) {
        this.team = team;
    }

    public PlayerStatSeedDTO getStat() {
        return stat;
    }

    public void setStat(PlayerStatSeedDTO stat) {
        this.stat = stat;
    }
}
