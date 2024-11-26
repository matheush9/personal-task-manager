package henrique.matheus.models;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "TASK_TIME_BETWEEN")
public class TaskTimeBetweenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalTime timeStarted;

    private LocalTime timeFinished;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskModel task;

    public UUID getId() {   
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalTime getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(LocalTime timeStarted) {
        this.timeStarted = timeStarted;
    }

    public LocalTime getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(LocalTime timeFinished) {
        this.timeFinished = timeFinished;
    }

    public TaskModel getTask() {
        return task;
    }

    public void setTask(TaskModel task) {
        this.task = task;
    }
}
