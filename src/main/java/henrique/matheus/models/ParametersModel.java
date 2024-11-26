package henrique.matheus.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "PARAMETERS")
public class ParametersModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID Id;

    private String dailyTasksFolder;
    private Integer minutesWorkTime;
    private LocalDateTime lastChange;

    @PrePersist
    @PreUpdate
    public void updateLastChange() {
        this.lastChange = LocalDateTime.now();
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getDailyTasksFolder() {
        return dailyTasksFolder;
    }

    public void setDailyTasksFolder(String dailyTasksFolder) {
        this.dailyTasksFolder = dailyTasksFolder;
    }

    public Integer getMinutesWorkTime() {
        return minutesWorkTime;
    }

    public void setMinutesWorkTime(Integer minutesWorkTime) {
        this.minutesWorkTime = minutesWorkTime;
    }

    public LocalDateTime getLastChange() {
        return lastChange;
    }

    public void setLastChange(LocalDateTime lastChange) {
        this.lastChange = lastChange;
    }
}
