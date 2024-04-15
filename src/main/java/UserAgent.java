public class UserAgent {
    private final String osType;
    private final String browserType;
    private boolean isBot;

    public UserAgent(String userAgent) {

        this.osType = LogEntry.getOSFromUserAgent(userAgent);
        this.browserType = LogEntry.getBrowserFromUserAgent(userAgent);
        this.isBot = userAgent.contains("bot");
    }

    public String getOsType() {
        return osType;
    }

    public String getBrowserType() {
        return browserType;
    }

    public boolean isBot(){
        return isBot;
    }
}
