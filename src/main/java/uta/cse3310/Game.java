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
    private String title;
    private String message;

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
    private Game(User user){
        // TODO: implement
    }

    private void gameMenu(){
        // TODO: implement
    }

    private void gameTitle(){
        // TODO: implement
    }

    private void createGame(){
        // TODO: implement
    }

    private void displayTitle(String title){
        // TODO: implement
    }

    private void displayWordGrid(int grid[][]){
        // TODO: implement
    }

    private void updateWordGrid( Words words){
        // TODO: implement
    }

    private void displayWordBank(Words words){
        // TODO: implement
    }

    private void updateWordBank(Words words){
        // TODO: implement
    }

    private void DisplayChat(String chat){
        // TODO: implement
    }

    private void gameStart(User user[]){
        // TODO: implement
    }

    private void gameEnd(Words words, Words foundWords){
        // TODO: implement
    }

    public void updateScoreboard(User user) {
        // TODO: implement
    }

    private void displayScoreboard(User user){
        // TODO: implement
    }

    public void checkWord(String foundword){
        // TODO: implement
    
    }

    private void gameWaiting(){
        // TODO: implement
    }

    public void WordFill(){
        // TODO: implement
    }

    public void displayPlayer(){
        // TODO: implement
    }

    private void gameTimer(){
        // TODO: implement
    }

    private void hintWordGrid(Words words, char hint ){
        // TODO: implement
    }

    private void gameChat(String message){
        // TODO: implement
    }
    private void Leave(){
        // TODO: implement
    }
}