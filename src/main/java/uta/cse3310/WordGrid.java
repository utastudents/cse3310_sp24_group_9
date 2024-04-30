package uta.cse3310;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WordGrid {
  /* private Color[][] cellColors;//new */
  private int MAXWORDS = 35;

  public char[][] grid = new char[MAXWORDS][MAXWORDS]; // This is the grid to be filled
  WordBank wordsBank; // to create instance of WordBank
  public HashMap<Integer, String> wordBankMap = new HashMap<>(MAXWORDS);
  private HashMap<Integer, ArrayList<Integer>> coordinateMap = new HashMap<Integer, ArrayList<Integer>>(
    MAXWORDS
  );
  private Random random = new Random();
  private List<Integer> variations = new ArrayList<>(
    Arrays.asList(0, 1, 2, 3, 4)
  );

  private float totalWords = 0;
  private float horizontalCount = 0;
  private float verticalUpCount = 0;
  private float verticalDownCount = 0;
  private float diagonalUpCount = 0;
  private float diagonalDownCount = 0;
  private float variationDensity;
  private float requiredDensity = 0.67f;

  public char[][] getGrid(){
    return grid;
  }

  public WordGrid(){
    try{
      this.wordsBank = new WordBank("Data/words.txt"); // create an instance of WordBank
    }catch (IOException e){
      // Handle the exception by printing an error message
      System.err.println("Error reading words file: " + e.getMessage());
    }
    //this.wordsBank.setRandomWords(wordBankMap);
    for(char[] row : this.grid){
      Arrays.fill(row, ' ');
    }
  }

  /*
   * Method WordFill() will iterate through each word
   * in the word bank map, and each variation in the array,
   * filling it accordingly so that it can fill the grid with words.
   */
  public void WordFill(){
    // Iterate through each word in the word bank map
    while(wordsBank.getDensity() < requiredDensity){
      String randomWord = wordsBank.getRandomWord();
      boolean placed = false;
      // Shuffle the list of variations for randomness
      Collections.shuffle(variations);

      // Iterate through each variation in the variations array
      for(int variation : variations){
        float vDensity = getVariationDensity(variation);
        if(vDensity > (0.15*totalWords)){
          // If variation density is less than 15%, skip this variation
          continue;
        }
        // Check the type of variation
        if(variation == 0){ // Horizontal filling
          placed = fillHorizontal(randomWord);
          if(placed){
            break;
          }
        }else if(variation == 1){ // Vertical filling
          placed = fillVerticalDown(randomWord);
          if(placed){
            break;
          }
        }else if(variation == 2){ // Diagonal down filling
          placed = fillDiagonalDown(randomWord);
          if(placed){
            break;
          }
        }else if(variation == 3){ // Diagonal up filling
          placed = fillDiagonalUp(randomWord);
          if(placed){
            break;
          }
        }else if(variation == 4){ // Vertical up filling
          placed = fillVerticalUp(randomWord);
          if(placed){
            break;
          }
        }
      }
    }

    // Fill the remaining empty spaces with random letters
    extraLetters();

  }

  public float getVariationDensity(int variation){
    switch(variation){
      case 0: // Horizontal filling
        return (float) horizontalCount / totalWords;
      case 1: // Vertical filling
        return (float) verticalDownCount / totalWords;
      case 2: // Diagonal down filling
        return (float) diagonalDownCount / totalWords;
      case 3: // Diagonal up filling
        return (float) diagonalUpCount / totalWords;
      case 4: // Vertical up filling
        return (float) verticalUpCount / totalWords;
      case 5:
        return (float) totalWords;
      case 6:
        variationDensity = (float) (totalWords) * requiredDensity;
        variationDensity = variationDensity / variations.size();
        return variationDensity;
    
      default:
        return -1;
    }
  }

  /*
   * Method fillVerticalDown() fills the grid horizontally
   * up with a word that returns the boolean value to check
   * if the word is placed successfully.
   */
  public boolean fillHorizontal(String word){
    int wordLength = word.length(); // Get the length of the word

    if(wordLength > MAXWORDS){
      return false;
    }

    /* 
    Generate a random starting column within the grid
    ensuring enough space for the word to fit vertically
    */
    int row = random.nextInt(grid.length);

    /* 
    Generate a random starting column within the grid
    ensuring enough space for the word to fit horizontally
    */
    int col = random.nextInt(grid.length);

    // Check if the word exceeds the grid boundary horizontally and return false if it exceeds
    if((col + wordLength) > grid.length){
      col -= wordLength;
    }

    // Check if the word conflicts with existing characters in the grid
    for(int i = 0; i < wordLength; i++){
      char currentChar = grid[row][col + i];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if(!String.valueOf(currentChar).equals(" ") && currentChar != wordChar){
        //System.err.println("This word conflicts with other placed words");
        return false;
      }
    }
    // Place the word horizontally in the grid
    for(int i = 0; i < wordLength; i++){
      grid[row][col + i] = word.charAt(i);
    }
    int hash = wordsBank.hashCode(row, col, row, (col + (wordLength - 1)));
    wordsBank.placedWord(word);
    wordBankMap.put(hash, word);
    addValueToMap(row, col);
    horizontalCount += 1;
    totalWords += 1;

    // Word successfully placed horizontally
    return true;
  }

  /*
   * Method fillVerticalDown() fills the grid vertically up
   * with a word that returns the boolean value to check
   * if the word is placed successfully.
   */
  public boolean fillVerticalUp(String word){
    int wordLength = word.length();

    if(wordLength > MAXWORDS){
      System.err.println("this word is longer than the max word length");
      return false;
    }
    /* 
    Generate a random starting row within the grid
    ensuring enough space for the word to fit vertically
    */
    int row = random.nextInt(grid.length);

    int col = random.nextInt(grid.length); //Generate a random starting column

    // Check if the word exceeds the grid boundary vertically and return false if it exceeds
    if((row + wordLength) > grid.length){
      row -= wordLength;
    }

    // Check if the word conflicts with existing characters in the grid
    for(int i = 0; i < wordLength; i++){
      char currentChar = grid[row + i][col];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if(currentChar != ' ' && currentChar != wordChar){
        //System.err.println("This word conflicts with other placed words");
        return false;
      }
    }

    // Place the word vertically down in the grid
    for(int i = 0; i < wordLength; i++){
      grid[row + i][col] = word.charAt(i);
    }
    int hash = wordsBank.hashCode(row, col, (row + (wordLength - 1)), col);
    wordsBank.placedWord(word);
    wordBankMap.put(hash, word);
    addValueToMap(row, col);
    verticalUpCount += 1;
    totalWords += 1;
    // Word successfully placed
    return true;
  }

  /*
   * Method fillVerticalDown() fills the grid diagonally down
   * with a word that returns the boolean value to check
   * if the word is placed successfully.
   */
  public boolean fillDiagonalDown(String word){
    int wordLength = word.length();

    if(wordLength > MAXWORDS){
      return false;
    }
    /* 
    Generate a random starting row within the grid
    ensuring enough space for the word to fit vertically
    */
    int row = random.nextInt(grid.length);

    /* 
    Generate a random starting column within the grid
    ensuring enough space for the word to fit horizontally
    */
    int col = random.nextInt(grid.length);

    // Check if the word exceeds the grid boundary vertically and horizonatlly
    // and return false if it exceeds
    if((col + wordLength) > grid.length){
      col -= wordLength;
    }
    if((row + wordLength) > grid.length){
      row -= wordLength;
    }

    // Check if the word conflicts with existing characters in the grid
    for(int i = 0; i < wordLength; i++){
      char currentChar = grid[row + i][col + i];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if(currentChar != ' ' && currentChar != wordChar){
        //System.err.println("This word conflicts with other placed words");
        return false;
      }
    }

    // Place the word diagonally down in the grid
    for(int i = 0; i < wordLength; i++){
      grid[row + i][col + i] = word.charAt(i);
    }
    int hash = wordsBank.hashCode(row, col, (row + (wordLength - 1)), (col + (wordLength - 1)));
    wordsBank.placedWord(word);
    wordBankMap.put(hash, word);
    addValueToMap(row, col);
    diagonalDownCount += 1;
    totalWords += 1;

    // Word successfully placed
    return true;
  }

  /*
   * Method fillVerticalDown() fills the grid diagonally up
   * with a word that returns the boolean value to check
   * if the word is placed successfully.
   */
  public boolean fillDiagonalUp(String word){
    int wordLength = word.length();

    if(wordLength > MAXWORDS){
      return false;
    }
    /* 
    Generate a random starting row within the grid
    ensuring enough space for the word to fit vertically
    */
    int row = random.nextInt(grid.length);

    /* 
    Generate a random starting column within the grid
    ensuring enough space for the word to fit horizontally
    */
    int col = random.nextInt(grid.length);

    // Check if the word exceeds the grid boundary vertically and horizonatlly
    // and return false if it exceeds
    if((col + wordLength) > grid.length){
      col -= wordLength;
    }
    if((row - wordLength) < 0){
      row += wordLength;
    }

    // Check if the word conflicts with existing characters in the grid
    for(int i = 0; i < wordLength; i++){
      char currentChar = grid[row - i][col + i];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if(currentChar != ' ' && currentChar != wordChar){
        //System.err.println("This word conflicts with other placed words");
        return false;
      }
    }

    // Place the word diagonally up in the grid
    for(int i = 0; i < wordLength; i++){
      grid[row - i][col + i] = word.charAt(i);
    }
    int hash = wordsBank.hashCode(row, col, (row - (wordLength - 1)), (col + (wordLength - 1)));
    wordsBank.placedWord(word);
    wordBankMap.put(hash, word);
    addValueToMap(row, col);
    diagonalUpCount += 1;
    totalWords += 1;
    // Word successfully placed
    return true;
  }

  /*
   * Method fillVerticalDown() fills the grid vertically
   * up with a word that returns the boolean value to check
   * if the word is placed successfully.
   */
  public boolean fillVerticalDown(String word){
    int wordLength = word.length();

    if(wordLength > MAXWORDS){
      return false;
    }

    /* 
    Generate a random starting row within the grid
    ensuring enough space for the word to fit vertically
    */
    int row = random.nextInt(grid.length);
    int col = random.nextInt(grid.length);

    // Check if the word exceeds the grid boundary vertically and return false if it exceeds
    if((row - wordLength) < 0){
      row += (wordLength);
    }

    // Check if the word conflicts with existing characters in the grid
    for(int i = 0; i < wordLength; i++){
      char currentChar = grid[row - i][col];
      char wordChar = word.charAt(i);
      // If the current position in the grid is not empty and
      // does not match the corresponding character in the word, return false
      if(currentChar != ' ' && currentChar != wordChar){
        //System.err.println("This word conflicts with other placed words");
        return false;
      }
    }

    // Place the word vertically up in the grid
    for(int i = 0; i < wordLength; i++){
      grid[row - i][col] = word.charAt(i);
    }
    int hash = wordsBank.hashCode(row, col,(row - (wordLength - 1)), col);
    wordsBank.placedWord(word);
    wordBankMap.put(hash, word);
    addValueToMap(row, col);
    verticalDownCount += 1;
    totalWords += 1;
    // Word successfully placed
    return true;
  }

  /*
   * Method extraLetters() will iterate thrugh each grid
   * and fill in the cell with an empty letter from A-Z.
   * Fill the current cell with a random letter.
   */
  public void extraLetters(){
    for(int i = 0; i < grid.length; i++){
      for(int j = 0; j < grid.length; j++){
        char currentChar = grid[i][j];
        if(currentChar == ' '){
          char randomLetter = (char) (random.nextInt(26) + 'a');
          grid[i][j] = randomLetter;
        }
      }
    }
  }

  /*
   * Method displayGrid() will iterate through
   * each cell in the grid, and print the current cell
   * followed by a space, and repeat until the max length.
   */
  public void DisplayGrid(){
    // Iterate through each cell in the grid
    for(int i = 0; i < grid.length; i++){
      for(int j = 0; j < grid[i].length; j++){
        // Print the value of the current cell followed by a space
        System.out.print(grid[i][j] + " ");
      }
      // Move to the next line after printing all cells in the row
      System.out.println();
    }
  }

  public String getGridAsJson(){
    String[][] stringGrid = new String[grid.length][grid[0].length];
    for(int i = 0; i < grid.length; i++){
        for(int j = 0; j < grid[i].length; j++){
            stringGrid[i][j] = String.valueOf(grid[i][j]);
        }
    }

    Gson gson = new Gson();
    return gson.toJson(stringGrid);
  }

  /*
   * Method removeWord() will make it incredibly fast to remove a word from
   * the available words. Instead of looping through every word to see if there's a 
   * match, simply just remove the word the map. If no match, nothing happens to the list.
   */
  public Object[] removeWord(int x1, int y1, int x2, int y2){
    int hash = wordsBank.hashCode(y1, x1, y2, x2);
    int hashTwo = wordsBank.hashCode(y2, x2, y1, x1);
    
    String word = null;
    boolean boolResult = false;
    String stringResult;

    System.out.println("word at hash: " + wordBankMap.get(hash) + " word at hash2: " + wordBankMap.get(hashTwo));

    if((word = wordBankMap.get(hash)) != null){
      boolResult = true;
      stringResult = word;
      wordBankMap.remove(hash);
      return new Object[] { boolResult, stringResult };
    }else if((word = wordBankMap.get(hashTwo)) != null){
      boolResult = true;
      stringResult = word;
      wordBankMap.remove(hashTwo);
      return new Object[] { boolResult, stringResult };
    }
    return new Object[] { boolResult };
  }

  /*
   * Method addValueToMap() will add a value to the coordinate map at given key. 
   * If the key already exists value is appended to the existing list. Otherwise if the
   * key is new, add a new list that contains the value created and add that to the map.
   */
  public void addValueToMap(int key, int value){
    if(coordinateMap.containsKey(key)){
      ArrayList<Integer> list = coordinateMap.get(key);
      list.add(value);
    }else{
      ArrayList<Integer> newList = new ArrayList<>();
      newList.add(value);
      coordinateMap.put(key, newList);
    }
  }

  /*
   * Method getRandomCoordinates() will randomly get a set 
   * of coordinates on the grid. Return an array that contains 
   * randomly selected key value pairs from the map.
   */
  public int[] getRandomCoordinates(){
    int randomIndex = random.nextInt(coordinateMap.keySet().size());
    int randomKey = coordinateMap
      .keySet()
      .stream()
      .skip(randomIndex)
      .findFirst()
      .orElse(null);
    ArrayList<Integer> list = coordinateMap.get(randomKey);
    int randomListIndex = random.nextInt(list.size());
    Integer randomValue = list.get(randomListIndex);
    list.remove(randomListIndex);

    return new int[] { randomKey, randomValue };
  }

  /*
   * Method wordsLeft() will check how many
   * words are left in the wordBankmap everytime
   * that it is called.
   */
  public int wordsLeft(){
    return wordBankMap.size();
  }

  public String wordGridJson(){
    ArrayList<String> wordList = new ArrayList<>();

    for(Integer key : wordBankMap.keySet()){
      wordList.add(wordBankMap.get(key));
    }

    ArrayList<ArrayList<Character>> gridList = new ArrayList<>();

    for(char[] row : grid){
      ArrayList<Character> rowList = new ArrayList<>();
      for(char cell : row){
        rowList.add(cell);
      }
      gridList.add(rowList);
    }

    HashMap<String, Object> jsonData = new HashMap<>();
    jsonData.put("Words", wordList);
    jsonData.put("Grid", gridList);

    Gson gson = new Gson();
    String jsonString = gson.toJson(jsonData);

    return jsonString;
  }
   
  /*
   * Method hintWordGrid() will check a set of random coordinates.
   * If the letter is empty, get a new set of coordinates and
   * keep going until a letter is returned.
   */
  public char hintWordGrid(){
    int[] coordinates = getRandomCoordinates();
    char letter = grid[coordinates[0]][coordinates[1]];

    while(letter == ' '){
      coordinates = getRandomCoordinates();
      letter = grid[coordinates[0]][coordinates[1]];
    }

    return letter;
  }
  
  /*
   * Method wordExistsInGrid() will check a set of coordinates for
   * an existing range through different logic sequence. If it matches
   * return true, if it doesn't return false.
   */
  public boolean wordExistsInGrid(int x1, int y1, int x2, int y2){
      Gson gson = new Gson();
      String gridJson = getGridAsJson();
      char[][] gridArray = gson.fromJson(gridJson, char[][].class);

      if(x1 < 0 || x1 >= gridArray.length || y1 < 0 || y1 >= gridArray[0].length || x2 < 0 || x2 >= gridArray.length || y2 < 0 || y2 >= gridArray[0].length){
          System.out.println("Coordinates out of bounds.");
          return false;
      }

      if(x1 == x2){
          if(y1 < 0 || y2 < 0 || y1 >= gridArray[0].length || y2 >= gridArray[0].length || y1 > y2){
              System.out.println("Invalid horizontal range.");
              return false;
          }
          for(int j = y1; j <= y2; j++){
              if(gridArray[x1][j] == ' '){
                  System.out.println("Found empty space at (" + x1 + ", " + j + ").");
                  return false;
              }
          }
          System.out.println("A letter was found.");
          return true;
      }else if(y1 == y2){
          if(x1 < 0 || x2 < 0 || x1 >= gridArray.length || x2 >= gridArray.length || x1 > x2){
              System.out.println("Invalid vertical range.");
              return false;
          }
          for(int i = x1; i <= x2; i++){
              if(gridArray[i][y1] == ' '){
                  System.out.println("Found empty space at (" + i + ", " + y1 + ").");
                  return false;
              }
          }
          System.out.println("A letter was found");
          return true;
      }else{
          if(x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 || x1 >= gridArray.length || y1 >= gridArray[0].length ||x2 >= gridArray.length || y2 >= gridArray[0].length ||Math.abs(x2 - x1) != Math.abs(y2 - y1)){
              System.out.println("Invalid diagonal range.");
              return false;
          }
          int i = x1;
          int j = y1;
          while(i <= x2 && j <= y2){
              if(gridArray[i][j] == ' '){
                  System.out.println("Found empty space at (" + i + ", " + j + ").");
                  return false;
              }
              i++;
              j++;
          }
          System.out.println("A letter was found.");
          return true;
      }
    }
  }

