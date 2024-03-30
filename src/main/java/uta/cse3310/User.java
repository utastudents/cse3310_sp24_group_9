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
    //public Word playerWord;
    public boolean crown;
    private ArrayList<Word> foundWords = new ArrayList<>();

    public User(int ID, String name,colors color){
        this.ID = ID;
        this.name = name;
        this.color = color;
        this.score = 0;
        this.ready = false;
        this.crown = false;
    }
    
    public String userName(){
        return name;
    }
    public void readyUp(boolean ready){
        if(!this.ready){ // if the player is not ready, then ready = true
            this.ready = false;
        } else{this.ready = true;} // this line adds functionality to switch from ready or not ready
    }
    public void updateUserWords(Word foundWord){
        foundWords.add(foundWord);
        score = foundWords.size() * 5;
    }   
    public void userCrown(boolean crown){
        this.crown = crown;
    }
    public String userJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
