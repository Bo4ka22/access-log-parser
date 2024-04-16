import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
     private HashMap<LocalDateTime, Integer> visitsEachSecond = new HashMap<>();
     private HashSet<String> domainsList = new HashSet<>();
     private HashMap<String, Integer> visitsByOnePerson = new HashMap<>();

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

        //���������, ���� �� � ���� ������ ��. ���� ����, �� ���������� ������� �������������. ���� ���, �� ��������� � � 1 ��������������
        if (!osCounter.containsKey(LogEntry.getOSFromUserAgent(entry.getUserAgent()))){
            osCounter.put(LogEntry.getOSFromUserAgent(entry.getUserAgent()), 1);
        } else {
            osCounter.put(LogEntry.getOSFromUserAgent(entry.getUserAgent()), osCounter.get(LogEntry.getOSFromUserAgent(entry.getUserAgent()))+1);
        }

        //�������� ��� path, � ������� ��� ������ 404
        if (entry.getResponseCode()==404) pathNotFound.add(entry.getPath());

        //���������, ���� �� � ���� ������ �������. ���� ����, �� ���������� ������� �������������. ���� ���, �� ��������� ��� � 1 ��������������
        if (!browsersCounter.containsKey(LogEntry.getBrowserFromUserAgent(entry.getUserAgent()))){
            browsersCounter.put(LogEntry.getBrowserFromUserAgent(entry.getUserAgent()), 1);
        } else {
            browsersCounter.put(LogEntry.getBrowserFromUserAgent(entry.getUserAgent()), browsersCounter.get(LogEntry.getBrowserFromUserAgent(entry.getUserAgent()))+1);
        }

        if (!agent.isBot()) {
            //����������� ������� �������� �����������
            realVisitsCounter++;

            //��������� map ��������� �� ������ �������.
            if (!visitsEachSecond.containsKey(entry.getTime())){
                visitsEachSecond.put(entry.getTime(), 1);
            } else {
                visitsEachSecond.put(entry.getTime(), visitsEachSecond.get(entry.getTime())+1);
            }

            //��������� map ��������� ��� ������� ip ������.
            if (!visitsByOnePerson.containsKey(entry.getIpAddr())){
                visitsByOnePerson.put(entry.getIpAddr(), 1);
            } else {
                visitsByOnePerson.put(entry.getIpAddr(), visitsByOnePerson.get(entry.getIpAddr())+1);
            }

        }

        //������� ���������� ������� � �������� 4�� � 5��
        if(entry.getResponseCode() >= 400 && entry.getResponseCode() < 600){
            errorResponsesCounter++;
        }

        //�������� ���������� ip ������
        uniqueAddresses.add(entry.getIpAddr());

        //�������� ������ referer'��
        if (entry.getReferer() != null){
            String[] tmp = entry.getReferer().split("//");
            if (tmp.length > 1){
                String[] tmp2 = tmp[1].split("/");
                domainsList.add(tmp2[0]);
            }
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

    public int getHighestVisitsPerSecond(){
        return Collections.max(visitsEachSecond.entrySet(), Map.Entry.comparingByValue()).getValue();
    }

    public HashSet<String> getDomains(){
        return new HashSet<>(domainsList);
    }

    public int getMaximumVisitsByOnePerson(){
        return Collections.max(visitsByOnePerson.entrySet(), Map.Entry.comparingByValue()).getValue();
    }

}
