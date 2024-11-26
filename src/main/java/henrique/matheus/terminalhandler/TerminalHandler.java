package henrique.matheus.terminalhandler;

import henrique.matheus.filewatcher.FileWatcher;
import henrique.matheus.models.ParametersModel;
import henrique.matheus.parameters.GlobalParameters;
import henrique.matheus.report.Report;
import henrique.matheus.repositories.ParametersRepository;
import henrique.matheus.repositories.TaskRepository;
import henrique.matheus.repositories.TaskTimeBetweenRepository;
import henrique.matheus.services.DayService;
import henrique.matheus.services.LazerService;
import henrique.matheus.services.TaskService;
import henrique.matheus.tasksreader.TasksReaderImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static henrique.matheus.utils.Utils.stringIsInteger;

@Component
public class TerminalHandler implements CommandLineRunner {
    private final DayService dayService;
    private final Report report;
    private final TasksReaderImpl tasksReader;
    private Thread watcherThread;
    private final TaskService taskService;
    private final TaskTimeBetweenRepository taskTimeBetweenRepository;
    private final TaskRepository taskRepository;
    private final LazerService lazerService;
    private final ParametersRepository parametersRepository;

    public TerminalHandler(DayService dayService,
                           Report report,
                           TasksReaderImpl tasksReader,
                           TaskService taskService,
                           TaskTimeBetweenRepository taskTimeBetweenRepository,
                           TaskRepository taskRepository,
                           LazerService lazerService,
                           ParametersRepository parametersRepository) {
        this.dayService = dayService;
        this.report = report;
        this.tasksReader = tasksReader;
        this.taskService = taskService;
        this.taskTimeBetweenRepository = taskTimeBetweenRepository;
        this.taskRepository = taskRepository;
        this.lazerService = lazerService;
        this.parametersRepository = parametersRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting application...");
        dayService.loadDay();
        checkParameters();
        fillGlobalParameters();
        verifyPrompt(args);
    }

    private void finalizeApplication() {
        System.out.println("Terminating application...");
        if (watcherThread != null)
            watcherThread.interrupt();
        System.exit(0);
    }

    public void scanPrompt() {
        System.out.println("Input a command");
        while (true) {
            String[] input = System.console().readLine().split(" ");
            if (verifyPrompt(input) == -1) break;
        }
    }

    public int verifyPrompt(String[] args) {
        if (args.length == 1) return verifySingleArgPrompt(args[0]);
        if (args.length > 1) return verifyMultiArgsPrompt(args);
        return -1;
    }

    private int verifySingleArgPrompt(String arg) {
        switch (arg) {
            case "report":
                report.showDayReport();
                break;
            case "quit":
                finalizeApplication();
                return -1;
            case "list":
                report.showTaskList();
                break;
            case "lazeres":
                report.showLazeresList();
                break;
            case "-w":
                startWatchDayReport();
                break;
            case "-sc":
                scanPrompt();
                break;
            case "":
                break;
            default:
                System.out.println("The option you provided doesn't exist!");
        }
        return 0;
    }

    private int verifyMultiArgsPrompt(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case "-w":
                    startWatchDayReport();
                    break;
                case "-sc":
                    scanPrompt();
                    break;
                case "-cf":
                    changeDailyTasksFolder(args[1]);
            }
        }

        if (args[0].equals("task") && stringIsInteger(args[1])) {
            var taskPosition = Integer.valueOf(args[1]);

            if (args.length == 2) {
                report.startTask(taskPosition);
                System.out.printf("Started task %d \n", taskPosition);
            }
            if (args.length == 3 && stringIsInteger(args[2])) {
                var minutes = Integer.valueOf(args[2]);

                taskService.addMinutes(taskPosition,
                        LocalDate.now(),
                        minutes);
                System.out.printf("Added more %d minutes to task %d \n", minutes, taskPosition);
            }
            if (args.length == 3 && args[2].equals("stop")) {
                var task = taskRepository.findByDateAndPosition(LocalDate.now(), taskPosition);
                if (task.isEmpty()) return -1;
                var taskTime = taskTimeBetweenRepository.findByTaskAndTimeFinished(task.get(), null);
                if (taskTime.isEmpty()) return -1;

                taskService.mergeTimeBetween(task.get(), taskTime.get());
                System.out.printf("Stopped task %d \n", taskPosition);
            }
        }

        if (args[0].equals("lazer") && stringIsInteger(args[1])) {
            var lazerPosition = Integer.valueOf(args[1]);

            if (args.length == 3 && stringIsInteger(args[2])) {
                var minutes = Integer.valueOf(args[2]);

                lazerService.addMinutes(lazerPosition,
                        LocalDate.now(),
                        minutes);
                System.out.printf("Added more %d minutes to lazer %d \n", minutes, lazerPosition);
            }
        }
        return 0;
    }

    private void startWatchDayReport() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String fileName = LocalDate.now().format(formatter) + ".md";
            watcherThread = new Thread(() -> {
            FileWatcher.watch(fileName, tasksReader::readLine);
        });
        watcherThread.start();
    }

    private void checkParameters() {
        var parameterModel = parametersRepository.findFirstByOrderByIdAsc();
        if (parameterModel.isEmpty()) {
            var parameter = new ParametersModel();
            parameter.setDailyTasksFolder("/home/matheus/Documents/Organização pessoal/Daily tasks");
            parametersRepository.save(parameter);
        }
    }

    private void changeDailyTasksFolder(String newPath) {
        var parameterModel = parametersRepository.findFirstByOrderByIdAsc();
        if (parameterModel.isPresent()) {
            parameterModel.get().setDailyTasksFolder(newPath);
            parametersRepository.save(parameterModel.get());
            System.out.printf("Changed daily tasks folder to: %s from %s \n",
                    newPath,
                    GlobalParameters.dailyTasksFolder);
        }
    }

    private void fillGlobalParameters() {
        var parameterModel = parametersRepository.findFirstByOrderByIdAsc();
        if (parameterModel.isPresent()) {
            GlobalParameters.dailyTasksFolder = parameterModel.get().getDailyTasksFolder();
            GlobalParameters.tasksFolder = Paths.get(
                    GlobalParameters.dailyTasksFolder , LocalDate.now().format(DateTimeFormatter.ofPattern("MMyyyy"))
            );
        }
    }
}
