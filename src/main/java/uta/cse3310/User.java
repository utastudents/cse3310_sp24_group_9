package uta.cse3310;
import java.util.ArrayList;
import com.google.gson.Gson;
public class User {
    private int ID;
    private int score;
    private String name;
    private colors color;
    private boolean ready;
    private boolean crown;
    ArrayList<String> foundWords = new ArrayList<>(); // This is a list of words the user has found
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
        this.crown = false;
    }
    //This second constructor is for use to use for testing, it less lines of repeated code
    public User(int ID, String name, colors color, int score){
        this.ID = ID;
        this.name = name;
        this.color = color;
        this.score = score;
        this.ready = false;
        this.crown = false;
    }
    /*
     * This method returns the name of the user
     * Json method provides more information but this could be easier for testing
     */
    public String getName(){
        return name;
    }

    public int getID(){
        return ID;
    }

    public boolean isReady(){
        return ready;
    }

    public boolean hasCrown(){
        return crown;
    }

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
     * if the player is not ready, then ready = true
     * this method adds functionality to switch from ready or not ready
     * there are no parameters because this is a button you can keep pressing
     */
    public void readyUp(){
        if(this.ready){ 
            this.ready = false;
        } else{this.ready = true;}
    }
    
    /*
     * Add the word to the players list of found words
     * It is a type string list because knowing the Word objects extra information is unecessary as game manages the validation
     * This method also updates the players score automatically using score = wordsfound * 5
     */
    public void updateUserWords(String foundWord){
        foundWords.add(foundWord);
        score += 1; // Changing this to a += makes the code fail, but if it remains =, it is ok? Test later, removed foundWords.size() b/c it'll do same thing w/o
    }
    /*
     * The game class will decide to pass on the crown to a specific player
     */
    public void userCrown(boolean crown){
        this.crown = crown;
    }
    /*
     * create a user json string, this makes printing the leaderboard information easy
     */
    public String userJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
