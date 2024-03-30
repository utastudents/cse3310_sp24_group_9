package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Unit test for simple App.
 */
public class UserTest {
   public void testUserGson(){
    //this test views the difference between two players to see how the values update
    User playerOne = new User(1,"Jimmy",colors.RED);
    User playerTwo = new User(2,"Jimmy",colors.BLUE);
    Word firstWord = new Word("hello");
    Word secondWord = new Word("world");

    playerOne.updateUserWords(firstWord);
    playerOne.updateUserWords(secondWord);
    playerOne.userCrown(true);

    System.out.println(playerOne.userJson());
    System.out.println(playerTwo.userJson());

   }
}