package uta.cse3310;

import java.util.List;

public class ServerEvent {
    public List<String> serverNames;
    public List<Boolean> readyStatus;
    public List<List<String>> usersList;

    public ServerEvent(List<String> serverNames, List<Boolean> readyStatus, List<List<String>> usersList){
        this.serverNames = serverNames;
        this.readyStatus = readyStatus;
        this.usersList = usersList;
    }
}
