package uta.cse3310;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class Word{
    public int xOne;
    public int yOne;
    public int xTwo;
    public int yTwo;
    public String word;

    public Word(String word){
        this.word = word;
    }
    public void setStart(int x, int y){
        this.xOne = x;
        this.yOne = y;
    }
    public void setEnd(int x, int y){
        this.xTwo = x;
        this.yTwo = y;
    }
    public int Length(){
        return word.length();
    }
    public String wordJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}