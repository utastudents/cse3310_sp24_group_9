package uta.cse3310;

import java.util.List;

public class ServerEvent {
    public List<Integer> serverIDs;
    public List<String> serverNames;
    public List<Boolean> readyStatus;
    public List<List<String>> usersLists;
    public List<List<String>> userReadyLists;

    /*
     * Method ServerEvent() will list out all of the 
     * servers with the respective List data that was
     * set within the method.
     */
    public ServerEvent(List<Integer> severIDs, List<String> serverNames, List<Boolean> readyStatus, List<List<String>> usersLists, List<List<String>> userReadyLists){
        this.serverIDs = severIDs;
        this.serverNames = serverNames;
        this.readyStatus = readyStatus;
        this.usersLists = usersLists;
        this.userReadyLists = userReadyLists;
    }
}
