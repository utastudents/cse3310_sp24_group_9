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


public class WordGrid {

  private int MAXWORDS = 50;
  public char[][] grid = new char[MAXWORDS][MAXWORDS]; // This is the grid to be filled
  private WordBank wordsBank; // to create instance of WordBank

  public WordGrid() {
    try {
      this.wordsBank = new WordBank("Data/words.txt"); // create an instance of WordBank
    } catch (IOException e) {
      // Handle the exception by printing an error message
      System.err.println("Error reading words file: " + e.getMessage());
    }
  }

  //method to fill the grid with words
  public void WordFill() {
    //initialize the grid with empty characters
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        grid[i][j] = ' ';
      }
    }

    this.wordsBank.setRandomWords(); //call the method in WordBank to populate HashMap with words
    HashMap<String, Word> wordBankMap = this.wordsBank.wordBankMap; //// Retrieve the populated word bank map
    
    // Iterate through each word in the word bank map
    for (Map.Entry<String, Word> entry : wordBankMap.entrySet()) {
      String word = entry.getKey(); //Get the keys which are the words
      boolean placed = false;

      // Shuffle the list of variations for randomness
      List<Integer> variations = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4));
      Collections.shuffle(variations);

      // Iterate through each variation in the variations array
      for (int variation : variations) {
        // Check the type of variation
        if (variation == 0) { // Horizontal filling
          // Try to fill the word horizontally up to 100 times
          for (int i = 0; i < 100; i++) {
            placed = fillHorizontal(word);
            // If word is successfully placed, exit the loop
            if (placed) break;
          }
        } else if (variation == 1) { // Vertical filling
          // Try to fill the word vertically up to 100 times
          for (int i = 0; i < 100; i++) {
            placed = fillVertical(word);
            // If word is successfully placed, exit the loop
            if (placed) break;
          }
        } else if (variation == 2) { // Diagonal down filling
          // Try to fill the word diagonally down up to 100 times
          for (int i = 0; i < 100; i++) {
            placed = fillDiagonalDown(word);
            // If word is successfully placed, exit the loop
            if (placed) break;
          }
        } else if (variation == 3) { // Diagonal up filling
          // Try to fill the word diagonally up up to 100 times
          for (int i = 0; i < 100; i++) {
            placed = fillDiagonalUp(word);
            // If word is successfully placed, exit the loop
            if (placed) break;
          }
        } else if (variation == 4) { // Vertical up filling
          // Try to fill the word vertically up (reversed) up to 100 times
          for (int i = 0; i < 100; i++) {
            placed = fillVerticalUp(word);
            // If word is successfully placed, exit the loop
            if (placed) break;
          }
        }
        // If the word is placed successfully, break out of the loop
        if (placed) {
          break;
        }
      }
    }
    // Fill the remaining empty spaces with random letters
    extraLetters();
  }

  /*
   *Method to fill the grid horizontally with a word
   *returns boolean value to check if the word is placed
   */
  public boolean fillHorizontal(String word) {

    int wordLength = word.length(); // Get the length of the word
    Random random = new Random(); // Create a Random object to generate random positions
    
    if(wordLength > MAXWORDS) {
      return false;
    }

    int row = random.nextInt(grid.length); // Generate a random row within the grid
    /* 
    Generate a random starting column within the grid
    ensuring enough space for the word to fit horizontally
    */
    int col = random.nextInt(grid[0].length - wordLength + 1);

    // Check if the word exceeds the grid boundary horizontally and return false if it exceeds
    if (col + wordLength > grid[0].length) {
      return false;
    }

    // Check if the word conflicts with existing characters in the grid
    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row][col + i];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if (!String.valueOf(currentChar).equals(" ") && currentChar != wordChar) {
        return false;
      }
    }
    // Place the word horizontally in the grid
    for (int i = 0; i < wordLength; i++) {
      grid[row][col + i] = word.charAt(i);
    }
    // Word successfully placed horizontally
    return true;
  }

  /*
   *Method to fill the grid vertically down with a word
   *returns boolean value to check if the word is placed
   */
  public boolean fillVertical(String word) {
    int wordLength = word.length();
    Random random = new Random();

    if(wordLength > MAXWORDS) {
      return false;
    }
    /* 
    Generate a random starting row within the grid
    ensuring enough space for the word to fit vertically
    */
    int row = random.nextInt(grid.length - wordLength + 1);

    int col = random.nextInt(grid[0].length); //Generate a random starting column

    // Check if the word exceeds the grid boundary vertically and return false if it exceeds
    if (col + wordLength > grid.length) {
      return false;
    }

    // Check if the word conflicts with existing characters in the grid
    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row + i][col];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if (currentChar != ' ' && currentChar != wordChar) {
        return false;
      }
    }

    // Place the word vertically down in the grid
    for (int i = 0; i < wordLength; i++) {
      grid[row + i][col] = word.charAt(i);
    }
    // Word successfully placed
    return true;
  }

  /*
   *Method to fill the grid diagonally down with a word
   *returns boolean value to check if the word is placed
   */
  public boolean fillDiagonalDown(String word) {
    int wordLength = word.length();
    Random random = new Random();

    if(wordLength > MAXWORDS) {
      return false;
    }
    /* 
    Generate a random starting row within the grid
    ensuring enough space for the word to fit vertically
    */
    int row = random.nextInt(grid.length - wordLength + 1);

    /* 
    Generate a random starting column within the grid
    ensuring enough space for the word to fit horizontally
    */
    int col = random.nextInt(grid[0].length - wordLength + 1);

    // Check if the word exceeds the grid boundary vertically and horizonatlly
    // and return false if it exceeds
    if ((col + wordLength) > grid.length || (row + wordLength) > grid.length) {
      return false;
    }

    // Check if the word conflicts with existing characters in the grid
    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row + i][col + i];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if (currentChar != ' ' && currentChar != wordChar) {
        return false;
      }
    }

    // Place the word diagonally down in the grid
    for (int i = 0; i < wordLength; i++) {
      grid[row + i][col + i] = word.charAt(i);
    }

    //word placed successfully
    return true;
  }

  /*
   *Method to fill the grid diagonally up with a word
   *returns boolean value to check if the word is placed
   */
  public boolean fillDiagonalUp(String word) {
    int wordLength = word.length();
    Random random = new Random();

    if(wordLength > MAXWORDS) {
      return false;
    }
    /* 
    Generate a random starting row within the grid
    ensuring enough space for the word to fit vertically
    */
    int row = random.nextInt(grid.length - wordLength + 1);

    /* 
    Generate a random starting column within the grid
    ensuring enough space for the word to fit horizontally
    */
    int col = random.nextInt(grid[0].length - wordLength + 1);

    // Check if the word exceeds the grid boundary vertically and horizonatlly
    // and return false if it exceeds
    if ((col + wordLength) > grid.length || (row - wordLength) < 0) {
      return false;
    }

    // Check if the word conflicts with existing characters in the grid
    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row - i][col + i];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if (currentChar != ' ' && currentChar != wordChar) {
        return false;
      }
    }

    // Place the word diagonally up in the grid
    for (int i = 0; i < wordLength; i++) {
      grid[row - i][col + i] = word.charAt(i);
    }
    //word placed succesfully
    return true;
  }

  /*
   *Method to fill the grid vertically up with a word
   *returns boolean value to check if the word is placed
   */
  public boolean fillVerticalUp(String word) {
    int wordLength = word.length();
    Random random = new Random();

    if(wordLength > MAXWORDS) {
      return false;
    }
    
    /* 
    Generate a random starting row within the grid
    ensuring enough space for the word to fit vertically
    */
    int row = random.nextInt(grid.length - wordLength + 1);

    int col = random.nextInt(grid[0].length); //Generate a random starting column

    // Check if the word exceeds the grid boundary vertically and return false if it exceeds
    if (col + wordLength > grid.length || (row - wordLength) < 0) {
      return false;
    }

    // Check if the word conflicts with existing characters in the grid
    for (int i = 0; i < wordLength; i++) {
      char currentChar = grid[row - i][col];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if (currentChar != ' ' && currentChar != wordChar) {
        return false;
      }
    }

    // Place the word vertically up in the grid
    for (int i = 0; i < wordLength; i++) {
      grid[row - i][col] = word.charAt(i);
    }
    // Word successfully placed
    return true;
  }

  /**
   * Fills the empty spaces in the grid with random letters.
   * Random letters are generated using lowercase English alphabets.
   */
  public void extraLetters() {
    Random random = new Random();
    // Iterate through each cell in the grid
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid.length; j++) {
        char currentChar = grid[i][j];
        // If the current cell is empty, fill it with a random letter
        if (currentChar == ' ') {
          // Generate a random lowercase letter (a-z)
          char randomLetter = (char) (random.nextInt(26) + 'a');
          // Fill the current cell with the random letter
          grid[i][j] = randomLetter;
        }
      }
    }
  }

  /**
   * Displays the contents of the grid in the console.
   * Each cell value is printed followed by a space.
   * Each row is printed on a new line.
   */
  public void DisplayGrid() {
    // Iterate through each cell in the grid
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        // Print the value of the current cell followed by a space
        System.out.print(grid[i][j] + " ");
      }
      // Move to the next line after printing all cells in the row
      System.out.println();
    }
  }
}

