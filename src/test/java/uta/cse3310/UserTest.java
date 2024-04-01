package uta.cse3310;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static junit.framework.Assert.assertEquals;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserTest {
   public void testUserGson(){//this test views the difference between two players to see how the values update
   User playerOne = new User(1,"Jimmy",colors.RED);
   User playerTwo = new User(2,"Davis",colors.BLUE);
   Word firstWord = new Word("hello");
   Word secondWord = new Word("world");

   playerOne.updateUserWords(firstWord.word);
   playerOne.updateUserWords(secondWord.word);
   playerOne.userCrown(true);
   playerOne.readyUp();
   
   assertEquals("{\"ID\":1,\"score\":10,\"name\":\"Jimmy\",\"color\":\"RED\",\"ready\":true,\"crown\":true,\"foundWords\":[\"hello\",\"world\"]}", playerOne.userJson());
   assertEquals("{\"ID\":2,\"score\":0,\"name\":\"Davis\",\"color\":\"BLUE\",\"ready\":false,\"crown\":false,\"foundWords\":[]}", playerTwo.userJson());

   }
}