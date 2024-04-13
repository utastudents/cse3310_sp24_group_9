package uta.cse3310;

import java.util.List;

public class MessageEvent {
    private String type;
    private String userName;
    private int UserID;
    private String buttonType;
    private String severName;
    private List<String> userList;
    private int getGameId;

    public MessageEvent(String type, String userName, boolean confirmButton) {
        this.type = type;
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public String getButtonType() {
        return buttonType;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserID() {
        return UserID;
    }

    public String getSeverName() {
        return severName;
    }

    public List<String> getUserList() {
        return userList;
    }

    public int getGameId() {
        return getGameId;
    }
}
