package uta.cse3310;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Game {
    private int gameId;
    private String gameName;
    private boolean joinable;
    private int maxBoardSize = 50;
    private WordGrid wordGrid;
    public ArrayList<User> users;
    private int gameCount;
    public String chat;
    private boolean playable;
    private int timers;
    private ArrayList<String> previousUsers = new ArrayList<>();
    private ArrayList<String> previousMessages = new ArrayList<>();
    private ArrayList<String> WordsFound = new ArrayList<>();
    private int incMax = 2; 
    // Constructor that creates a new game, this assumes that the game has not been
    // started
    
    /*
     * Constructor Game() creates a new game with the set variables.
     * This is assuming that a new game has been created and has not 
     * been started yet.
     */
    public Game() {
        this.gameId = 0;
        this.users = new ArrayList<>();
        this.timers = 30;
        this.gameCount = 0;
        this.wordGrid = new WordGrid();
        this.chat = "";
        this.playable = false;

    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setServerName(String gameName) {
        this.gameName = gameName;
    }

    public String getServerName() {
        return gameName;
    }

    public void setJoinable(boolean joinable) {
        this.joinable = joinable;
    }

    public boolean getJoinable() {
        return joinable;
    }

    public String getUserName(int index) {
        return users.get(index).getName();
    }

    public colors getUserColor(int index) {
        return users.get(index).getColor();
    }

    public User getUser(int index) {
        return users.get(index);
    }

    public void removeUser(String username) {
        for (User user : users) {
            if (user.getName().equals(username)) {
                users.remove(user);
                System.out.println("User " + username + " removed from the game.");
                break;
            }
        }
    }

    // print all users 
    public void printUsers() {
        // if there are no users in the game, print a message
        if (users.isEmpty()) {
            System.out.println("No users in the game.");
        } else {
            // print all users and ID in game
            for (User user : users) {
                System.out.println("User " + user.getName() + " ID: " + user.getID());
            }
        }
    }

    public ArrayList<String> getUserList() {
        ArrayList<String> userList = new ArrayList<>();
        ArrayList<User> users = this.users;

        for (User user : users) {
            userList.add(user.getName());
        }
        return userList;
    }

    public List<String> getUserReadyListAsString() {
        List<String> userReadyList = new ArrayList<>();
        List<Boolean> userReadyBooleans = getUserReadyList();

        for (Boolean isReady : userReadyBooleans) {
            userReadyList.add(Boolean.toString(isReady));
        }
        return userReadyList;
    }

    public ArrayList<Boolean> getUserReadyList() {
        ArrayList<Boolean> userReadyList = new ArrayList<>();
        ArrayList<User> users = this.users;

        for (User user : users) {
            userReadyList.add(user.isReady());
        }
        return userReadyList;
    }

    public boolean isReady(User user) {
        return user.isReady();
    }

    /*
     * Method gameMenu() shall allow the user to create a 
     * game from the game menu by pressing on the create 
     * button.
     */
     public String gameMenu() {
        JsonObject createGameMenuJson = new JsonObject();
        createGameMenuJson.addProperty("message", "Create Game Menu");
        createGameMenuJson.addProperty("confirmButton", "Confirm");

        Gson gson = new Gson();
        String json = gson.toJson(createGameMenuJson);
        Buttons.confirmButton();

        return json;
    }

    /*
     * Method addUser() shall take user ID and their username, checking
     * if there is an empty slot for that lobby, as well as adding
     * a random color to each user that is added.
     */
    public void addUser(int ID, String userName) {
        // Find an empty slot to add the user
        if (users.size() < 5) {
            users.add(new User(ID, userName, generateRandomUniqueColor()));
            // System.out.println("User " + userName + " added to the game.");
        } else {
            // If no empty slot found, print a message
            System.out.println("Unable to add user " + userName + ". The game is full.");
        }
    }

    /*
     * Method removeUser() shall take the username, checking
     * through every user in the given list, if it matches
     * the taken username, remove that user from the game.
     */


    // gameEnd method that returns a json string of the end leaderboard
    String gameEnd(int gameId) {
        // return a json string of end leaderboard
        Gson gson = new Gson();

        JsonObject endGameData = new JsonObject();
        
        // add user data
        JsonArray userDataArray = new JsonArray();
        for (User user : users) {
            String userDataJson = user.userJson();
            userDataArray.add(gson.fromJson(userDataJson, JsonObject.class));
        }
        endGameData.add("userData", userDataArray);

        return gson.toJson(endGameData);
    }

    
    /*
     * Method generateRandomUniqueColor() shall generate
     * a random color from the enum of colors, and assign
     * a unique random color to each user in the list.
     */
    private colors generateRandomUniqueColor() {
        Random random = new Random();
        colors[] allColors = colors.values();

        Set<colors> usedColors = new HashSet<>();
        for (User user : users) {
            if (user != null) {
                usedColors.add(user.getColor());
            }
        }

        colors randomColor;
        do {
            randomColor = allColors[random.nextInt(allColors.length)];
        } while (usedColors.contains(randomColor));

        return randomColor;
    }

    /*
     * Method gameWaiting() checks the serverID, if serverID is negative it doesn't
     * exist, however if serverID is positive, it prints out serverID,
     * and iterates through the players that're waiting in the lobby. A playAgain
     * button is used to configure playAgain action, monitoring user clicks.
     */
    void gameWaiting(int serverID) {
        if (serverID < -1) {
            System.out.println("User is not in any server.");
        } else if (users == null) {
            System.out.println("No users in the server.");
        } else {
            // Display the game waiting menu
            System.out.println("Game Waiting Menu for Server " + serverID + " to start: ");
            // Implement displaying players waiting in the specified server, if needed
            System.out.println("Players waiting: ");
            Gson gson = new Gson();

            for (User concurrentUser : users) {
                // create a json object for each user name and ready status
                JsonObject userJson = new JsonObject();
                if (concurrentUser != null) {
                    userJson.addProperty("name", concurrentUser.getName());
                    userJson.addProperty("ready", concurrentUser.isReady());
                }

                String json = gson.toJson(userJson);
                System.out.println(json);
            }
            
            Buttons.playAgainButton();
        }
    }

    /*
     * Method readyFlip() shall take a specific username.
     * Iterating through the userlist, checking if there
     * is a name equal to given user, then ready them up.
     */
    public void readyFlip(String username) {
        for (User user : users) {
            if (user.getName().equals(username)) {
                user.readyUp();
            }
        }
    }
    /*
     * Method gameStart() checks that each user is ready, if so, increment the
     * readyCount. Game shall begin once two to four members are ready
     * and display/fill the word grid.
     */
    boolean gameStart() {    
        int readyCount = 0;
        for (User concurrentUser : users) {
            if (concurrentUser.isReady()) {
                readyCount++;
            }
        }
        if (readyCount >= 2 && readyCount <= 4) {
            fillGrid();
            return true;
        } else {
            return false;
        }
    }

    /*
     * Method gameChat() enables in-game messaging between all players
     * in the game. The users can send messages without time limits,
     * and the chat functionality is triggered by pressing the chat button.
     */
    public void gameChatToJsonString(String message, String username) {
        User currentUser = null;
        for (User user : users) {
            if (user.getName().equals(username)) {
                currentUser = user;
                break;
            }
        }

        int indexStart = Math.max(0, previousUsers.size() - incMax);
        incMax++;
        String userName = currentUser != null ? currentUser.getName() : "";

        JsonObject chatDataObject = new JsonObject();
        JsonArray userNameArray = new JsonArray();
        JsonArray userChatMessagesArray = new JsonArray();

        if (!previousMessages.isEmpty() && !previousUsers.isEmpty()) {
            previousUsers.add(userName);
            previousMessages.add(message);
        } else {
            previousUsers.add(userName);
            previousMessages.add(message);
        }

        for (int i = indexStart; i < previousUsers.size(); i++) {
            userNameArray.add(previousUsers.get(i));
            userChatMessagesArray.add(previousMessages.get(i));
        }

        chatDataObject.add("username", userNameArray);
        chatDataObject.add("userChatMessages", userChatMessagesArray);

        JsonObject combineUserAndChat = new JsonObject();
        combineUserAndChat.add("ChatData", chatDataObject);

        Gson gson = new Gson();
        chat = gson.toJson(combineUserAndChat);
    }

    /*
     * Method gameDataToString() will store the gameId, gameName, joinability, chat
     * as JSON data into gameData. As well as adding the userData and wordGridData
     * into gameData. Returning it into a string.
     */
    public String gameDataToString() {
        Gson gson = new Gson();

        JsonObject gameData = new JsonObject();

        gameData.addProperty("gameId", gameId);
        gameData.addProperty("gameName", gameName);
        gameData.addProperty("joinable", joinable);
        gameData.addProperty("chat", chat);

        JsonArray userDataArray = new JsonArray();
        for (User user : users) {
            String userDataJson = user.userJson();
            userDataArray.add(gson.fromJson(userDataJson, JsonObject.class));
        }
        gameData.add("userData", userDataArray);

        String wordGridDataJson = wordGrid.wordGridJson();
        gameData.add("wordGridData", gson.fromJson(wordGridDataJson, JsonObject.class));

        return gson.toJson(gameData);
    }

   /*
     * Method DisplayInfo() iterates through each user
     * and checks if that user exists, display the
     * user and their score.
     */
    public void displayPlayerInfo() {
        // might have to use user json
        for (User user : users) {
            if (user != null) {
                System.out.println("Name: " + user.getName() + " Score: " + user.getScore());
            }
        }
    }

    /*
     * Method fillGrid() shall fill in the wordGrid with a
     * set of random letters, and actual words so that
     * there will be an actual word grid created.
     */
    public void fillGrid() {
        wordGrid.WordFill();
    }

    /*
     * Method checkWord() shall check a given set of coordinates for a
     * given user. Checking the wordGrid at the given coordinates,
     * if the word is a part of the current games wordbank, store it.
     */
    public boolean checkWord(int x1, int y1, int x2, int y2, String username) {

        User user = null;
        for (User currentUser : users) {
            if (currentUser.getName().equals(username)) {
                user = currentUser;
            }
        }

        Object[] result = wordGrid.removeWord(x1, y1, x2, y2);
        boolean boolResult = (boolean) result[0];
        if (boolResult) {
            String word = (String) result[1];
            user.updateUserWords(word);
        } else {
            System.out.println("This word is invalid or not part of this games wordbank");
        }
        return boolResult;
    }

    /*
     * Method wordFound() shall check if a specific word is found through
     * the HashMap. If the HashMap contains that word, then the word is
     * found, otherwise that word is not found.
     */
    public boolean wordFound(String word, HashMap<Integer, String> wordBankMap) {
        if(wordBankMap.containsValue(word)){
            System.out.println("Word found: " + word);
            return true;
        }
        System.out.println("Word not found: " + word);
        return false;
    }

    /*
     * Method updateScoreboard() updates the scoreboard by adjusting the users
     * score based on how many words that is found in the WSG. The connected
     * users' scores are sorted and updated, displaying names along with scores, as
     * for the disconnected users' score, it will display at the end of the list.
     */
    public void updateScoreboard(String word) {
        ArrayList<User> connectedUsers = new ArrayList<>();
        ArrayList<User> disconnectedUsers = new ArrayList<>();

        for (User concurrentUser : users) {
            if (concurrentUser.getScore() > -1) {
                if (wordFound(word,wordGrid.wordBankMap) == true) {
                    concurrentUser.updateUserWords(word); // Updates score
                }
                connectedUsers.add(concurrentUser);
            } else {
                disconnectedUsers.add(concurrentUser);
            }
        }

        connectedUsers.sort((u1, u2) -> Integer.compare(u2.getScore(), u1.getScore()));
        connectedUsers.addAll(disconnectedUsers);

        for (User concurrentUser : connectedUsers) {
            System.out.println("User " + concurrentUser.getName() + " Score: " + concurrentUser.getScore());
        }
        for (User concurrentUser : disconnectedUsers) {
            System.out.println("User " + concurrentUser.getName() + " Disconnected");
        }
    }
    
    /*
     * Method Leave() shall iterate through each user inside of the object.
     * If the user leaves the game, reset the users' score to zero,
     * and take the user back to the game menu.
     */
    void Leave() {
        for (User user : users) {
            if (user != null) {
                user.setScore(0);
            }
        }
        gameMenu();
    }

    /*
     * Method displayScoreboard() uses a queue with all the users.
     * Iterates through each user, and add the user to the leaderboard queue
     * then while the leaderboard isn't empty, print out the final leaderboard.
     */
    public void displayScoreboard() {
        int rank = 1;
        PriorityQueue<User> leaderboard = new PriorityQueue<>((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        for (User user : users) {
            leaderboard.add(user);
        }

        System.out.println("Leaderboard:");
        while (!leaderboard.isEmpty()) {
            User currentUser = leaderboard.poll();
            System.out.println(rank + ". " + currentUser.getName() + " - Score: " + currentUser.getScore());
            rank++;
        }

        Buttons.playAgainButton();
        Buttons.leaveButton();
    }

    /*
     *   The public static class Buttons can be accessed from any other classes,
     *   just as long as it is declared in the same package as the same class
     *   that it needs to be used in, with no issues.
    */    
    public static class Buttons{
        public static void playAgainButton() {
            // System.out.println("play again button");
        }

        public static void confirmButton() {
            // System.out.println("create again button");
        }

        public static void leaveButton() {
            // System.out.println("leave button");
        }

        public static void chatButton() {
            // System.out.println("chat button");
        }
    }

}