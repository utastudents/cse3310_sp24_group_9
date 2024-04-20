package uta.cse3310;

import java.util.List;

public class MessageEvent {
    private String type;
    private String userName;
    private int UserID;
    private String buttonType;
    private String severName;
    private List<String> userList;
    private int gameId;
    private String word;
    private String message;
    private int x1;
    private int y1;
    private int x2;
    private int y2;

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
        return gameId;
    }

    public String getWord() {
        return word;
    }

    public String getMessage() {
        return message;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
}
