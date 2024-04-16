package uta.cse3310;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WordBank {

  private int MAXWORDS = 50; //this value is only for testing purposes, maxwords is still tbd
  private ArrayList<String> Words = new ArrayList<>(); //This holds every word in the file, we use this for filling the hashmap
  private Random random = new Random();
  private float density = 0;
  private float characters = 0;
  private boolean playable; 
  /*
   * Try and catch for invalid files or filepaths
   * Validate that every word is at least 3 letters long
   */
  public WordBank(String filename) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = br.readLine()) != null) { //while there are more lines
        line = line.trim(); //get rid of the whitespace
        if (line.length() < 3) { //ensure the word is at least 3 letters long
          continue;
        } else { //create a new word object and add it to our word list
          //Word word = new Word(line);
          Words.add(line);
        }
      }
    } catch (IOException e) {
      System.err.println("Unable to open file or wrong file path");
    }
  }
   
  float getDensity(){
    return density = characters / (MAXWORDS * MAXWORDS);
  }
  String getRandomWord(){
    int randomIndex = 0;
    randomIndex = random.nextInt(Words.size()); //this generates a random integer from 0(inclusive) to arraylist.size()(exclusive)
    String randomWord = Words.get(randomIndex); //get the word from the random index
    characters += randomWord.length();
    return randomWord;

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
