package CodeConnect.CodeConnect.converter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static final String yy_MM_dd_HH_mm_ss = "yy/MM/dd HH:mm:ss";
    private static final String HH_mm = "HH:mm";

    public static String formatTimeAgo(String currentDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(yy_MM_dd_HH_mm_ss);
        LocalDateTime timestamp = LocalDateTime.parse(currentDateTime, formatter);
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(timestamp, now);

        if (duration.toMinutes() < 1) {
            return "방금";
        } else if (duration.toHours() < 1) {
            long minutes = duration.toMinutes();
            return minutes + "분 전";
        } else if (duration.toDays() < 1) {
            long hours = duration.toHours();
            return hours + "시간 전";
        } else if (duration.toDays() < 30) {
            long days = duration.toDays();
            return days + "일 전";
        } else if (duration.toDays() < 365) {
            long months = duration.toDays() / 30;
            return months + "달 전";
        } else {
            long years = duration.toDays() / 365;
            return years + "년 전";
        }
    }

    public static String changeDateTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(yy_MM_dd_HH_mm_ss);
        return dateTime.format(formatter);
    }

    public static String changeChatTimeFormat(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(HH_mm);
        return dateTime.format(formatter);
    }

}
