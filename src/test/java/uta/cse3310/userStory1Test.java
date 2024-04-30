package uta.cse3310;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import junit.framework.Assert;

public class userStory1Test {
    // Check later ***
    public void testInitializeLobby() {
        Game game = new Game();
        
        // User user1 = new User(1, "Jimmy", colors.RED);
        // User user2 = new User(2, "Davis", colors.BLUE);
        // User user3 = new User(3, "Bud", colors.GREEN);
    
        String user1Json = "{\"ID\":1,\"username\":\"Jimmy\"}";
        String user2Json = "{\"ID\":2,\"username\":\"Davis\"}";
        String user3Json = "{\"ID\":3,\"username\":\"Bud\"}";
        String user4Json = "{\"ID\": 4, \"username\": \"JBD\"}";

        game.addUserFromJson(user1Json);
        game.addUserFromJson(user2Json);
        game.addUserFromJson(user3Json);
        game.addUserFromJson(user4Json);

        String userListJson = game.getUserListJson();
        System.out.println("User List JSON: " + userListJson);
    
        // assertEquals("{\"ID\":1,\"username\":\"Jimmy\",\"color\":\"Red\"}", user1.userJson());
        // assertEquals("{\"ID\":2,\"username\":\"Davis\",\"color\":\"Blue\"}", user2.userJson());
        // assertEquals("{\"ID\":3,\"username\":\"Bud\",\"color\":\"Green\"}", user3.userJson());

        String expectedUserListJson = "[{\"ID\":1,\"username\":\"Jimmy\"},{\"ID\":2,\"username\":\"Davis\"},{\"ID\":3,\"username\":\"Bud\"},{\"ID\":4,\"username\":\"JBD\"}]";

        assertEquals(expectedUserListJson, userListJson);
    }

    public void testAddRemovePlayer() {
        Game game = new Game();

        String user1Json = "{\"ID\": 1, \"username\": \"Jimmy\"}";
        String user2Json = "{\"ID\": 2, \"username\": \"Davis\"}";
        String user3Json = "{\"ID\": 3, \"username\": \"Bud\"}";
        String user4Json = "{\"ID\": 4, \"username\": \"JBD\"}";

        game.addUserFromJson(user1Json);
        game.addUserFromJson(user2Json);
        game.addUserFromJson(user3Json);
        game.addUserFromJson(user4Json);

        assertEquals(4, game.users.size());
        assertEquals("Jimmy", game.getUserName(0));
        assertEquals("Davis", game.getUserName(1));
        assertEquals("Bud", game.getUserName(2));
        assertEquals("JBD", game.getUserName(3));

        game.removeUserFromJson(user4Json);

        assertEquals(3, game.users.size());
        assertEquals("Jimmy", game.getUserName(0));
        assertEquals("Davis", game.getUserName(1));
        assertEquals("Bud", game.getUserName(2));
    }

    public void testFillGridCheckWordAndUpdateScore() {
        Game game = new Game();
        WordGrid wordGrid = new WordGrid();
        
        String user1Json = "{\"ID\": 1, \"username\": \"Jimmy\"}";
        String user2Json = "{\"ID\": 2, \"username\": \"Davis\"}";
        String user3Json = "{\"ID\": 3, \"username\": \"Bud\"}";
    
        game.addUserFromJson(user1Json);
        game.addUserFromJson(user2Json);
        game.addUserFromJson(user3Json);
    
        for (User user : game.users) {
            if (!user.isReady()) {
                user.readyUp();
            }
        }
    
        game.gameStart();
        game.fillGrid();
        wordGrid.WordFill();
        
        for (User user : game.users) {
            System.out.println("Also updated " + user.getName() + " score: " + user.getScore());
        }

        wordGrid.grid[0][0] = 'a';
        wordGrid.grid[0][1] = 'b';
        wordGrid.grid[0][2] = 'c';
        wordGrid.grid[0][3] = 'd';

        // DisplayWordGrid for now. ***
        for (int i = 0; i < wordGrid.grid.length; i++) {
            for (int j = 0; j < wordGrid.grid[i].length; j++) {
                System.out.print(wordGrid.grid[i][j] + " ");
            }
            System.out.println();
        }
        
        String gridJson = wordGrid.getGridAsJson();
        
        assertNotNull(gridJson);
        assertEquals(true, wordGrid.wordExistsInGrid(0, 0, 0, 3)); // Horizontal
        assertEquals(true, wordGrid.wordExistsInGrid(0, 0, 3, 0)); // Vertical
        assertEquals(true, wordGrid.wordExistsInGrid(0, 0, 3, 3)); // Diagonal down
        assertEquals(true, wordGrid.wordExistsInGrid(3, 3, 0, 0)); // Diagonal up
        
        String wordFoundJson = "{\"wordFound\": " + wordGrid.wordExistsInGrid(0, 0, 0, 3) + "}";
        System.out.println("The word 'abcd' was found: " + wordFoundJson);
        
        for (User user : game.users) {
            if (user.getName().equals("Davis") && wordGrid.wordExistsInGrid(0, 0, 0, 3)) {
                user.updateUserWords("abcd");
                String updatedUserJson = new Gson().toJson(user);
                System.out.println("Found word 'abcd' added to user Davis's list: " + updatedUserJson);
            }
        }
    }

