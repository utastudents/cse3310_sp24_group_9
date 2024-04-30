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
    public void testInitializeLobby() {
        Game game = new Game();
        
        String user1Json = "{\"ID\":1,\"username\":\"Jimmy\",\"color\":\"RED\",\"score\":0,\"ready\":false}";
        String user2Json = "{\"ID\":2,\"username\":\"Davis\",\"color\":\"BLUE\",\"score\":0,\"ready\":false}";
        String user3Json = "{\"ID\":3,\"username\":\"Bud\",\"color\":\"GREEN\",\"score\":0,\"ready\":false}";
        String user4Json = "{\"ID\":4,\"username\":\"JBD\",\"color\":\"PURPLE\",\"score\":0,\"ready\":false}";
        
        game.addUserFromJson(user1Json);
        game.addUserFromJson(user2Json);
        game.addUserFromJson(user3Json);
        game.addUserFromJson(user4Json);
        
        String userListJson = game.getUserListJson();
        System.out.println("User List JSON: " + userListJson);
        
        String expectedUserListJson = "[" + user1Json + "," + user2Json + "," + user3Json + "," + user4Json + "]";
        
        assertEquals(expectedUserListJson, userListJson);
    }
    
    public void testAddRemovePlayer() {
        Game game = new Game();

        // Assume a lobby was just created, nobody has yet to ready up
        String user1Json = "{\"ID\":1,\"username\":\"Jimmy\",\"color\":\"RED\",\"score\":0,\"ready\":false}";
        String user2Json = "{\"ID\":2,\"username\":\"Davis\",\"color\":\"BLUE\",\"score\":0,\"ready\":false}";
        String user3Json = "{\"ID\":3,\"username\":\"Bud\",\"color\":\"GREEN\",\"score\":0,\"ready\":false}";
        String user4Json = "{\"ID\":4,\"username\":\"JBD\",\"color\":\"PURPLE\",\"score\":0,\"ready\":false}";

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
        
        String user1Json = "{\"ID\":1,\"username\":\"Jimmy\",\"color\":\"red\",\"score\":0,\"ready\":true}";
        String user2Json = "{\"ID\":2,\"username\":\"Davis\",\"color\":\"blue\",\"score\":0,\"ready\":true}";
        String user3Json = "{\"ID\":3,\"username\":\"Bud\",\"color\":\"green\",\"score\":0,\"ready\":true}";

        // Only implementing ID, UserName, Color so far
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
        
        // Tests Score here
        for (User user : game.users) {
            System.out.println("Initial: " + user.getName() + " score: " + user.getScore());
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
        
        String disconnectedUserJson = user3Json;
        String scoreboardJson = game.updateScoreboardToJsonString("abcd", disconnectedUserJson);
        assertTrue(scoreboardJson.contains("Jimmy"));
        // System.out.println("User '" + disconnectedUser.get("username").getAsString() + "' disconnected.");

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
        
        String user1Json = "{\"ID\":1,\"username\":\"Jimmy\",\"color\":\"RED\",\"score\":0,\"ready\":true}";
        String user2Json = "{\"ID\":2,\"username\":\"Davis\",\"color\":\"BLUE\",\"score\":1,\"ready\":true}";
        String user3Json = "{\"ID\":3,\"username\":\"Bud\",\"color\":\"GREEN\",\"score\":0,\"ready\":true}";
        
        game.addUserFromJson(user1Json);
        game.addUserFromJson(user2Json);
        game.addUserFromJson(user3Json);


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

        String user1Json = "{\"ID\":1,\"username\":\"Jimmy\",\"color\":\"RED\",\"score\":0,\"ready\":true}";
        String user2Json = "{\"ID\":2,\"username\":\"Davis\",\"color\":\"BLUE\",\"score\":1,\"ready\":true}";
        String user3Json = "{\"ID\":3,\"username\":\"Bud\",\"color\":\"GREEN\",\"score\":0,\"ready\":true}";

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
}