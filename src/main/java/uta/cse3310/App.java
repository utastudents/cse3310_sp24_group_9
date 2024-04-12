package uta.cse3310;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.time.Instant;
import java.time.Duration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uta.cse3310.MessageEvent;

public class App extends WebSocketServer {

  private Vector<Game> concurrentGames = new Vector<Game>();

  ServerEvent serverEvent = new ServerEvent(null, null, null, null);

  private int SeverID = 1;

  private int UserID = 0;

  public App(int port) {
    super(new InetSocketAddress(port));
  }

  public App(InetSocketAddress address) {
    super(address);
  }

  public App(int port, Draft_6455 draft) {
    super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    // TODO implement
    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");

    // Initialize the lobby menu for users that connect
    // initializeLobby(conn);

    updateLobby(SeverID, conn);
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    // TODO implement
    System.out.println(conn + " disconnected");

    
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    // TODO implement

    Gson gson = new Gson();
    MessageEvent receivedMessage = gson.fromJson(message, MessageEvent.class);

    System.out.println("Received message: " + receivedMessage.getType());

    // checks type of message received
    if (receivedMessage.getType().equals("Username")) {
      String Username = receivedMessage.getUserName();
      UserID++;

      System.out.println("User " + UserID + " has connected with username: " + Username);

      displayLobby(conn);
    }
    else if(receivedMessage.getType().equals("CreateGame")){
      Game game = new Game();

      game.createGame();

      if(receivedMessage.getButtonType().equals("Confirm")){
        String serverName = receivedMessage.getSeverName();

        game.setGameName(serverName);
        game.setGameId(SeverID++);
      
        // add the new game to lobby list
        concurrentGames.add(game);

        updateLobby(game.getGameId(), conn);

        // display the game waiting room
        game.gameWaiting(SeverID, null);
      }
      else if(receivedMessage.getButtonType().equals("Join")){

        int gameId = receivedMessage.getGameId();

        // I NEED TO ADD addUser METHOD TO GAME CLASS
        concurrentGames.forEach(gameInstance -> {
          if(gameInstance.getGameId() == gameId){
            gameInstance.addUser(receivedMessage.getUserName());
          }
        });

        List<String> UserList = new ArrayList<>();
        
        receivedMessage.getUserList().forEach(user -> {
          UserList.add(user);
        });



      }
    }
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    System.err.println("An error occurred on connection " + conn.getRemoteSocketAddress());
    ex.printStackTrace();
  }

  @Override
  public void onStart() {

    // Sets server not to automatically close inactive connections.
    setConnectionLostTimeout(0);

    System.out.println("The server has started!");
  }

  // public void initializeLobby(WebSocket conn) {

  //   // lobby information to be displayed with the message type: lobbyInfo
  //   HashMap<String, Object> lobbyInfo = new HashMap<>();
  //       lobbyInfo.put("title", "Word Search Game");,
  //       lobbyInfo.put("inputLabel", "Enter your name");

  //       // Create a HashMap for the message event
  //       HashMap<String, Object> messageEvent = new HashMap<>();
  //       messageEvent.put("type", "InitializeLobby");
  //       messageEvent.put("data", lobbyInfo);

  //       Gson gson = new Gson();
  //       String json = gson.toJson(messageEvent);

  //       conn.send(json);
  // }

  public void updateLobby(int gameId, WebSocket conn) {
    // TODO implement

    List<Integer> serverIds = new ArrayList<>();
    List<String> serverNames = new ArrayList<>();
    List<Boolean> readyStatuses = new ArrayList<>();
    List<List<String>> usersLists = new ArrayList<>();

    // // test put in some dummy data
    // serverNames.add("Servername 1");
    // serverNames.add("Servername 2");
    // serverNames.add("Servername 3");

    // readyStatuses.add(true);
    // readyStatuses.add(false);
    // readyStatuses.add(true);

    // List<String> users1 = new ArrayList<>();
    // users1.add("Adam");
    // users1.add("Bob");
    // users1.add("Candice");

    // List<String> users2 = new ArrayList<>();
    // users2.add("Derick");
    // users2.add("Eve");
    // users2.add("Fred");

    // List<String> users3 = new ArrayList<>();
    // users3.add("Gred");
    // users3.add("Henry");
    // users3.add("Ian");

    // usersLists.add(users1);
    // usersLists.add(users2);
    // usersLists.add(users3);

    // PLEASE ADD game.getUsername(), game.isReady(), game.getUserList method to Game class PLEASE PLEASE PLEASE PLEASE
    // for (Game game : games) {
    //   serverNames.add(game.getServerName()); // Assuming each game has a method to get the server name
    //   readyStatuses.add(game.isReady()); // Assuming each game has a method to get the ready status
    //   usersLists.add(game.getUserList()); // Assuming each game has a method to get the list of users
    // }


    ServerEvent serverEventData = new ServerEvent(serverIds, serverNames, readyStatuses, usersLists);

    HashMap<String, Object> Severs = new HashMap<>();
    Severs.put("severTitle", "Sever Name");
    Severs.put("playerTitle", "Players");
    Severs.put("serverData", serverEventData);

    Gson gson = new Gson();
    String json = gson.toJson(Severs);

    conn.send(json);
  }


  public void displayLobby(WebSocket conn) {
    
    // display the lobby menu for the user
    HashMap<String, Object> Severs = new HashMap<>();
    Severs.put("severTitle", "Sever Name");
    Severs.put("playerTitle", "Players");

    
    Severs.put("serverData", serverEvent);

    Gson gson = new Gson();
    String json = gson.toJson(Severs);

    conn.send(json);
  }

  public void joinGame(Game concurrentGame, User id) {
    // TODO implement
  }

  public static void main(String[] args) {

    // Set up the http server
    int port = 9080;
    HttpServer H = new HttpServer(port, "./html");
    H.start();
    System.out.println("http Server started on port: " + port);

    // create and start the websocket server
    port = 9880;
    App A = new App(port);
    A.setReuseAddr(true);
    A.start();
    System.out.println("websocket Server started on port: " + port);
  }
}
