package uta.cse3310;
public class Word{
    public int xCoordinate;
    public int yCoordinate;
    public String word;
    public boolean hasBeenFound;

    public Word(String word){
        this.word = word;
        hasBeenFound = false;
    }
    public void setCoordinates(int x, int y){
        this.xCoordinate = x;
        this.yCoordinate = y;
    }
    @Override
    public String toString(){
        return word;
    }
}