package henrique.matheus.report;

import henrique.matheus.models.DayModel;
import henrique.matheus.models.LazerModel;
import henrique.matheus.models.TaskModel;
import henrique.matheus.models.TaskTimeBetweenModel;
import henrique.matheus.parameters.GlobalParameters;
import henrique.matheus.repositories.LazerRepository;
import henrique.matheus.repositories.TaskRepository;
import henrique.matheus.repositories.TaskTimeBetweenRepository;
import henrique.matheus.services.DayService;
import henrique.matheus.timeutils.TimeUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class Report {
    private final DayService dayService;
    private final TaskRepository taskRepository;
    private final TaskTimeBetweenRepository taskTimeBetweenRepository;
    private final LazerRepository lazerRepository;

    public Report(DayService dayService, TaskRepository taskRepository, TaskTimeBetweenRepository taskTimeBetweenRepository, LazerRepository lazerRepository) {
        this.dayService = dayService;
        this.taskRepository = taskRepository;
        this.taskTimeBetweenRepository = taskTimeBetweenRepository;
        this.lazerRepository = lazerRepository;
    }

    public void showDayReport() {
        calculateRemainingTime();
        allTimeSpent();
    }

    public void showTaskList() {
       var tasks = taskRepository.findByDateOrderByPositionAsc(LocalDate.now());
       if (tasks.isEmpty()) return;

       for (TaskModel task : tasks.get())
           System.out.println(task.getPosition() + " - " + task.getDescription());
    }

    public void startTask(Integer taskPosition) {
        var task = taskRepository.findByDateAndPosition(LocalDate.now(), taskPosition);
        if (task.isEmpty()) return;
        var newTaskTime = new TaskTimeBetweenModel();
        newTaskTime.setTask(task.get());
        newTaskTime.setTimeStarted(LocalTime.now());
        taskTimeBetweenRepository.save(newTaskTime);
    }

    private void calculateRemainingTime() {
        var day = dayService.getCurrentDay();
        if (day.isEmpty()) return;

        var remainingTime = GlobalParameters.finalTime;
        remainingTime = remainingTime.minusSeconds(LocalTime.now().toSecondOfDay());
        remainingTime = calculateBaseTimePerPeriod(remainingTime, day.get());

        System.out.printf(
            """
            The remaining time is: %s
            45%% Lazer - %s
            55%% Tasks - %s \n
            """,
                TimeUtils.formatDuration(remainingTime),
                TimeUtils.formatDuration(remainingTime.multipliedBy(45).dividedBy(100)),
                TimeUtils.formatDuration(remainingTime.multipliedBy(55).dividedBy(100))
        );
    }

    private Duration calculateBaseTimePerPeriod(Duration remainingTime, DayModel day) {
        var currentTime = LocalTime.now();
        if (currentTime.isBefore(LocalTime.of(22, 0))) {
            remainingTime = remainingTime.minus(GlobalParameters.supperTime);
        }

        if (currentTime.isBefore(LocalTime.of(20, 0))) {
            remainingTime = remainingTime.minus(GlobalParameters.dinnerTime);
        }

        if (currentTime.isBefore(LocalTime.of(18, 0))) {
            if (day.getWorkDay()) {
                remainingTime = remainingTime.minus(GlobalParameters.workTime.dividedBy(2));
            }
            remainingTime = remainingTime.minus(GlobalParameters.showerTime);
        }

        if (currentTime.isBefore(LocalTime.of(16, 30)) && !day.getWorkDay()) {
            remainingTime = remainingTime.minus(GlobalParameters.snackTime);
        }

        if (currentTime.isBefore(LocalTime.of(13, 0))) {
            remainingTime = remainingTime.minus(GlobalParameters.dinnerTime);
            if (day.getWorkDay()) {
                remainingTime = remainingTime.minus(GlobalParameters.workTime.dividedBy(2));
            }
        }

        if (currentTime.isBefore(LocalTime.of(8, 0))) {
            remainingTime = remainingTime.minus(GlobalParameters.breakFastTime);
        }

        return remainingTime;
    }

    public void showLazeresList() {
        var lazeres = lazerRepository.findByDateOrderByPositionAsc(LocalDate.now());
        if (lazeres.isEmpty()) return;

        for (LazerModel lazer : lazeres.get())
            System.out.println(lazer.getPosition() + " - " + lazer.getDescription());
    }

    public void allTimeSpent() {
        var lazerDuration = Duration.ofSeconds(lazerRepository.getTotalDurationInSeconds(LocalDate.now())
                                                            .orElse(0L));
        var taskDuration = Duration.ofSeconds(taskRepository.getTotalDurationInSeconds(LocalDate.now())
                                                            .orElse(0L));

        System.out.printf(
            """   
            Spent time on tasks: %s
            Spent time on lazeres: %s
            Total spent time: %s \n
            """,
                TimeUtils.formatDuration(taskDuration),
                TimeUtils.formatDuration(lazerDuration),
                TimeUtils.formatDuration(lazerDuration.plus(taskDuration))
        );
    }
}
