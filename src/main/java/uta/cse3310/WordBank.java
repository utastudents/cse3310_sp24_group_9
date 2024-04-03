package uta.cse3310;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WordBank {

  private int MAXWORDS = 50; //this value is only for testing purposes, maxwords is still tbd
  private ArrayList<Word> Words = new ArrayList<>(); //This holds every word in the file, we use this for filling the hashmap
  private HashMap<String, Word> wordBankMap = new HashMap<>(MAXWORDS); //the key is the String word and the value is the word object
  private char[][] grid = new char[MAXWORDS][MAXWORDS];
  private WordBank wordsBank;
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
  public void setRandomWords() {
    int randomIndex = 0; //this holds the current random index
    Random random = new Random(); //this object will generate a random int for the index
    Word randomWord; //the random word that will be inserted into the hashmap
    for (int i = 0; i < MAXWORDS; i++) {
      randomIndex = random.nextInt(Words.size()); //this generates a random integer from 0(inclusive) to arraylist.size()(exclusive)
      randomWord = Words.get(randomIndex); //get the word from the random index
      wordBankMap.put(randomWord.word, randomWord); //the hashmap uses the word string as the key and the word object as the value, this makes it easy to remove words
      Words.remove(randomIndex); //remove the word from the arraylist so that there are no duplicates
    }
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

  public void WordFill() {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        grid[i][j] = ' ';
      }
    }

    for (Map.Entry<String, Word> entry : wordBankMap.entrySet()) {
      String word = entry.getKey();
      Word wordObj = entry.getValue();
      boolean placed = false;
      Random random = new Random();
      int rev = random.nextInt(2);
      if (rev == 0) {
        word = new StringBuilder(word).reverse().toString();
      }
      while (!placed) {
        int variation = random.nextInt(5);
        if (variation == 0) {
          placed = fillHorizontal(word);
        } else if (variation == 1) {
          placed = fillVertical(word);
        } else if (variation == 2) {
          placed = fillDiagonalDown(word);
        } else if (variation == 3) {
          placed = fillDiagonalUp(word);
        } else if (variation == 4) {
          placed = fillVerticalUp(word);
        }
      }
    }
    extraLetters();
  }

  public boolean fillHorizontal(String word) {
    int wordLength = word.length();
    Random random = new Random();

    int row = random.nextInt(grid.length);
    int col = random.nextInt(grid[0].length - wordLength + 1);

    if (col + wordLength > grid[0].length) {
      return false;
    }

    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row][col + i];
      char wordChar = word.charAt(i);
      if (currentChar != ' ' && currentChar != wordChar) {
        return false;
      }
    }

    for (int i = 0; i < wordLength; i++) {
      grid[row][col + i] = word.charAt(i);
    }

    return true;
  }

  public boolean fillVertical(String word) {
    int wordLength = word.length();
    Random random = new Random();

    int row = random.nextInt(grid.length - wordLength + 1);
    int col = random.nextInt(grid[0].length);

    if (col + wordLength > grid.length) {
      return false;
    }

    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row + i][col];
      char wordChar = word.charAt(i);
      if (currentChar != ' ' && currentChar != wordChar) {
        return false;
      }
    }

    for (int i = 0; i < wordLength; i++) {
      grid[row + i][col] = word.charAt(i);
    }

    return true;
  }

  public boolean fillDiagonalDown(String word) {
    int wordLength = word.length();
    Random random = new Random();

    int row = random.nextInt(grid.length - wordLength + 1);
    int col = random.nextInt(grid[0].length - wordLength + 1);

    if ((col + wordLength) > grid.length || (row + wordLength) > grid.length) {
      return false;
    }

    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row + i][col + i];
      char wordChar = word.charAt(i);
      if (currentChar != ' ' && currentChar != wordChar) {
        return false;
      }
    }

    for (int i = 0; i < wordLength; i++) {
      grid[row + i][col + i] = word.charAt(i);
    }

    return true;
  }

  public boolean fillDiagonalUp(String word) {
    int wordLength = word.length();
    Random random = new Random();

    int row = random.nextInt(grid.length - wordLength + 1);
    int col = random.nextInt(grid[0].length - wordLength + 1);

    if (
      (col + wordLength) > grid.length ||
      (row + wordLength) > grid.length ||
      (row - grid.length) <= 0
    ) {
      return false;
    }

    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row - i][col + i];
      char wordChar = word.charAt(i);
      if (currentChar != ' ' && currentChar != wordChar) {
        return false;
      }
    }

    for (int i = 0; i < wordLength; i++) {
      grid[row - i][col + i] = word.charAt(i);
    }

    return true;
  }

  public boolean fillVerticalUp(String word) {
    int wordLength = word.length();
    Random random = new Random();

    int row = random.nextInt(grid.length - wordLength + 1);
    int col = random.nextInt(grid[0].length);

    if (col + wordLength > grid.length || (row - grid.length) < 0) {
      return false;
    }

    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row - i][col];
      char wordChar = word.charAt(i);
      if (currentChar != ' ' && currentChar != wordChar) {
        return false;
      }
    }

    for (int i = 0; i < wordLength; i++) {
      grid[row - i][col] = word.charAt(i);
    }

    return true;
  }

  public void extraLetters() {
    Random random = new Random();
    for(int i = 0; i < grid.length; i++) {
      for(int j = 0; j < grid.length; j++) {
        char currentChar = grid[i][j];
        if(currentChar == ' '){
          char randomLetter = (char) (random.nextInt(26) + 'a');
          grid[i][j] = randomLetter;
        }
      }
    }
  }

  public void printGrid() {
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        System.out.print(grid[i][j] + " ");
      }
      System.out.println();
    }
  }
}

