import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {
     private long totalTraffic;
     private LocalDateTime minTime;
     private LocalDateTime maxTime;

    public Statistics() {
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.MIN;
    }

    public void addEntry(LogEntry entry){
        totalTraffic += entry.getResponseSize();
        if (entry.getTime().isBefore(minTime)) minTime = entry.getTime();
        if (entry.getTime().isAfter(maxTime)) maxTime = entry.getTime();
    }

    public long getTrafficRate(){
        return totalTraffic / ChronoUnit.HOURS.between(minTime,maxTime);
    }


}
