package uta.cse3310;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WordBank {

  private int MAXWORDS = 35; //this value is only for testing purposes, maxwords is still tbd
  ArrayList<String> Words = new ArrayList<>(); //This holds every word in the file, we use this for filling the hashmap
  private Random random = new Random();
  private float density = 0;
  private float characters = 0;

  /*
   * WordBank() has a try and catch for invalid files or filepaths.
   * While there are more lines, validate that every word is at least 
   * 3 letters long and add that word to the list of words.
   */
  public WordBank(String filename) throws IOException{
    try(BufferedReader br = new BufferedReader(new FileReader(filename))){
      String line;
      while((line = br.readLine()) != null){
        line = line.trim();
        if(line.length() < 3){
          continue;
        }else{
          //Word word = new Word(line);
          Words.add(line);
        }
      }
    }catch (IOException e){
      System.err.println("Unable to open file or wrong file path");
    }
  }
  

  float getDensity(){
    density = characters / (MAXWORDS * MAXWORDS);
    return density;
  }

  String getRandomWord(){
    int randomIndex = 0;
    randomIndex = random.nextInt(Words.size()); // This generates a random integer from 0 (inclusive) to arraylist.size() (exclusive)
    String randomWord = Words.get(randomIndex); // Get the word from the random index
    characters += randomWord.length();
    return randomWord;

  }

  public float characterCount(){
    return characters;
  }

  public int hashCode(int x1, int y1, int x2, int y2){
    int result = Integer.hashCode(x1);
    result = 31 * result + Integer.hashCode(y1);
    result = 31 * result + Integer.hashCode(x2);
    result = 31 * result + Integer.hashCode(y2);
    return result;
  }
  
  public boolean placedWord(String word){
    return Words.remove(word);
  }
}
