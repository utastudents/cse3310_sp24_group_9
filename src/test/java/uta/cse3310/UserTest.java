package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static junit.framework.Assert.assertEquals;
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

   //  System.out.println(playerOne.userJson());
   //  System.out.println(playerTwo.userJson());
   assertEquals("{\"ID\":1,\"score\":10,\"name\":\"Jimmy\",\"color\":\"RED\",\"ready\":false,\"crown\":true,\"foundWords\":[{\"xCoordinate\":0,\"yCoordinate\":0,\"word\":\"hello\",\"hasBeenFound\":false},{\"xCoordinate\":0,\"yCoordinate\":0,\"word\":\"world\",\"hasBeenFound\":false}]}", playerOne.userJson());
   assertEquals("{\"ID\":2,\"score\":0,\"name\":\"Jimmy\",\"color\":\"BLUE\",\"ready\":false,\"crown\":false,\"foundWords\":[]}", playerTwo.userJson());

   }
}