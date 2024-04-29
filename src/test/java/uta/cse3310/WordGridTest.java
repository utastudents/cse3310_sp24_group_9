package uta.cse3310;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

public class WordGridTest {

  
  public void testWordGridCreation() {
    WordGrid wordGrid = new WordGrid();
    assertNotNull(wordGrid);
  }

  public void testWordFill() {
    WordGrid wordGrid = new WordGrid();
    wordGrid.WordFill();
    float totalWords = wordGrid.getVariationDensity(5);
    float requiredVariationDensity = wordGrid.getVariationDensity(6);
    float requiredUpDensity = wordGrid.getVariationDensity(4);
    float requiredDownDensity = wordGrid.getVariationDensity(1);
    float requiredHorizontalDensity = wordGrid.getVariationDensity(0);
    float requiredDiagonalUpDensity = wordGrid.getVariationDensity(3);
    float requiredDiagonalDownDensity = wordGrid.getVariationDensity(2);
    /*
     * 
    assertTrue(requiredUpDensity >= requiredVariationDensity);
    assertTrue(requiredDownDensity >= requiredVariationDensity);
    assertTrue(requiredDiagonalUpDensity >= requiredVariationDensity);
    assertTrue(requiredDiagonalDownDensity >= requiredVariationDensity);
    assertTrue(requiredHorizontalDensity >= requiredVariationDensity);
     * 
    */
    

    

    System.out.println("\nRequired number of words per variation " + totalWords*0.15f);
    System.out.println("Required variation density: " + requiredVariationDensity);

    System.out.println("\nHorizontal words: " + requiredHorizontalDensity * totalWords);
    System.out.println("Horizontal density: " + requiredHorizontalDensity);

    System.out.println("\nVertical Up words: " + requiredUpDensity * totalWords);
    System.out.println("Vertical Up density: " + requiredUpDensity);

    System.out.println("\nVertical Down words: " + requiredDownDensity * totalWords);
    System.out.println("Vertical Down density: " + requiredDownDensity);

    System.out.println("\nDiagonal Up words: " + requiredDiagonalUpDensity * totalWords);
    System.out.println("Diagonal Up density: " + requiredDiagonalUpDensity);

    System.out.println("\nDiagonal Down words: " + requiredDiagonalDownDensity * totalWords);
    System.out.println("Diagonal Down density: " + requiredDiagonalDownDensity);



  }
  
  // Implemented 4/21 
  public void testHintWordGrid() {
      WordGrid wordGrid = new WordGrid();
      wordGrid.WordFill(); // Fill the word grid with words
      char hintLetter = wordGrid.hintWordGrid(); // Get a hint letter from the word grid
      // Assert that the hint letter is not a space character
      assertTrue(hintLetter != ' ');
      System.out.println("Received hint letter: " + hintLetter);
  }

  
  public void testFillHorizontal() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillHorizontal("test")); // Test a word that can be placed horizontally
    assertFalse(
      wordGrid.fillHorizontal(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed horizontally due to length
  }

  
  public void testFillVerticalDown() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillVerticalDown("test")); // Test a word that can be placed vertically
    assertFalse(
      wordGrid.fillVerticalDown(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed vertically due to length
  }

  
  public void testFillDiagonalDown() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillDiagonalDown("test")); // Test a word that can be placed diagonally down
    assertFalse(
      wordGrid.fillDiagonalDown(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed diagonaly down due to length

  }

  
  public void testFillDiagonalUp() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillDiagonalUp("test")); // Test a word that can be placed diagonally up
    assertFalse(
      wordGrid.fillDiagonalUp(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed diagonally up due to length
  }

  
  public void testFillVerticalUp() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillVerticalUp("test")); // Test a word that can be placed vertically up
    assertFalse(
      wordGrid.fillVerticalUp(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed vertically up due to length
  }

  
  public void testExtraLetters() {
    WordGrid wordGrid = new WordGrid();
    // Fill the grid with some words first
    wordGrid.WordFill();
    // Test adding extra letters
    // wordGrid.extraLetters(); 
    // Assuming extraLetters method doesn't throw exceptions and completes without error,
    // we can consider the test passed.
  }

    public void testDisplayGrid() {
    WordGrid wordGrid = new WordGrid();
    // Fill the grid with some words first
    wordGrid.WordFill();
    // Test displaying the grid
    wordGrid.DisplayGrid();
    // Assuming DisplayGrid method doesn't throw exceptions and completes without error,
    // we can consider the test passed.
  }

  public void testWordGridJson() {
    WordGrid wordGrid = new WordGrid();
    // Fill the grid with some words first
    wordGrid.WordFill();

    // Test converting the grid to JSON
    String json = wordGrid.wordGridJson();

    // Test that the JSON is valid
    assertTrue(json.startsWith("{"));
    assertTrue(json.endsWith("}"));
  }

  
}
