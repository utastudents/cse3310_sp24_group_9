package uta.cse3310;
import java.util.ArrayList;
import com.google.gson.Gson;
public class User {
    private int ID;
    private int score;
    private String name;
    private colors color;
    private boolean ready;
    // private boolean crown;
    ArrayList<String> foundWords = new ArrayList<>();
    
    /*
     * Creates a new user, this assumes game is providing a unique id (because this object doesnt manage other users)
     * The name parameter comes from the user setting one in the UI
     * Color parameter also assumes it is unique, the game class should know which ones are taken
     * This is a new player, so they are not ready and they havent won a game (so no crown)
     */
    public User(int ID, String name){
        this.ID = ID;
        this.name = name;
    }

    public User(int ID, String name,colors color){
        this.ID = ID;
        this.name = name;
        this.color = color;
        this.score = 0;
        this.ready = false;
        // this.crown = false;
    }

    /*
     * The second User constructor is for use for testing.
     * This constructor has less lines of repeated code
     */
    public User(int ID, String name, colors color, int score){
        this.ID = ID;
        this.name = name;
        this.color = color;
        this.score = score;
        this.ready = false;
        // this.crown = false;
    }

    public String getName(){
        return name;
    }

    public int getID(){
        return ID;
    }

    public boolean isReady(){
        return ready;
    }

    // public boolean hasCrown(){
    //     return crown;
    // }

    // public void userCrown(boolean crown){
    //     this.crown = crown;
    // }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public colors getColor(){
        return color;
    }

    /*
     * Method readyUp() will check if the player is not ready,
     * then set their ready status to true. The method will switch
     * the status based on what button is pressed.
     */
    public void readyUp(){
        if(this.ready){ 
            this.ready = false;
        }else{this.ready = true;}
    }
    
    /*
     * Method updateUserWords() will add the word to the players list
     * of found words. It is a ArrayList of string that adds any found words
     * to the list, as well as updating the players score automatically by 1 every time.
     */
    public void updateUserWords(String foundWord){
        foundWords.add(foundWord);
        score += 1;
    }

    /*
     * Method userJson() will create a user JSON string.
     * This will make printing the leaderboard information
     * and other necessary data easier.
     */
    public String userJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    
}
