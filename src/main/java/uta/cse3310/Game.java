package uta.cse3310;

public class Game {

    public int gameId;
    public int[][] grid;
    public User[] users;
    public int timer;
    public int gameCount;
    public String chat;
    public int maxBoardSize;
    public Words[][] wordsBank;
    public boolean playable;
    public String title;
    public String message;

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
    public Game(User user){
        // TODO: implement
    }

    public void gameMenu(){
        // TODO: implement
    }

    public void gameTitle(){
        // TODO: implement
    }

    public void createGame(){
        // TODO: implement
    }

    public void displayTitle(String title){
        // TODO: implement
    }

    public void displayWordGrid(int grid[][]){
        // TODO: implement
    }

    public void updateWordGrid( Words words){
        // TODO: implement
    }

    public void displayWordBank(Words words){
        // TODO: implement
    }

    public void updateWordBank(Words words){
        // TODO: implement
    }

    public void DisplayChat(String chat){
        // TODO: implement
    }

    private void gameStart(User user[]){
        // TODO: implement
    }

    private void gameEnd(Words words, Words foundWords){
        // TODO: implement
    }
    
    private void leave(User id){
        // TODO: implement
    }

    public void updateScoreboard(User user) {
        // TODO: implement
    }

    public void displayScoreboard(User user){
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

    public void displayPlayers(){
        // TODO: implement
    }

    private void gameTimer(){
        // TODO: implement
    }

    public void hintWordGrid(Words words, char hint ){
        // TODO: implement
    }

    public void gameChat(String message){
        // TODO: implement
    }
}