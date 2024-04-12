package uta.cse3310;

public class MessageEvent {
    private String type;
    private String userName;
    private String buttonType;


    public MessageEvent(String type, String userName, boolean confirmButton) {
        this.type = type;
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public String getButtonType(){
        return buttonType;
    }

    public String getUserName() {
        return userName;
    }
}
