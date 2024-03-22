package uta.cse3310;

public class Game {

    private int gameId;
    private int[][] grid;
    private User[] users;
    private int timer;
    private int gameCount;
    private String chat;
    private int maxBoardSize;
    private Words[][] wordsBank;
    private boolean playable;

    public Game() {
        this.gameId = 0;
        this.grid = new int[3][3];
        this.users = new User[2];
        this.timer = 0;
        this.gameCount = 0;
        this.chat = "";
        this.maxBoardSize = 3;
        this.wordsBank = new Words[3][3];
        this.playable = false;
    }

    private void Start() {
        // TODO: implement
    }

    private void End() {
        // TODO: implement
    }

    public void UpdateScore(User user) {
        // TODO: implement
    }

    public void wordFound(String word) {
        // TODO: implement

    }

    private void Wait() {
        // TODO: implement
    }

    public void WordFill() {
        // TODO: implement
    }

    public void DisplayPlayerInfo() {
        // TODO: implement
    }

    private void Timer() {
        // TODO: implement
    }

    private void Leave() {
        // TODO: implement
    }
}