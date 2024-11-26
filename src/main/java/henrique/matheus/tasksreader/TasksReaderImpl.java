package henrique.matheus.tasksreader;

import henrique.matheus.exceptions.StopReadingException;
import henrique.matheus.models.LazerModel;
import henrique.matheus.models.TaskModel;
import henrique.matheus.repositories.LazerRepository;
import henrique.matheus.repositories.TaskRepository;
import henrique.matheus.repositories.TaskTimeBetweenRepository;
import henrique.matheus.services.TaskService;
import henrique.matheus.utils.Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

@Component
@Scope("prototype")
public class TasksReaderImpl {
    private String currentContext;
    private String currentCategory;
    private final TaskRepository taskRepository;
    private final TaskTimeBetweenRepository taskTimeBetweenRepository;
    private final LazerRepository lazerRepository;
    private final TaskService taskService;

    public TasksReaderImpl(TaskRepository taskRepository, TaskTimeBetweenRepository taskTimeBetweenRepository, LazerRepository lazerRepository, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskTimeBetweenRepository = taskTimeBetweenRepository;
        this.lazerRepository = lazerRepository;
        this.taskService = taskService;
        this.currentCategory = "";
        this.currentContext = "";
    }

    public void cleanCurrentInfo() {
        this.currentContext = "";
        this.currentCategory = "";
    }

    public void readLine(Path filename) {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(filename.toAbsolutePath().toString()));
            String line = bufferedReader.readLine();
            System.out.println("Initiating task reading...");

            while (line != null) {
                processLine(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            System.out.println("Task reading finished.");
            cleanCurrentInfo();
        } catch (StopReadingException e) {
            System.out.println("Task reading stopped.");
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processLine(String content) throws StopReadingException {
        if (content.isEmpty()) return;
        if (!content.contains(".")) return;
        if (content.contains("-STOP-")) throw new StopReadingException();

        if (content.contains("## **")) {
            currentContext = Utils.onlyAlphabetAndNumbers(content).trim();
            return;
        }

        if (currentContext.equals("Tasks.")) processTask(content);
        else if (currentContext.equals("Lazer.")) processLazer(content);
    }

    public void processTask(String content) {
        var contentOnlyAlphabet = Utils.removeXBrackets(content);
        contentOnlyAlphabet = Utils.onlyAlphabetAndNumbers(contentOnlyAlphabet).trim();
        if (content.contains("###")) {
            currentCategory = contentOnlyAlphabet;
            return;
        }

        //os numerozinhos lá, vão ser extintos, só vão poder ser inputados via terminal ou banco.
        if (content.contains("- [")) {
            var task = taskRepository.findByDescriptionAndDate(contentOnlyAlphabet, LocalDate.now());
            var taskCount = taskRepository.countByDate(LocalDate.now());
            if (task.isEmpty()) {
                var newTask = new TaskModel();
                newTask.setDescription(contentOnlyAlphabet);
                newTask.setCategory(currentCategory);
                newTask.setDate(LocalDate.now());
                newTask.setPosition(taskCount);
                taskRepository.save(newTask);
            } else {
                if (content.contains("[x]")) {
                    var taskTimes = taskTimeBetweenRepository.findByTaskAndTimeFinished(task.get(), null);
                    if (!task.get().getDone()) {
                        task.get().setDone(true);
                        System.out.println("Task marked as done!");
                        taskRepository.save(task.get());
                    }
                    if (taskTimes.isPresent()) {
                        taskService.mergeTimeBetween(task.get(), taskTimes.get());
                    }
                } else if (content.contains("- [ ]")) {
                    task.get().setDone(false);
                    taskRepository.save(task.get());
                }
            }
        }
    }

    public void processLazer(String content) {
        var lazerOnlyAlphabet = Utils.onlyAlphabetAndNumbers(content).trim();
        var lazer = lazerRepository.findByDescriptionAndDate(lazerOnlyAlphabet, LocalDate.now());
        if (lazer.isEmpty()) {
            var lazerCount = lazerRepository.countByDate(LocalDate.now());
            var newLazer = new LazerModel();
            newLazer.setDate(LocalDate.now());
            newLazer.setDescription(lazerOnlyAlphabet);
            newLazer.setPosition(lazerCount);
            lazerRepository.save(newLazer);
        }
    }
}
