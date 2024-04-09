package uta.cse3310;

import static junit.framework.Assert.assertEquals;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import main.java.uta.cse3310.WordGrid;

public class WordGridTest {

  @Test
  public void testWordGridCreation() {
    WordGrid wordGrid = new WordGrid();
    assertNotNull(wordGrid);
  }

  @Test
  public void testWordFill() {
    WordGrid wordGrid = new WordGrid();
    wordGrid.WordFill();
    // Assuming WordFill method doesn't throw exceptions and completes without error,
    // we can consider the test passed.
  }

  @Test
  public void testFillHorizontal() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillHorizontal("test")); // Test a word that can be placed horizontally
    assertFalse(
      wordGrid.fillHorizontal(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed horizontally due to length
  }

  @Test
  public void testFillVertical() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillVertical("test")); // Test a word that can be placed vertically
    assertFalse(
      wordGrid.fillVertical(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed vertically due to length
  }

  @Test
  public void testFillDiagonalDown() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillDiagonalDown("test")); // Test a word that can be placed diagonally down
    assertFalse(
      wordGrid.fillDiagonalDown(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed diagonally down due to length
  }

  @Test
  public void testFillDiagonalUp() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillDiagonalUp("test")); // Test a word that can be placed diagonally up
    assertFalse(
      wordGrid.fillDiagonalUp(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed diagonally up due to length
  }

  @Test
  public void testFillVerticalUp() {
    WordGrid wordGrid = new WordGrid();
    assertTrue(wordGrid.fillVerticalUp("test")); // Test a word that can be placed vertically up
    assertFalse(
      wordGrid.fillVerticalUp(
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
      )
    ); // Test a word that cannot be placed vertically up due to length
  }

  @Test
  public void testExtraLetters() {
    WordGrid wordGrid = new WordGrid();
    // Fill the grid with some words first
    wordGrid.WordFill();
    // Test adding extra letters
    wordGrid.extraLetters();
    // Assuming extraLetters method doesn't throw exceptions and completes without error,
    // we can consider the test passed.
  }

  @Test
  public void testDisplayGrid() {
    WordGrid wordGrid = new WordGrid();
    // Fill the grid with some words first
    wordGrid.WordFill();
    // Test displaying the grid
    wordGrid.DisplayGrid();
    // Assuming DisplayGrid method doesn't throw exceptions and completes without error,
    // we can consider the test passed.
  }
}
