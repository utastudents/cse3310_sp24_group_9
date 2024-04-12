package uta.cse3310;

public class ServerEvent {
    public Game game;
    public boolean ready;
    public User user;

    public ServerEvent(String severName, boolean ready, User user){
        this.game = null;
        this.ready = false;
        this.user = null;
    }
}
