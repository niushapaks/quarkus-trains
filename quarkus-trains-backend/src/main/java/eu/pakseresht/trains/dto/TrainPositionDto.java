package eu.pakseresht.trains.dto;

import java.io.Serializable;

public class TrainPositionDto implements Serializable {

    private Long trackId;
    private Float kilometer;

    public TrainPositionDto(Long trackId, Float kilometer) {
        this.trackId = trackId;
        this.kilometer = kilometer;
    }

    public TrainPositionDto() {
        super();
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Float getKilometer() {
        return kilometer;
    }

    public void setKilometer(Float kilometer) {
        this.kilometer = kilometer;
    }
}
