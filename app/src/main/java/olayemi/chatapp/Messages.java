package olayemi.chatapp;

public class Messages {



    private String DisplayName;
    private String Message;
    public String DateTime;

    public Messages(String displayName, String message, String dateTime) {
        DisplayName = displayName;
        Message = message;
        DateTime = dateTime;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public String getMessage() {
        return Message;
    }

    public String getDateTime() {
        return DateTime;
    }



}
