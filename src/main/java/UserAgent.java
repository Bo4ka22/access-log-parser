public class UserAgent {
    private final String osType;
    private final String browserType;

    public UserAgent(String userAgent) {

        this.osType = LogEntry.getOSFromUserAgent(userAgent);
        this.browserType = LogEntry.getBrowserFromUserAgent(userAgent);
    }

    public String getOsType() {
        return osType;
    }

    public String getBrowserType() {
        return browserType;
    }
}
