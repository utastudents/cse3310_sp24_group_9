package uta.cse3310;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class userStory1Test {
    Game game = new Game();
    public void testInitializeLobby() {


        String user1Json = "{\"ID\":1,\"username\":\"Jimmy\"}";
        String user2Json = "{\"ID\":2,\"username\":\"Davis\"}";

        // Simulate adding users to the lobby using JSON strings
        game.addUserFromJson(user1Json);
        game.addUserFromJson(user2Json);

        // Get the JSON string representation of the user list
        String userListJson = game.getUserListJson();
        System.out.println("User List JSON: " + userListJson);

        // Define the expected JSON string representation of the user list
        String expectedUserListJson = "[{\"ID\":1,\"username\":\"Jimmy\"},{\"ID\":2,\"username\":\"Davis\"}]";

        // Assert that the actual JSON string matches the expected JSON string
        assertEquals(expectedUserListJson, userListJson);
    }
}



/*
 * Two kinds of tests in the project:

#1.  Unit tests that test a class or methods in a class.
#2.  System Level tests used to verify that a requirement is met.

Unit tests are required for the Project Implementation. The test cases should use classes and variables for representing data.

Automatic System Tests are allowed for verifying some requirements in the Project Testing assignment.  At least one of these tests is required.  And, yes, these are the tests that would have json strings for input and output.  The remainder of the requirements will be verified using manual testing with a written test procedure.
 */