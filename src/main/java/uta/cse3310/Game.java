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

    /*
     * Constructor Game() creates a new game with the set variables.
     * This is assuming that a new game has been created and has not 
     * been started yet.
     */
    public Game(){
        this.gameId = 0;
        this.users = new ArrayList<>();
        this.timers = 30;
        this.gameCount = 0;
        this.wordGrid = new WordGrid();
        this.chat = "";
        this.playable = false;
        this.joinable = true;
    }

    public void setGameId(int gameId){
        this.gameId = gameId;
    }

    public int getGameId(){
        return gameId;
    }

    public void setServerName(String gameName){
        this.gameName = gameName;
    }

    public String getServerName(){
        return gameName;
    }

    public void setJoinable(boolean joinable){
        this.joinable = joinable;
    }

    public boolean getJoinable(){
        return joinable;
    }

    public String getUserName(int index){
        return users.get(index).getName();
    }

    public colors getUserColor(int index){
        return users.get(index).getColor();
    }

    public User getUser(int index){
        return users.get(index);
    }
    
    public ArrayList<String> getUserList(){
        ArrayList<String> userList = new ArrayList<>();
        ArrayList<User> users = this.users;

        for(User user : users){
            userList.add(user.getName());
        }

        return userList;
    }

    public String getUserListJson(){
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();

        for(User user : users){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("ID", user.getID());
            jsonObject.addProperty("username", user.getName());
            jsonObject.addProperty("color", user.getColor().toString()); // Convert Color to String
            jsonObject.addProperty("score", user.getScore()); // Add score property
            jsonObject.addProperty("ready", user.isReady()); // Add ready status property
            jsonArray.add(jsonObject);
        }
        
        return gson.toJson(jsonArray);
    }
    
    
    public String getUserJsonByID(int userID){
        for(User user : users){
            if(user.getID() == userID){
                return user.userJson();
            }
        }
        return null;
    }

    public List<String> getUserReadyListAsString(){
        List<String> userReadyList = new ArrayList<>();
        List<Boolean> userReadyBooleans = getUserReadyList();

        for(Boolean isReady : userReadyBooleans){
            userReadyList.add(Boolean.toString(isReady));
        }
        return userReadyList;
    }

    public ArrayList<Boolean> getUserReadyList(){
        ArrayList<Boolean> userReadyList = new ArrayList<>();
        ArrayList<User> users = this.users;

        for(User user : users){
            userReadyList.add(user.isReady());
        }
        return userReadyList;
    }

    public boolean isReady(User user){
        return user.isReady();
    }

    /*
     * Method gameMenu() shall allow the user to create a 
     * game from the game menu by pressing on the create 
     * button. Returning a JSON string of this.
     */
     public String gameMenu(){
        JsonObject createGameMenuJson = new JsonObject();
        createGameMenuJson.addProperty("message", "Create Game Menu");
        createGameMenuJson.addProperty("confirmButton", "Confirm");

        Gson gson = new Gson();
        String json = gson.toJson(createGameMenuJson);

        return json;
    }
    /*
     * Method addUser() shall take user ID and their username, checking
     * if there is an empty slot for that lobby, as well as adding
     * a random color to each user that is added.
     */
    public void addUser(int ID, String userName){
        for(int i = 0; i < users.size(); i++){
            if(userName.equals(users.get(i).getName())){
                System.out.println("Unable to add user " + userName + ". There is a user with the same name in this game.");
                joinable = false;
                return;
            }
        }
        if((users.size() < 5) ){
            users.add(new User(ID, userName, generateRandomUniqueColor()));
            System.out.println("User " + userName + " added to the game.");
        }else{
            joinable = false;
            System.out.println("Unable to add user " + userName + ". The game is full.");
        }
    }

    /*
     * Method genColorTest() is the same as generateRandomUniqueColor()
     * but this is used for the test case, so that we get the colors
     * in the order of the enums.
     */
    public void genColorTest(int ID, String userName, colors color, boolean ready, int score){
        for(int i = 0; i < users.size(); i++){
            if(userName.equals(users.get(i).getName()) && !ready){ // Check if the user is not ready
                System.out.println("Unable to add user " + userName + ". There is a user with the same name in this game. And not ready");
                joinable = false;
                return;
            }
        }
        if((users.size() < 5)){
            users.add(new User(ID, userName, color, score));
            System.out.println("User " + userName + " added to the game.");
        }else{
            joinable = false;
            System.out.println("Unable to add user " + userName + ". The game is full.");
        }
    }
    
    public void addUserFromJson(String userJson){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(userJson, JsonObject.class);
    
        int userID = jsonObject.get("ID").getAsInt();
        String userName = jsonObject.get("username").getAsString();
        String colorName = jsonObject.get("color").getAsString();
        String readyStr = jsonObject.get("ready").getAsString(); // Get as String to perform validation
        int score = jsonObject.get("score").getAsInt();
        colors color = colors.valueOf(colorName.toUpperCase());

        if(!readyStr.equals("true") && !readyStr.equals("false")){
            System.out.println("Invalid value for ready field: " + readyStr);
            if(readyStr.equals("false")){
                return;
            }
        }

        if(score < 0){
            System.out.println("THIS IS NOT A VALID SCORE");
            return;
        }
        boolean ready = Boolean.parseBoolean(readyStr); // Convert String to boolean
        genColorTest(userID, userName, color, ready, score);
    }

    /*
     * Method removeUser() shall take the username, checking
     * through every user in the given list, if it matches
     * the taken username, remove that user from the game.
     */
    public void removeUser(String username){
        for(User user : users){
            if(user.getName().equals(username)){
                users.remove(user);
                System.out.println("User " + username + " removed from the game.");
                break;
            }
        }
    }

    public void removeUserFromJson(String userDataJson){
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(userDataJson, JsonObject.class);

        if(jsonObject == null || !jsonObject.has("ID") || !jsonObject.has("username")){
            System.out.println("Error: Invalid user data provided.");
            return;
        }

        int userID = jsonObject.get("ID").getAsInt();
        String username = jsonObject.get("username").getAsString();

        for(int i = 0; i < users.size(); i++){
            User currentUser = users.get(i);
            if(currentUser.getID() == userID && currentUser.getName().equals(username)){
                users.remove(i);
                System.out.println("User " + username + " removed from the game.");
                if(users.size() < 5){
                    joinable = true;
                }
                return;
            }
        }

        System.out.println("User " + username + " not found in the game.");
    }

    /*
     * Method printUser() shall check if the user list is
     * empty or not. If it is empty, print out no users,
     * however, if it is not empty, print the username and ID.
     */
    public void printUsers(){
        if(users.isEmpty()){
            System.out.println("No users in the game.");
        }else{
            for(User user : users){
                System.out.println("User " + user.getName() + " ID: " + user.getID());
            }
        }
    }

    /*
     * Method gameEnd() shall create a JSON Object and Array.
     * This will add the user data, and also check if the user size
     * is full or not. It will return a JSON string of the final leaderboard.
     */
    String gameEnd(int gameId){
        Gson gson = new Gson();

        JsonObject endGameData = new JsonObject();
        JsonArray userDataArray = new JsonArray();

        for(User user : users){
            String userDataJson = user.userJson();
            userDataArray.add(gson.fromJson(userDataJson, JsonObject.class));
        }

        if(users.size() < 5){
            joinable = true;
        }
        return gson.toJson(endGameData);
    }
    
    /*
     * Method generateRandomUniqueColor() shall generate
     * a random color from the enum of colors, and assign
     * a unique random color to each user in the list.
     */
    colors generateRandomUniqueColor(){
        Random random = new Random();
        colors[] allColors = colors.values();

        Set<colors> usedColors = new HashSet<>();
        for(User user : users){
            if(user != null){
                usedColors.add(user.getColor());
            }
        }
        colors randomColor;
        do{
            randomColor = allColors[random.nextInt(allColors.length)];
        }while(usedColors.contains(randomColor));

        return randomColor;
    }

    /*
     * Method gameWaiting() checks the serverID, if serverID is negative it doesn't
     * exist, however if serverID is positive, it prints out serverID,
     * and iterates through the players that're waiting in the lobby.
     */
    void gameWaiting(int serverID){
        if(serverID < -1){
            System.out.println("User is not in any server.");
        }else if(users == null){
            System.out.println("No users in the server.");
        }else{
            System.out.println("Game Waiting Menu for Server " + serverID + " to start: ");
            System.out.println("Players waiting: ");

            Gson gson = new Gson();

            for(User concurrentUser : users){
                JsonObject userJson = new JsonObject();
                if(concurrentUser != null){
                    userJson.addProperty("name", concurrentUser.getName());
                    userJson.addProperty("ready", concurrentUser.isReady());
                }

                String json = gson.toJson(userJson);
                System.out.println(json);
            }
            
        }
    }

    /*
     * Method readyFlip() shall take a specific username.
     * Iterating through the userlist, checking if there
     * is a name equal to given user, then ready them up.
     */
    public void readyFlip(String username){
        for(User user : users){
            if(user.getName().equals(username)){
                user.readyUp();
            }
        }
    }

    /*
     * Method gameStart() checks that each user is ready, if so, increment the
     * readyCount. Game shall begin once two to four members are ready
     * and display/fill the word grid.
     */
    boolean gameStart(){    

        if(joinable == false){
            return false;
        }

        int readyCount = 0;
        List<String> readyUsers = new ArrayList<>();
    
        for(User concurrentUser : users){
            if(concurrentUser.isReady()){
                readyCount++;
                readyUsers.add(concurrentUser.getName());
            }
        }
    
        if(readyCount >= 2 && readyCount <= 4){
            fillGrid();
            joinable = false;
            return true;
        }else{
            return false;
        }
    }
    
    /*
     * Method gameChat() enables in-game messaging between all players
     * in the game. The users can send messages without time limits,
     * and the chat functionality is triggered by pressing the chat button.
     */
    public String gameChatToJsonString(String message, String username){
        User currentUser = null;
        for(User user : users){
            if(user.getName().equals(username)){
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

        if(!previousMessages.isEmpty() && !previousUsers.isEmpty()){
            previousUsers.add(userName);
            previousMessages.add(message);
        }else{
            previousUsers.add(userName);
            previousMessages.add(message);
        }

        for(int i = indexStart; i < previousUsers.size(); i++){
            userNameArray.add(previousUsers.get(i));
            userChatMessagesArray.add(previousMessages.get(i));
        }

        chatDataObject.add("username", userNameArray);
        chatDataObject.add("userChatMessages", userChatMessagesArray);

        JsonObject combineUserAndChat = new JsonObject();
        combineUserAndChat.add("ChatData", chatDataObject);

        Gson gson = new Gson();
        chat = gson.toJson(combineUserAndChat);
        return chat;
    }

    /*
     * Method gameDataToString() will store the gameId, gameName, joinability, chat
     * as JSON data into gameData. As well as adding the userData and wordGridData
     * into gameData. Returning it into a string.
     */
    public String gameDataToString(){
        Gson gson = new Gson();
        JsonObject gameData = new JsonObject();

        gameData.addProperty("gameId", gameId);
        gameData.addProperty("gameName", gameName);
        gameData.addProperty("joinable", joinable);
        gameData.addProperty("chat", chat);

        JsonArray userDataArray = new JsonArray();
        for(User user : users){
            String userDataJson = user.userJson();
            userDataArray.add(gson.fromJson(userDataJson, JsonObject.class));
        }

        gameData.add("userData", userDataArray);
        String wordGridDataJson = wordGrid.wordGridJson();
        gameData.add("wordGridData", gson.fromJson(wordGridDataJson, JsonObject.class));

        return gson.toJson(gameData);
    }

   /*
     * Method displayPlayerInfo() iterates through each user
     * and checks if that user exists, display the
     * user and their score.
     */
    public void displayPlayerInfo(){
        for(User user : users){
            if(user != null){
                System.out.println("Name: " + user.getName() + " Score: " + user.getScore());
            }
        }
    }

    /*
     * Method fillGrid() shall fill in the wordGrid with a
     * set of random letters, and actual words so that
     * there will be an actual word grid created.
     */
    public void fillGrid(){
        wordGrid.WordFill();
    }

    /*
     * Method checkWord() shall check a given set of coordinates for a
     * given user. Checking the wordGrid at the given coordinates,
     * if the word is a part of the current games wordbank, store it.
     */
    public boolean checkWord(int x1, int y1, int x2, int y2, String username){

        User user = null;
        for(User currentUser : users){
            if(currentUser.getName().equals(username)){
                user = currentUser;
            }
        }

        Object[] result = wordGrid.removeWord(x1, y1, x2, y2);
        boolean boolResult = (boolean) result[0];
        if(boolResult){
            String word = (String) result[1];
            user.updateUserWords(word);
        }else{
            System.out.println("This word is invalid or not part of this games wordbank");
        }
        return boolResult;
    }

    /*
     * Method wordFound() shall check if a specific word is found through
     * the HashMap. If the HashMap contains that word, then the word is
     * found, otherwise that word is not found.
     */
    public boolean wordFound(String word, HashMap<Integer, String> wordBankMap){
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
    public void updateScoreboard(String word){
        ArrayList<User> connectedUsers = new ArrayList<>();
        ArrayList<User> disconnectedUsers = new ArrayList<>();

        for(User concurrentUser : users){
            if(concurrentUser.getScore() > -1){
                if(wordFound(word,wordGrid.wordBankMap) == true){
                    concurrentUser.updateUserWords(word); // Updates score
                }
                connectedUsers.add(concurrentUser);
            }else{
                disconnectedUsers.add(concurrentUser);
            }
        }

        connectedUsers.sort((u1, u2) -> Integer.compare(u2.getScore(), u1.getScore()));
        connectedUsers.addAll(disconnectedUsers);

        for(User concurrentUser : connectedUsers){
            System.out.println("User " + concurrentUser.getName() + " Score: " + concurrentUser.getScore());
        }

        for(User concurrentUser : disconnectedUsers){
            System.out.println("User " + concurrentUser.getName() + " Disconnected");
        }
    }

    public String updateScoreboardToJsonString(String word, String disconnectedUserJson){
        ArrayList<User> connectedUsers = new ArrayList<>();
        ArrayList<User> disconnectedUsers = new ArrayList<>();

        Gson gson = new Gson();
        JsonObject disconnectedUserJsonObject = gson.fromJson(disconnectedUserJson, JsonObject.class);
        String disconnectedUsername = disconnectedUserJsonObject.get("username").getAsString();

        for(User concurrentUser : users){
            if(concurrentUser.getName().equals(disconnectedUsername)){
                disconnectedUsers.add(concurrentUser);
                System.out.println("User " + concurrentUser.getName() + " left");
                continue;
            }

            if(!disconnectedUsers.contains(concurrentUser)){
                if(concurrentUser.getScore() > -1){
                    System.out.println("Checking word for user " + concurrentUser.getName() + ": " + word);
                    if(wordFound(word, wordGrid.wordBankMap)){
                        concurrentUser.updateUserWords(word);
                    }
                }else{
                    disconnectedUsers.add(concurrentUser);
                }
                connectedUsers.add(concurrentUser);
            }
        }

        connectedUsers.sort((u1, u2) -> Integer.compare(u2.getScore(), u1.getScore()));
        connectedUsers.addAll(disconnectedUsers);
    
        for(User concurrentUser : connectedUsers){
            System.out.println("User " + concurrentUser.getName() + " Score: " + concurrentUser.getScore());
        }

        for(User concurrentUser : disconnectedUsers){
            System.out.println("User " + concurrentUser.getName() + " Disconnected prior to searching for word. Not included.");
        }
    
        JsonObject scoreboardJson = new JsonObject();
        JsonArray usernameArray = new JsonArray();
        JsonArray userScoreArray = new JsonArray();
    
        for(User user : connectedUsers){
            usernameArray.add(user.getName());
            userScoreArray.add(user.getScore());
        }
    
        scoreboardJson.add("username", usernameArray);
        scoreboardJson.add("userScore", userScoreArray);
    
        return gson.toJson(scoreboardJson);
    }
    
    /*
     * Method Leave() shall iterate through each user inside of the object.
     * If the user leaves the game, reset the users' score to zero,
     * and take the user back to the game menu.
     */
    void Leave(){
        for(User user : users){
            if(user != null){
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
    public void displayScoreboard(){
        int rank = 1;
        PriorityQueue<User> leaderboard = new PriorityQueue<>((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        for(User user : users){
            leaderboard.add(user);
        }

        System.out.println("Leaderboard:");
        while(!leaderboard.isEmpty()){
            User currentUser = leaderboard.poll();
            System.out.println(rank + ". " + currentUser.getName() + " - Score: " + currentUser.getScore());
            rank++;
        }

    }

    public String displayScoreboardInJson(){
        PriorityQueue<User> leaderboard = new PriorityQueue<>((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        leaderboard.addAll(users);
        JsonObject scoreboardObject = new JsonObject();

        while(!leaderboard.isEmpty()){
            User currentUser = leaderboard.poll();
            scoreboardObject.addProperty(currentUser.getName(), currentUser.getScore());
        }

        return scoreboardObject.toString();
    }

}