    public void testGameChatToJsonString() {
        Game game = new Game();
        
        String user1Json = "{\"ID\": 1, \"username\": \"Jimmy\"}";
        String user2Json = "{\"ID\": 2, \"username\": \"Davis\"}";
        String user3Json = "{\"ID\": 3, \"username\": \"Bud\"}";
        
        game.addUserFromJson(user1Json);
        game.addUserFromJson(user2Json);
        game.addUserFromJson(user3Json);

        // game.addUser(1, "Jimmy");
        // game.addUser(2, "Bud");
        // game.addUser(3, "Davis");

        // try {
        //     game.gameChatToJsonString("Do better guys", 1);
        //     // System.out.println("ChatData 1: " + game.chat);

        //     game.gameChatToJsonString("Jimmy how does it feel to be losing", 2);
        //     // System.out.println("ChatData 2: " + game.chat);

        //     game.gameChatToJsonString("Bud I dont care if I lose", 1);
        //     // System.out.println("ChatData 3: " + game.chat);

        //     game.gameChatToJsonString("Guys can yall just play the game", 3);
        //     // System.out.println("ChatData 4: " + game.chat);

        // } catch (Exception e) {
        //     junit.framework.Assert.fail("Exception thrown: " + e.getMessage());
        // }

        String[] messages = {"Do better guys", "Jimmy how does it feel to be losing", "Bud I dont care if I lose", "Guys can yall just play the game"};

        String[] usernames = {"Jimmy", "Davis", "Jimmy", "Bud"};

        // int[] userIDs = {1, 2, 1, 3};
        
        String actualGameChatJson = "";
        
        for (int i = 0; i < messages.length; i++) {
            actualGameChatJson = game.gameChatToJsonString(messages[i], usernames[i]);
        }
        
        String expectedGameChatJson = "{\"ChatData\":{\"username\":[\"Jimmy\",\"Davis\",\"Jimmy\",\"Bud\"]," +
                                       "\"userChatMessages\":[\"Do better guys\",\"Jimmy how does it feel to be losing\"," +
                                       "\"Bud I dont care if I lose\",\"Guys can yall just play the game\"]}}";

        System.out.println("Actual Game Chat JSON: " + actualGameChatJson);
        assertEquals(expectedGameChatJson, actualGameChatJson);
    }
    
    public void testDisplayScoreboardInJson() {
        Game game = new Game();

        String user1Json = "{\"ID\":1,\"username\":\"Jimmy\",\"crown\":true}";
        String user2Json = "{\"ID\":2,\"username\":\"Davis\",\"crown\":false}";
        String user3Json = "{\"ID\":3,\"username\":\"Bud\",\"crown\":false}";
        
        game.addUserFromJson(user1Json);
        game.addUserFromJson(user2Json);
        game.addUserFromJson(user3Json);

        game.users.get(0).setScore(100);
        game.users.get(1).setScore(80);
        game.users.get(2).setScore(120);

        String expectedJson = "{\"Bud\":120,\"Jimmy\":100,\"Davis\":80}";

        String actualJson = game.displayScoreboardInJson();
        System.out.println(actualJson);
        Assert.assertEquals(expectedJson, actualJson);
    }
    // Have to implement: COLORS, then test case finished, but all exclusively in JSON.
}