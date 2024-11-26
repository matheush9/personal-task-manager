package henrique.matheus.parameters;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GlobalParameters {
    public static final Duration finalTime = Duration.ofHours(23).plusMinutes(25);
    public static final Duration timeSlept = Duration.ofHours(8);
    public static final Duration lunchTime = Duration.ofMinutes(35);
    public static final Duration showerTime = Duration.ofMinutes(25);
    public static final Duration dinnerTime = lunchTime;
    public static final Duration breakFastTime = Duration.ofMinutes(30);
    public static final Duration supperTime = Duration.ofMinutes(15);
    public static final Duration snackTime = Duration.ofMinutes(30);
    public static final Duration workTime = Duration.ofHours(9).plusMinutes(10);
    public static String dailyTasksFolder = "";
    public static Path tasksFolder = Paths.get(
            dailyTasksFolder, LocalDate.now().format(DateTimeFormatter.ofPattern("MMyyyy"))
    );
}