package uta.cse3310;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class GameTest {

    public void testAddUser() {
        Game game = new Game();

        game.addUser(1, "Adam");
        game.addUser(2, "Bob");

        // Check if the users were added successfully
        assertEquals("Adam", game.getUserName(0));
        assertEquals("Bob", game.getUserName(1));


    }

    public void testGenerateRandomUniqueColor() {
        Game game = new Game();
        
        game.addUser(1, "Adam");
        game.addUser(2, "Bob");

        // Check if both users have unique colors
        assertFalse(game.getUserColor(0).equals(game.getUserColor(1)));
    }

    public void testCreateGameConfirmButton() {
        Game game = new Game();

        // Redirecting System.out to capture the output
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        game.createGame();

        // Get the captured output
        String output = outputStreamCaptor.toString().trim();

        // Verify that the output contains the expected confirmation message
        assertTrue(output.contains("Confirm button works"));
    }

    public void testGameStartWithTwoUsers() {
        Game game = new Game();

        game.addUser(1, "Alice");
        game.addUser(2, "Bob");

        game.users.get(0).readyUp();
        game.users.get(1).readyUp();


        String expectedOutput = "User Alice is ready\n" + //
                                "User Bob is ready\n" + //
                                "Game is ready to begin with 2 players";

        game.gameStart();

        assertEquals(expectedOutput, captureOutput(() -> game.gameStart()));
    }

    private String captureOutput(Runnable task) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        task.run();

        System.setOut(originalOut);
        return outContent.toString().trim();
    }

    // Redirect System.out to capture console output
    public void testDisplayPlayerInfo() {
        Game game = new Game();
        // Create an array of User objects
        game.users = new ArrayList<>();
        game.users.add(new User(1, "Alice", colors.RED));
        game.users.add(new User(2, "Bob", colors.BLUE));

        // Redirect System.out to a custom PrintStream for testing
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Call the method to display player info
        game.displayPlayerInfo(); // Calling the method directly

        // Expected output
        String expectedOutput = "Name: Alice Score: 0\nName: Bob Score: 0\n"; // Corrected format

        // Compare expected output with the actual output captured
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    public void testGameChat() {
        // Create a mock User object
        User currentUser = new User(1, "Alice", colors.RED);

        // Create a Game object
        Game game = new Game();

        // Call the gameChat method with a message and the current user
        try {
            game.gameChat("Hello everyone!", currentUser);
        } catch (Exception e) {
            junit.framework.Assert.fail("Exception thrown: " + e.getMessage());
        }
    }

    public void testDisplayScoreboard() {
        Game game = new Game();

        // Mock User objects with different scores
        game.users = new ArrayList<>();
        game.users.addAll(Arrays.asList(
            new User(1, "Alice", colors.RED),
            new User(2, "Bob", colors.BLUE),
            new User(3, "Charlie", colors.GREEN)
        ));

        // Set scores for each user
        game.users.get(0).score = 100;
        game.users.get(1).score = 150;
        game.users.get(2).score = 200;

        // Redirect System.out to a ByteArrayOutputStream for testing
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create a Game object

        // Call the displayScoreboard method
        game.displayScoreboard();

        // Expected output
        String expectedOutput = "Leaderboard:\n1. Charlie - Score: 200\n2. Bob - Score: 150\n3. Alice - Score: 100\n" +
                "Play again button works, this will have to take to the waiting lobby again so probably call gamesWaiting()\n"
                +
                "Leave button works\n";

        // Compare expected output with the actual output captured
        Assert.assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    public void testGameWaiting() {
        // Set up test data
        // Example server ID
        int serverID = 123;

        // Redirect System.out to a ByteArrayOutputStream for testing
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create a Game object
        Game game = new Game();

        game.users = new ArrayList<>();
        game.users.addAll(Arrays.asList(
            new User(1, "User1", colors.RED),
            new User(2, "User2", colors.BLUE),
            new User(3, "User3", colors.GREEN)
        ));

        // Call the gameWaiting method
        game.gameWaiting(serverID);

        // Expected output
        String expectedOutput = "Game Waiting Menu for Server 123 to start: \n" +
                                "Players waiting: \n" +
                                "{\"name\":\"User1\",\"ready\":false}\n" +
                                "{\"name\":\"User2\",\"ready\":false}\n" +
                                "{\"name\":\"User3\",\"ready\":false}\n" + 
                                "Play again button works, this will have to take to the waiting lobby again so probably call gamesWaiting() again\n";

        // Compare expected output with the actual output captured
        assertEquals(expectedOutput, outputStreamCaptor.toString());
    }

    public void testUpdateScoreboard() {
        // Create mock users
        User user1 = new User(1, "Alice", colors.RED);
        User user2 = new User(2, "Bob", colors.BLUE);
        User user3 = new User(3, "Charlie", colors.GREEN);

        // Set scores for each user
        user1.score = 100;
        user2.score = 150;
        user3.score = 200;

        // Create an array of users
        User[] users = { user1, user2, user3 };

        // Create an instance of Game
        Game game = new Game();

        // Call the method being tested
        game.updateScoreboard();

        // Assert that all connected users have positive scores
        for (User user : users) {
            assertTrue(user.score > 0);
        }
    }

    public static void testLeave() {
        // Create some mock users
        User user1 = new User(1, "Alice", colors.RED);
        User user2 = new User(2, "Bob", colors.BLUE);
        User user3 = new User(3, "Charlie", colors.GREEN);

        // Set scores for each user
        user1.score = 100;
        user2.score = 150;
        user3.score = 200;

        // Set up the game with users
        Game game = new Game();

        // Redirect System.out to a ByteArrayOutputStream for testing
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Call the Leave method
        game.Leave();

        // Verify that scores are reset
        assertEquals(0, user1.score);
        assertEquals(0, user2.score);
        assertEquals(0, user3.score);

        // Verify that the gameMenu method is called
        assertEquals("Your expected output here", outputStreamCaptor.toString().trim());
    }

    public void testGameEnd() {
        Game game = new Game();

        game.users = new ArrayList<>();
        game.users.addAll(Arrays.asList(
            new User(1, "Alice", colors.RED),
            new User(2, "Bob", colors.BLUE),
            new User(3, "Charlie", colors.GREEN)
        ));
        
        // Set scores for each user
        game.users.get(0).score = 100;
        game.users.get(1).score = 150;
        game.users.get(2).score = 200;


        // Redirect System.out to a ByteArrayOutputStream for testing
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        // Call the displayScoreboard method
        game.displayScoreboard();

        // Verify the output
        String expectedOutput = "Leaderboard:\n" +
                "1. Charlie - Score: 200\n" +
                "2. Bob - Score: 150\n" +
                "3. Alice - Score: 100\n" +
                "Play again button works, this will have to take to the waiting lobby again so probably call gamesWaiting()\n"
                +
                "Leave button works";
        assertEquals(expectedOutput, outputStreamCaptor.toString().trim());
    }
}
