package uta.cse3310;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class Word{
    public int xOne;
    public int yOne;
    public int xTwo;
    public int yTwo;
    public String word;
    /*
     * Every valid line in the text file creates a word
     * No validation is needed from the word class, because the word bank does that
     */
    public Word(String word){
        this.word = word;
    }
    /*
     * Once we know that a word can fit in the word grid, we set the coordinates for where the word starts and ends
     * This can make it easier to keep track of what spaces in the word grid are open
     * This functionality can be flexible, we can find another way to keep track of the open spaces is this doesnt work
     */
    public void setStart(int x, int y){
        this.xOne = x;
        this.yOne = y;
    }
    /*
     * Once we know that a word can fit in the word grid, we set the coordinates for where the word starts and ends
     * This can make it easier to keep track of what spaces in the word grid are open
     * This functionality can be flexible, we can find another way to keep track of the open spaces is this doesnt work
     */
    public void setEnd(int x, int y){
        this.xTwo = x;
        this.yTwo = y;
    }
    /*
     * This method returns the length of the word
     * This is useful for determining if the word can fit in the word grid
     * EX: if there are 5 consecutive open spaces in row 5 and this word is 3 letters long, we can fit the word
     */
    public int Length(){
        return word.length();
    }
    /*
     * Information of the word in json format
     */
    public String wordJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}