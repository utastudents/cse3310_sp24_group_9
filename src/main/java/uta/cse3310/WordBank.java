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
  private ArrayList<Word> Words = new ArrayList<>(); //This holds every word in the file, we use this for filling the hashmap
  public HashMap<String, Word> wordBankMap = new HashMap<>(MAXWORDS); //the key is the String word and the value is the word object
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
          Word word = new Word(line);
          Words.add(word);
        }
      }
    } catch (IOException e) {
      System.err.println("Unable to open file or wrong file path");
    }
  }

  /*
   * Every word bank should be unique
   * This method randomly adds a word from Words arraylist to the hashmap the game will use
   * The hashmap is of size MAXWORDS (temporarily set to 50 for testing)
   */
  public int setRandomWords() {
    int randomIndex = 0; //this holds the current random index
    Random random = new Random(); //this object will generate a random int for the index
    Word randomWord; //the random word that will be inserted into the hashmap
    int maxwords = 0;
    int characters = 0;

    int found_density = 0;

    while (found_density <= 0.67) {
      randomIndex = random.nextInt(Words.size()); //this generates a random integer from 0(inclusive) to arraylist.size()(exclusive)
      randomWord = Words.get(randomIndex); //get the word from the random index
      characters = characters + (randomWord.Length());
      maxwords = maxwords + 1;
      wordBankMap.put(randomWord.word, randomWord); //the hashmap uses the word string as the key and the word object as the value, this makes it easy to remove words
      Words.remove(randomIndex); //remove the word from the arraylist so that there are no duplicates
      found_density = characters / (MAXWORDS * MAXWORDS);
    }
    return found_density;
  }

  /*
   * This method returns the size of the hashmap
   * This makes it easy to determine when to end the game
   * While(game1.wordsLeft() > 0) continue the game while there are still words
   */
  public int wordsLeft() {
    return wordBankMap.size();
  }

  /*
   * The hashmap makes it incredibly fast to remove a word from the available words
   * Instead of looping through every word to see if theres a match we simply wordBankMap.remove("hello")
   * If there is no match, then nothing happens to the list
   */
  public void removeWord(Word wordToRemove) {
    String keyWord = wordToRemove.word;
    wordBankMap.remove(keyWord);
  }
}
