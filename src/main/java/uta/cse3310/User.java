package uta.cse3310;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class User {
    public int ID;
    public int score;
    public String name;
    public colors color;
    public boolean ready;
    public boolean crown;
    ArrayList<String> foundWords = new ArrayList<>();
    
    /*
     * Creates a new user, this assumes game is providing a unique id (because this object doesnt manage other users)
     * The name parameter comes from the user setting one in the UI
     * Color parameter also assumes it is unique, the game class should know which ones are taken
     * This is a new player, so they are not ready and they havent won a game (so no crown)
     */
    public User(int ID, String name,colors color){
        this.ID = ID;
        this.name = name;
        this.color = color;
        this.score = 0;
        this.ready = false;
        this.crown = false;
    }
    public User(boolean b) {
        ready = b;
    }

    /*
     * This method returns the name of the user
     * Json method provides more information but this could be easier for testing
     */
    public String userName(){
        return name;
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
        score = foundWords.size() * 5;
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