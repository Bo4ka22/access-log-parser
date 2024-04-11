import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Statistics {
     private long totalTraffic;
     private LocalDateTime minTime;
     private LocalDateTime maxTime;
     private HashSet<String> referers = new HashSet<>();

     private  HashMap<String, Integer> osCounter = new HashMap<>();

    public Statistics() {
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.MIN;
    }

    public void addEntry(LogEntry entry){
        totalTraffic += entry.getResponseSize();
        if (entry.getTime().isBefore(minTime)) minTime = entry.getTime();
        if (entry.getTime().isAfter(maxTime)) maxTime = entry.getTime();
        if (entry.getResponseCode()==200) referers.add(entry.getReferer());

        //���������, ���� �� � ���� ������ ��. ���� ����, �� ���������� ������� �������������. ���� ���, �� ��������� � � 1 ��������������
        if (!osCounter.containsKey(LogEntry.getOSFromUserAgent(entry.getUserAgent()))){
            osCounter.put(LogEntry.getOSFromUserAgent(entry.getUserAgent()), 1);
        } else {
            osCounter.put(LogEntry.getOSFromUserAgent(entry.getUserAgent()), osCounter.get(LogEntry.getOSFromUserAgent(entry.getUserAgent()))+1);
        }
    }

    public long getTrafficRate(){
        return totalTraffic / ChronoUnit.HOURS.between(minTime,maxTime);
    }

    public HashSet<String> getReferers(){
        return new HashSet<>(referers);
    }

    public HashMap<String,Double> getOSUsage(){
        int osTotal = 0;
        HashMap<String, Double> res = new HashMap<>();
        List<Integer> values = new ArrayList<>(osCounter.values());
        for (Integer integer : values) {
            osTotal += integer;
        }
        List<String> keys = new ArrayList<>(osCounter.keySet());
        for (String key: keys) {
            res.put(key, (double)osCounter.get(key) / osTotal);
        }
        return res;
    }



}