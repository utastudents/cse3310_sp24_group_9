package uta.cse3310;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import junit.framework.Assert;

public class GameTest {

    public void testAddUser() {
        Game game = new Game();

        game.addUser(1, "Adam");
        game.addUser(2, "Bob");

        assertEquals("Adam", game.getUserName(0));
        assertEquals("Bob", game.getUserName(1));
    }

    public void testGenerateRandomUniqueColor() {
        Game game = new Game();

        game.addUser(1, "Adam");
        game.addUser(2, "Bob");

        assertFalse(game.getUserColor(0).equals(game.getUserColor(1)));
    }

    public void testCreateGameConfirmButton() {
        Game game = new Game();

        String menuJson = game.gameMenu();

        assertTrue(menuJson.contains("confirmButton"));
    }

    public void testGameStart() {
        Game game = new Game();

        game.addUser(1, "Alice");
        game.addUser(2, "Bob");

        game.users.get(0).readyUp();
        game.users.get(1).readyUp();

        ArrayList<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("User Alice is ready");
        expectedOutput.add("User Bob is ready");
        expectedOutput.add("Game is ready to begin with 2 players");

        ArrayList<String> actualOutput = game.gameStart();

        assertEquals(expectedOutput.size(), actualOutput.size());
        for (int i = 0; i < expectedOutput.size(); i++) {
            assertEquals(expectedOutput.get(i), actualOutput.get(i));
        }
    }

    public void testDisplayPlayerInfo() {
        Game game = new Game();

        game.addUser(1, "Alice");
        game.addUser(2, "Bob");

        game.users.get(0).setScore(10);
        game.users.get(1).setScore(20);

        game.displayPlayerInfo();

        assertEquals(10, game.users.get(0).getScore());
        assertEquals(20, game.users.get(1).getScore());
    }

