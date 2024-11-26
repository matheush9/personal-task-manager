package henrique.matheus.models;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "TASKS")
public class TaskModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String description;

    private Integer minutesDistracted;

    private Integer minutesRested;

    @Range(min = 0, max = 10)
    private Integer resistanceToStart;

    private String category;

    private Boolean isDone = false;

    private LocalDate date;

    private Long durationInSeconds;

    private Integer position;

    private Integer minutesAdded;

    @OneToMany(mappedBy = "task")
    private Set<TaskTimeBetweenModel> taskTimes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinutesDistracted() {
        return minutesDistracted;
    }

    public void setMinutesDistracted(Integer minutesDistracted) {
        this.minutesDistracted = minutesDistracted;
    }

    public Integer getMinutesRested() {
        return minutesRested;
    }

    public void setMinutesRested(Integer minutesRested) {
        this.minutesRested = minutesRested;
    }

    public @Range(min = 0, max = 10) Integer getResistanceToStart() {
        return resistanceToStart;
    }

    public void setResistanceToStart(@Range(min = 0, max = 10) Integer resistanceToStart) {
        this.resistanceToStart = resistanceToStart;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
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

    public Integer getMinutesAdded() {
        return minutesAdded;
    }

    public void setMinutesAdded(Integer minutesAdded) {
        this.minutesAdded = minutesAdded;
    }

    public Set<TaskTimeBetweenModel> getTaskTimes() {
        return taskTimes;
    }

    public void setTaskTimes(Set<TaskTimeBetweenModel> taskTimes) {
        this.taskTimes = taskTimes;
    }
}
