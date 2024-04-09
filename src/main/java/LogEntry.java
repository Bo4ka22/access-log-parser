import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {
    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final String userAgent;


    public LogEntry(String str) {
        String[] tmp = disAssemble(str);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

        this.ipAddr = tmp[0];
        this.time = LocalDateTime.parse(tmp[1], formatter);
        this.method = HttpMethod.valueOf(tmp[2]);
        this.path = tmp[3];
        this.responseCode = Integer.parseInt(tmp[4]);
        this.responseSize = Integer.parseInt(tmp[5]);
        this.referer = tmp[6];
        this.userAgent = tmp[7];
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    //����� ��� ��������� User-Agent
    public static String getUserAgent(String str) {
        String[] res = str.split("\" \"");
        return res[res.length - 1];
    }


    //����� ��������� ������ � User-Agent � ������������ �, ������� ������ ����� � ������� �������
    public static String getBotTypeFromUserAgent(String str) {
        String[] tmp = str.split("compatible;");
        String processing;
        if (tmp.length >= 2) {
            processing = tmp[1].trim();
        } else processing = tmp[0].trim();
        String[] res = processing.split("/");
        return res[0];
    }

    //����� ��������� ������ userAgent � ���������� ������������ �������
    public static String getBrowserFromUserAgent(String str){
        String[] tmp1 = str.split(" ");
        String[] res = tmp1[tmp1.length-1].split("/");
        return res[0];
    }

    //����� ��������� ������ userAgent � ���������� ��
    public static String getOSFromUserAgent(String str){
        String[] tmp1 = str.split("\\(");
        String[] tmp2 = tmp1[1].split(";");
        String[] res = tmp2[0].split(" ");
        return res[0];
    }

    //����� ��������� ���������� ������ ���� �� �������� � ������������ �� ��������� ������� ��������� ���������. �������� ������, �� ��������
    public static String[] disAssemble(String str){
        String[] res = new String[8];
        String[] tmp = str.split("- -"); // �������� IP �����
        res[0] = tmp[0].trim(); // �������� IP � ������� ������� ������� ������
        String[] tmp2 = tmp[1].split("]"); // �������� ���� � ����� �������
        res[1] = tmp2[0].substring(2); // �������� ���� ������� � ������ ������
        String[] tmp21 = tmp2[1].split(" "); // �������� ����� �������
        res[2] = tmp21[1].substring(1); // �������� ����� � ������ ������
        String[] tmp3 = tmp2[1].split("\""); // �������� ���� �������
        res[3] = tmp21[2]; // �������� ���� ������� � ������ ������
        String[] tmp4 = tmp3[2].split(" "); // ������� �� ������� � �������� ��� ������ + ������ ������ � ������
        res[4] = tmp4[1]; // �������� ��� ������ � ������ ������
        res[5] = tmp4[2]; // �������� ������ ������ � ������ ������
        String[] tmp5 = tmp3[3].split("\""); // �������� ���� � ��������, � ������� ������� �� �������
        res[6] = tmp5[0]; // �������� ���� � �������� � ������ ������
        res[7] = getUserAgent(str);
        return res;
    }


    public static boolean isYandexBot(String str){
        return str.equals("YandexBot");
    }
    public static boolean isGoogleBot(String str){
        return str.equals("Googlebot");
    }

}
