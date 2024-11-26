package henrique.matheus.services;

import henrique.matheus.models.TaskModel;
import henrique.matheus.models.TaskTimeBetweenModel;
import henrique.matheus.repositories.TaskRepository;
import henrique.matheus.repositories.TaskTimeBetweenRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskTimeBetweenRepository taskTimeBetweenRepository;

    public TaskService(TaskRepository taskRepository, TaskTimeBetweenRepository taskTimeBetweenRepository) {
        this.taskRepository = taskRepository;
        this.taskTimeBetweenRepository = taskTimeBetweenRepository;
    }

    public void addMinutes(Integer position, LocalDate date, Integer minutes) {
        var task = taskRepository.findByDateAndPosition(date, position);
        if (task.isPresent()) {
            task.get().setMinutesAdded(
                    Optional.ofNullable(task.get().getMinutesAdded())
                            .orElse(0) + minutes
            );
            task.get().setDurationInSeconds(
                    Optional.ofNullable(task.get().getDurationInSeconds())
                            .orElse(0L) + (60L * minutes)
            );
            taskRepository.save(task.get());
        }
    }

    public void mergeTimeBetween(TaskModel task, TaskTimeBetweenModel taskTimes) {
        if (taskTimes.getTimeFinished() != null) return;

        taskTimes.setTimeFinished(LocalTime.now());
        taskTimeBetweenRepository.save(taskTimes);

        var durationLastTime = Duration.between(
                taskTimes.getTimeStarted(),
                taskTimes.getTimeFinished()
        ).toSeconds();
        var totalDurationInSeconds = Optional.ofNullable(task.getDurationInSeconds())
                .orElse(0L);

        task.setDurationInSeconds(durationLastTime + totalDurationInSeconds);
        taskRepository.save(task);
    }
}
