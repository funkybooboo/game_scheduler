package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IceTime implements Comparable<IceTime>{
    public String date;
    public String time;
    public boolean isEarly;
    public LocalDateTime dateTime;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");

    public IceTime(String dateString) {
        String[] parts = dateString.split(" ");
        this.dateTime = LocalDateTime.parse(dateString, formatter);
        this.date = parts[0];
        this.time = parts[1];
        int hour = Integer.parseInt(this.time.split(":")[0]);
        int minute = Integer.parseInt(this.time.split(":")[1]);
        this.isEarly = isEarlyGame(hour, minute);
    }

    private boolean isEarlyGame(int hour, int minute) {
        if (hour < 20) {
            return true;
        }
        return switch (hour) {
            case 20 -> true;
            case 21 -> minute <= 15;
            default -> false;
        };
    }

    public String toString() {
        return """
                Date: %s
                Time: %s
                Early Game: %s
                """.formatted(date, time, isEarly);
    }

    @Override
    public int compareTo(IceTime other) {
        if (this.dateTime.isBefore(other.dateTime)) {
            return -1;
        } else if (this.dateTime.isAfter(other.dateTime)) {
            return 1;
        } else {
            return 0;
        }
    }
}
