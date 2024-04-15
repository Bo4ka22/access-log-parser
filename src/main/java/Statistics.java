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

     private HashSet<String> pathNotFound = new HashSet<>();
     private HashMap<String, Integer> browsersCounter = new HashMap<>();
     private int realVisitsCounter;

     private int errorResponsesCounter;

     private HashSet<String> uniqueAddresses = new HashSet<>();

    public Statistics() {
        this.minTime = LocalDateTime.now();
        this.maxTime = LocalDateTime.MIN;
    }

    public void addEntry(LogEntry entry){
        totalTraffic += entry.getResponseSize();
        if (entry.getTime().isBefore(minTime)) minTime = entry.getTime();
        if (entry.getTime().isAfter(maxTime)) maxTime = entry.getTime();
        if (entry.getResponseCode()==200) referers.add(entry.getReferer());
        UserAgent agent = new UserAgent(entry.getUserAgent());

        //Проверяем, есть ли в мапе данная ОС. Если есть, то прибавляем единицу использований. Если нет, то добавляем её с 1 использованием
        if (!osCounter.containsKey(LogEntry.getOSFromUserAgent(entry.getUserAgent()))){
            osCounter.put(LogEntry.getOSFromUserAgent(entry.getUserAgent()), 1);
        } else {
            osCounter.put(LogEntry.getOSFromUserAgent(entry.getUserAgent()), osCounter.get(LogEntry.getOSFromUserAgent(entry.getUserAgent()))+1);
        }

        //Собираем все path, у которых код ответа 404
        if (entry.getResponseCode()==404) pathNotFound.add(entry.getPath());

        //Проверяем, есть ли в мапе данный браузер. Если есть, то прибавляем единицу использований. Если нет, то добавляем его с 1 использованием
        if (!browsersCounter.containsKey(LogEntry.getBrowserFromUserAgent(entry.getUserAgent()))){
            browsersCounter.put(LogEntry.getBrowserFromUserAgent(entry.getUserAgent()), 1);
        } else {
            browsersCounter.put(LogEntry.getBrowserFromUserAgent(entry.getUserAgent()), browsersCounter.get(LogEntry.getBrowserFromUserAgent(entry.getUserAgent()))+1);
        }

        //Проверяем кто посещал: если реальный браузер, то добавляем к счётчику. Если бот - игнорируем
        if (!agent.isBot()) realVisitsCounter++;

        //Считаем количество ответов с ошибками 4хх и 5хх
        if(entry.getResponseCode() >= 400 && entry.getResponseCode() < 600){
            errorResponsesCounter++;
        }

        //Собираем уникальные ip адреса
        uniqueAddresses.add(entry.getIpAddr());


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

    public HashMap<String, Double> getBrowsersUsage(){
        int browsersTotal = 0;
        HashMap<String, Double> res = new HashMap<>();
        List<Integer> values = new ArrayList<>(browsersCounter.values());
        for (Integer integer : values) {
            values.size();
            browsersTotal += integer;
        }
        List<String> keys = new ArrayList<>(browsersCounter.keySet());
        for (String key: keys) {
            res.put(key, (double)browsersCounter.get(key) / browsersTotal);
        }
        return res;
    }

    public HashSet<String> getPathNotFound(){
        return new HashSet<>(pathNotFound);
    }

    public double getAverageVisitsPerHour(){
        return (double) realVisitsCounter / ChronoUnit.HOURS.between(minTime,maxTime);
    }

    public double getErrorResponsesPerHour(){
        return (double) errorResponsesCounter / ChronoUnit.HOURS.between(minTime,maxTime);
    }

    public double getAverageVisitsByOnePerson(){
        return (double) realVisitsCounter / uniqueAddresses.size();
    }


}