    public void testGameChat() {

        Game game = new Game();

        game.addUser(1, "Alice");
        game.addUser(2, "Bob");
        game.addUser(3, "Charlie");

        try {
            game.gameChatToJsonString("Hello everyone!", 1);
            // System.out.println("ChatData 1: " + game.chat);

            game.gameChatToJsonString("Hey Alice! How's it going?", 2);
            // System.out.println("ChatData 2: " + game.chat);

            game.gameChatToJsonString("I'm good, Bob! Excited for the game!", 1);
            // System.out.println("ChatData 3: " + game.chat);

            game.gameChatToJsonString("Me too guys don't forget about me!", 3);
            // System.out.println("ChatData 4: " + game.chat);

        } catch (Exception e) {
            junit.framework.Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    public void testDisplayScoreboard() {
        Game game = new Game();
    
        User[] users = new User[] {
            new User(1, "Alice", colors.RED, 100),
            new User(2, "Bob", colors.BLUE, 150),
            new User(3, "Charlie", colors.GREEN, 200)
        };
    
        Arrays.sort(users, (a, b) -> Integer.compare(b.getScore(), a.getScore()));
    
        game.users.addAll(Arrays.asList(users));
        game.displayScoreboard();
    
        assertEquals("Charlie", game.users.get(0).getName());
        assertEquals("Bob", game.users.get(1).getName());
        assertEquals("Alice", game.users.get(2).getName());
    }
    

    public void testGameWaiting() {
        Game game = new Game();
        int serverID = 123;
        String expectedOutput = "Game Waiting Menu for Server 123 to start: \n" +
                                "Players waiting: \n" +
                                "{\"name\":\"User1\",\"ready\":false}\n" +
                                "{\"name\":\"User2\",\"ready\":false}\n" +
                                "{\"name\":\"User3\",\"ready\":false}\n" +
                                "Play again button works, this will have to take to the waiting lobby again so probably call gamesWaiting() again\n";

        game.users = new ArrayList<>();
        game.users.addAll(Arrays.asList(
                new User(1, "User1", colors.RED),
                new User(2, "User2", colors.BLUE),
                new User(3, "User3", colors.GREEN)));

        String actualOutput = "Game Waiting Menu for Server " + serverID + " to start: \n" +
                              "Players waiting: \n";

        for (User user : game.users) {
            actualOutput += "{\"name\":\"" + user.getName() + "\",\"ready\":" + user.isReady() + "}\n";
        }

        actualOutput += "Play again button works, this will have to take to the waiting lobby again so probably call gamesWaiting() again\n";

        assertEquals(expectedOutput, actualOutput);
    }

    public void testUpdateScoreboard() {
        // Create a Game instance
        Game game = new Game();

        // Add some users to the game
        game.addUser(1, "Alice");
        game.addUser(2, "Bob");
        game.addUser(3, "Charlie");

        // Set scores for the users
        game.users.get(0).setScore(100);
        game.users.get(1).setScore(150);
        game.users.get(2).setScore(200);

        // Update the scoreboard with a word
        String word = "apple"; // Assuming "apple" is found
        game.updateScoreboard(word);

        // Assert that scores are greater than 0 after updating
        assertTrue(game.users.get(0).getScore() > 0);
        assertTrue(game.users.get(1).getScore() > 0);
        assertTrue(game.users.get(2).getScore() > 0);
    }



    public static void testLeave() {
        User user1 = new User(1, "Alice", colors.RED, 100);
        User user2 = new User(2, "Bob", colors.BLUE, 150);
        User user3 = new User(3, "Charlie", colors.GREEN, 200);
    
        Game game = new Game();
        game.users.add(user1);
        game.users.add(user2);
        game.users.add(user3);
        game.Leave();
    
        assertEquals(0, user1.getScore());
        assertEquals(0, user2.getScore());
        assertEquals(0, user3.getScore());
    
        assertTrue(game.users.isEmpty());
    }
    
    public void testGameEnd() {
        Game game = new Game();
    
        User user1 = new User(1, "Alice", colors.RED, 100);
        User user2 = new User(2, "Bob", colors.BLUE, 150);
        User user3 = new User(3, "Charlie", colors.GREEN, 200);
    
        game.users = new ArrayList<>(Arrays.asList(user1, user2, user3));
        game.displayScoreboard();
    
        PriorityQueue<User> leaderboard = new PriorityQueue<>((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        for (User user : game.users) {
            leaderboard.add(user);
        }
    
        assertEquals("Charlie", leaderboard.poll().getName());
        assertEquals("Bob", leaderboard.poll().getName());
        assertEquals("Alice", leaderboard.poll().getName());
    }
    
public void testWordFound() {
        // Create a WordGrid instance
        WordGrid wordGrid = new WordGrid();

        // Set up the WordGrid object as needed
        wordGrid.wordBankMap.put(1, "apple");
        wordGrid.wordBankMap.put(2, "banana");
        wordGrid.wordBankMap.put(3, "orange");

        // Create a Game instance
        Game game = new Game();

        // Test wordFound method with words that should be found
        assertTrue(game.wordFound("apple", wordGrid.wordBankMap));
        assertTrue(game.wordFound("banana", wordGrid.wordBankMap));
        assertTrue(game.wordFound("orange", wordGrid.wordBankMap));

        // Test wordFound method with a word that should not be found
        assertFalse(game.wordFound("grape", wordGrid.wordBankMap));
}


    // // Fix later
    // public void testGameDataToString() {
    //     Game game = new Game();

    //     User user1 = new User(1, "John", colors.BLUE, 5);
    //     User user2 = new User(2, "Jane", colors.RED, 10);
    //     User user3 = new User(3, "Jake", colors.GREEN, 15);
    //     User user4 = new User(4, "Jill", colors.PURPLE, 25);

    //     game.users = new ArrayList<>();
    //     game.users.add(user1);
    //     game.users.add(user2);
    //     game.users.add(user3);
    //     game.users.add(user4);

    //     game.gameChatToJsonString("Hello everyone!", 1);
    //     game.gameChatToJsonString("Hey Alice! How's it going?", 2);
    //     game.gameChatToJsonString("I'm good, Bob! Excited for thegame!", 1);
    //     game.gameChatToJsonString("Me too guys don't forget aboutme!", 3);

    //     game.fillGrid();

    //     String GameData = game.gameDataToString();

    //     System.out.println(GameData);
    // }
}