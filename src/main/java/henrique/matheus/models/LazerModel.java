package henrique.matheus.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "LAZERES")
public class LazerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID Id;

    private String description;

    private LocalDate date;

    private Long durationInSeconds;

    private Integer position;

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(Long durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
