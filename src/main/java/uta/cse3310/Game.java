package uta.cse3310;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.PriorityQueue;
import java.io.IOException;
import java.lang.Thread;
import javax.sql.rowset.WebRowSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class Game {
   private int gameId;
   private int maxBoardSize = 50;
   private char[][] grid = new char[maxBoardSize][maxBoardSize];
   User[] users;
   private int gameCount;
   private String chat;
   private WordBank wordsBank;
   private boolean playable;
   private int timers;


   // Constructor that creates a new game, this assumes that the game has not been started
   public Game(){
       this.gameId = 0; 
       this.users = new User[5];
       this.timers = 30;
       this.gameCount = 0;
       //this.wordGrid = new wordGrid();
       this.chat = "";
       this.playable = false;
   }


   // Creates WordBank object in java
   WordBank WordBank = new WordBank();


   /*
    * public void Game(User user[]) throws IOException{
      
       while(WordBank.wordsLeft() > 0)
       {
           String foundWord = chat; // Need to implement a user get input to change chat
           checkWord(foundWord, users, WordBank);
       }
       gameEnd(null, null);
   }
    *
    */
  
   public void gameMenu(){
       // A player will be able to create a game from the game menu by pressing the create button.
       createGame();
   }
  
   /*
       Method createGame() creates a game menu with a message & confirmation button,
       converting it to a JSON text format, in which it will print, and
       configuring a confirmation action, monitoring user clicks.
   */
  
   public void createGame(){


       JsonObject createGameMenuJson = new JsonObject();
       createGameMenuJson.addProperty("message", "Create Game Menu");
       createGameMenuJson.addProperty("confirmButton", "Confirm");


       Gson gson = new Gson();
       String json = gson.toJson(createGameMenuJson);
       System.out.println(json);


       Buttons confirm = new Buttons(){
           public void confirmButton(){
               System.out.println("Confirm button works");
           }
       };
       confirm.confirmButton();
   }


   /*
       Method gameStart() checks that each user is ready, if so, increment the
       readyCount. Game shall begin once two to four members are ready
       and display the word grid. Otherwise, print out waiting.
   */
   void gameStart(User[] user){
       int readyCount = 0;
       for(User concurrentUser : user){
           if(concurrentUser.ready == true){
               readyCount++;
           }
       }
       if(readyCount >= 2 && readyCount <= 4){
           System.out.println("Game is ready to begin with " + readyCount + " players");
           //Game(user); // Display word grid w/ the users
       }
       else{
           System.out.println("Waiting for more players to join...");
       }
   }
 
   /*
       Method gameEnd() shall check from the WordBank class if there is any words
       left. If there is none, call the method displayScoreboard() as well
       as returning a true value. Otherwise, return a false value and end WSG once all words found.
   */
   boolean gameEnd(){
       if(wordsBank.wordsLeft() == 0)
       {
           displayScoreboard(users);
           return true;
       }
       return false;
   }
  
   public boolean wordFound(String word){
       // TODO: implement
       return true;
   }
  
   /*
       Method Leave() shall iterate through each user inside of the object.
       If the user leaves the game, reset the users' score to zero,
       and call method gameMenu().
   */
   void Leave(){
       for(User user : users){
           if(user != null){
               user.score = 0;
           }
       }
       gameMenu(); // If player chooses to leave the game it returns to game Menu
   }


   /*
       Method updateScoreboard() updates the scoreboard by adjusting the users
       score based on how many words that is found in the WSG. The connected
       users' scores are sorted and updated, displaying names along with scores, as for
       the disconnected users' score, it will display at the end of the list.
   */


   public void updateScoreboard(User[] user){
       ArrayList<User> connectedUsers = new ArrayList<>();
       ArrayList<User> disconnectedUsers = new ArrayList<>();


       for(User concurrentUser : user){
           if(concurrentUser.score > 0){
               if(wordFound(chat)){
                   concurrentUser.updateUserWords(chat); // Updates score
               }
               connectedUsers.add(concurrentUser);
           }
           else{
               disconnectedUsers.add(concurrentUser);
           }
       }


       connectedUsers.sort((u1, u2) -> Integer.compare(u2.score, u1.score));
       connectedUsers.addAll(disconnectedUsers);


       for (User concurrentUser : connectedUsers){
           System.out.println("User " + concurrentUser.name + " Score: " + concurrentUser.score);
       }
       for (User concurrentUser : disconnectedUsers){
           System.out.println("User " + concurrentUser.name + " Disconnected");
       }
   }


   /*
       Method displayScoreboard() uses a queue with all the users.
       Iterates through each user, and add the user to the leaderboard
       queue, then while the leaderboard isn't empty, print out the final leaderboard.
   */
   public void displayScoreboard(User[] users){
       int rank = 1;
       PriorityQueue<User> leaderboard = new PriorityQueue<>((a, b) -> Integer.compare(b.score, a.score));


       for (User user : users){
           leaderboard.add(user);
       }


       System.out.println("Leaderboard:");
       while (!leaderboard.isEmpty()){
           User currentUser = leaderboard.poll();
           System.out.println(rank + ". " + currentUser.name + " - Score: " + currentUser.score);
           rank++;
       }


       Buttons playAgainAndLeave = new Buttons(){
           public void playAgainButton(){
               System.out.println("Play again button works, this will have to take to the waiting lobby again so probably call gamesWaiting()");
           }
           public void leaveButton(){
               System.out.println("Leave button works");
           }
       };
       playAgainAndLeave.playAgainButton();
       playAgainAndLeave.leaveButton();
   }


   /*
       public void checkWord(String foundwords, User user[]) {
           // TODO: implement
           HashMap<String, String> Map = new HashMap<>();
           Map.put();
       }
   */


   /*
       Method gameWaiting() checks the serverID, if serverID is negative it doesn't
       exist, however if serverID is positive, it prints out serverID,
       and iterates through the players that're waiting in the lobby. A playAgain
       button is used to configure playAgain action, monitoring user clicks.
   */
   void gameWaiting(int serverID, User[] users){
       if(serverID < -1){
           System.out.println("User is not in any server.");
       }
       else{
           // Display the game waiting menu
           System.out.println("Game Waiting Menu for Server " + serverID + " to start: ");
           // Implement displaying players waiting in the specified server, if needed
           System.out.println("Players waiting: ");
           for(User concurrentUser : users){
               System.out.println(concurrentUser.name + " ");
           }
          
           Buttons playAgain = new Buttons(){
               public void playAgainButton(){
                   System.out.println("Play again button works, this will have to take to the waiting lobby again so probably call gamesWaiting() again");
               }
           };
           playAgain.playAgainButton();
       }
   }


   /*
       Method DisplayInfo() iterates through each user
       and checks if that user exists, display the
       user and their score.
   */
   public void DisplayPlayerInfo(User[] users){
       // might have to use user json
       for(User user : users){
           if(user != null){
               // String PlayerInfo[] = {
               //     "Name:" + user.name + "Score: " + user.score
               // };
               // System.out.println(PlayerInfo);
               System.out.println("Name: " + user.name + " Score: " + user.score);
           }
       }
   }


   /*
      Method ResetTimer() will reset the timer to 30 seconds everytime
       the function is called
   */
   private void ResetTimer(){
       this.timers = 30;
   }


   public void hintWordGrid(){
       //find a random word in the hashmap and display one character
       ResetTimer();
  }


   /*
       Method gameChat() enables in-game messaging between all players
       in the game. The users can send messages without time limits,
       and the chat functionality is triggered by pressing the chat button.
   */
   public void gameChat(String message, User currentUser){
       ArrayList<String> userChatMessages = new ArrayList<>();


       /*
       for (User concurrentUser : users) {
           if (concurrentUser == currentUser) {
               userChatMessages.add(concurrentUser.name + ": " + message);
           }
       }for (String userChatMessage : userChatMessages){
           userChatMessagesArray.add(userChatMessage);
       }
       */
       
       JsonObject jsonObject = new JsonObject();
       JsonArray userChatMessagesArray = new JsonArray();


       String userChatMessage = currentUser.name + ": " + message;
       userChatMessagesArray.add(userChatMessage);


       jsonObject.add("userChatMessages", userChatMessagesArray);


       Gson gson = new Gson();
       String json = gson.toJson(jsonObject);
       System.out.println(json);


       Buttons chat = new Buttons(){
           @Override
           public void chatButton(){
               System.out.println("Chat button works");
           }
       };
       chat.chatButton();
   }




   /*
       Abstract clas Button represents the buttons ihn the game interface.
       It provies the methods for play again, confirm, leave and chat.
       Each method prints out the respective thing the button is supposed to do.
   */
  abstract class Buttons{
       public void playAgainButton(){
           System.out.println("play again button");
       }
       public void confirmButton(){
           System.out.println("create again button");
       }
       public void leaveButton(){
           System.out.println("leave button");
       }
       public void chatButton(){
           System.out.println("chat button");
       }
    }


}