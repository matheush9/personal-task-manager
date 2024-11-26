package henrique.matheus.timeutils;

import java.time.Duration;

public class TimeUtils {
    public static String formatDuration(Duration duration) {
        return String.format("%sh%sm%ss",
            duration.toHoursPart(),
            duration.toMinutesPart(),
            duration.toSecondsPart());
    }
}
