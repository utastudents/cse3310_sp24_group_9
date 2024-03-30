package uta.cse3310;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class Word{
    public int xCoordinate;
    public int yCoordinate;
    public String word;
    public boolean hasBeenFound;

    public Word(String word){
        this.word = word;
        this.hasBeenFound = false;
    }
    public void setCoordinates(int x, int y){
        this.xCoordinate = x;
        this.yCoordinate = y;
    }
    public void wordHasBeenFound(){
        this.hasBeenFound = true;
    }
    public String wordJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}