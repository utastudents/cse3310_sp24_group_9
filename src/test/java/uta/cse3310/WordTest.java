package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Unit test for simple App.
 */
public class WordTest {
   public void testWordGson(){
   //test the difference of how json prints with or without coordinates
    Word firstWord = new Word("hello"); //this variable gets its coordinates set
    Word secondWord = new Word("world"); //this variable does not get its coordinates set

    firstWord.setCoordinates(5,10);

    System.out.println(firstWord.wordJson());
    System.out.println(secondWord.wordJson());

   }
}
