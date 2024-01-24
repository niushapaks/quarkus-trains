package eu.pakseresht.trains.dto;

import java.io.Serializable;

public class TrainDto implements Serializable {

    private Long id;
    private String name;
    private TrainPositionDto position;

    public TrainDto() {
        super();
    }

    public TrainDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TrainDto(Long id, String name, TrainPositionDto position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TrainPositionDto getPosition() {
        return position;
    }

    public void setPosition(TrainPositionDto position) {
        this.position = position;
    }
}
