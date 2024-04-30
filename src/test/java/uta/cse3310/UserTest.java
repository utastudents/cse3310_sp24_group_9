package uta.cse3310;

import static junit.framework.Assert.assertEquals;

public class UserTest {
   public void testUserGson(){//this test views the difference between two players to see how the values update
   User playerOne = new User(1,"Jimmy",colors.RED);
   User playerTwo = new User(2,"Davis",colors.BLUE);

   playerOne.updateUserWords("hello");
   playerOne.updateUserWords("world");
   // playerOne.userCrown(true);
   playerOne.readyUp();
   
   // assertEquals("{\"ID\":1,\"score\":2,\"name\":\"Jimmy\",\"color\":\"RED\",\"ready\":true,\"crown\":true,\"foundWords\":[\"hello\",\"world\"]}", playerOne.userJson());
   // assertEquals("{\"ID\":2,\"score\":0,\"name\":\"Davis\",\"color\":\"BLUE\",\"ready\":false,\"crown\":false,\"foundWords\":[]}", playerTwo.userJson());

   assertEquals("{\"ID\":1,\"score\":2,\"name\":\"Jimmy\",\"color\":\"RED\",\"ready\":true,\"foundWords\":[\"hello\",\"world\"]}", playerOne.userJson());
   assertEquals("{\"ID\":2,\"score\":0,\"name\":\"Davis\",\"color\":\"BLUE\",\"ready\":false,\"foundWords\":[]}", playerTwo.userJson());
   }
}