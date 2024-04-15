package uta.cse3310;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Vector;

import com.google.gson.Gson;


public class App extends WebSocketServer {

  // List of games that are currently running
  private Vector<Game> concurrentGames = new Vector<Game>();
  private List<User> userList = new ArrayList<>();


  // ServerEvent object to be used to display the lobby menu
  // initially empty until a game is created
  ServerEvent serverEvent = new ServerEvent(null, null, null, null);
  
  private int ServerID = 1;

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
    System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected");
    broadcastGameList();
    displayLobby(conn);
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println(conn + " disconnected");

  }
  //to update how many game has been created to all players's screen
  public void broadcastGameList() {
    Gson gson = new Gson();
    String json = gson.toJson(serverEvent); // Convert the list of games to JSON
    broadcast(json); // Send the JSON string to all connected clients
  }
  //send the list of games to any new client after they create username
  /* public void sendGameList(WebSocket conn) {
    Gson gson = new Gson();
    String json = gson.toJson(concurrentGames); // Convert the list of games to JSON
    conn.send(json); // Send the JSON string to the client
} */

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

      User newUser = new User(UserID, Username);

      userList.add(newUser);

      // print all users in userList
      userList.forEach(user -> {
        System.out.println("UserID: " + user.getID() + " Username: " + user.getName());
      });

      displayLobby(conn);
    }/* else if (receivedMessage.getType().equals("RequestGameList")) { // Handle the request for the game list
        sendGameList(conn); // Send the list of games to the client
    } */ else if (receivedMessage.getType().equals("CreateGame")) {
      Game game = new Game();

      game.createGame();

      if (receivedMessage.getButtonType().equals("Confirm")) {
        String serverName = receivedMessage.getSeverName();
        int UserID = receivedMessage.getUserID();
        String userName = receivedMessage.getUserName();

        game.setServerName(serverName);
        game.setGameId(ServerID++);
        game.addUser(UserID, userName);
        game.gameWaiting(ServerID);

        

        // System.out.println("UserID: " + UserID + "Username: " + userName );

        // add the new game to lobby list
        concurrentGames.add(game);

        updateLobby(conn);

        broadcastGameList();

      } else if (receivedMessage.getButtonType().equals("Join")) {

        int gameId = receivedMessage.getGameId();
        String username = receivedMessage.getUserName();

        // find the game with the matching gameId
        concurrentGames.forEach(gameInstance -> {
          if (gameInstance.getGameId() == gameId) {
            gameInstance.addUser(receivedMessage.getUserID(), username);
          }
        });

        game.gameWaiting(gameId);
      } else if (receivedMessage.getButtonType().equals("Leave")) {

        int gameId = receivedMessage.getGameId();

        // find the game with the matching gameId
        concurrentGames.forEach(gameInstance -> {
          if (gameInstance.getGameId() == gameId) {
            gameInstance.removeUser(receivedMessage.getUserID());
          }
        });

        // display the lobby menu
        updateLobby(conn);
      }

    } else if (receivedMessage.getType().equals("StartGame")) {
      int gameId = receivedMessage.getGameId();

      // find the game with the matching gameId
      concurrentGames.forEach(gameInstance -> {
        if (gameInstance.getGameId() == gameId) {
          gameInstance.gameStart();
        }
      });

    } else if (receivedMessage.getType().equals("FoundWord")){
      int gameId = receivedMessage.getGameId();
      int userId = receivedMessage.getUserID();
      int x1 = receivedMessage.getX1();
      int y1 = receivedMessage.getY1();
      int x2 = receivedMessage.getX2();
      int y2 = receivedMessage.getY2();

      // find the game with the matching gameId
      concurrentGames.forEach(gameInstance -> {
        if (gameInstance.getGameId() == gameId) {
          // gameInstance.checkWord(x1, y1, x2, y2, USER);
        }
      });

    } else if (receivedMessage.getType().equals("Chat")){
      int gameId = receivedMessage.getGameId();
      String chatMessage = receivedMessage.getMessage();
      int userId = receivedMessage.getUserID();

      // find the game with the matching gameId
      concurrentGames.forEach(gameInstance -> {
        if (gameInstance.getGameId() == gameId) {
          gameInstance.gameChat(message, userId);
        }
      });

    } else if (receivedMessage.getType().equals("EndGame")){
      int gameId = receivedMessage.getGameId();

      // find the game with the matching gameId
      concurrentGames.forEach(gameInstance -> {
        if (gameInstance.getGameId() == gameId) {
          gameInstance.gameEnd();
        }
      });

    } 
    // this checks if the user is ready to start the game
    else if (receivedMessage.getType().equals("Ready")) {
      int gameId = receivedMessage.getGameId();
      int userId = receivedMessage.getUserID();

      // find the game with the matching gameId
      concurrentGames.forEach(gameInstance -> {
        if (gameInstance.getGameId() == gameId) {
          gameInstance.readyFlip(userId);
        }
      });

    } else if (receivedMessage.getType().equals("GameEnd")) {
      int gameId = receivedMessage.getGameId();

      // find the game with the matching gameId
      concurrentGames.forEach(gameInstance -> {
        if (gameInstance.getGameId() == gameId) {
          gameInstance.gameEnd();
        }
      });

    } else if (receivedMessage.getType().equals("Hint")){
      int gameId = receivedMessage.getGameId();


      String hint  = "";
      // find the game with the matching gameId
      concurrentGames.forEach(gameInstance -> {
        if (gameInstance.getGameId() == gameId) {
          // hint = gameInstance.gameHint();
        }

        // json object to send hint to the user
        HashMap<String, Object> hintMessage = new HashMap<>();
        hintMessage.put("type", "Hint");
        hintMessage.put("hint", hint);

        Gson gsonHint = new Gson();
        String jsonHint = gsonHint.toJson(hintMessage);

        conn.send(jsonHint);
      });
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


  // When a game is created, and confirmed the lobby menu is updated with the new game added.
  public void updateLobby(WebSocket conn) {
    // TODO implement

    List<Integer> serverIds = new ArrayList<>();
    List<String> serverNames = new ArrayList<>();
    List<Boolean> readyStatuses = new ArrayList<>();
    List<List<String>> usersLists = new ArrayList<>();

    for (Game game : concurrentGames) {
      serverIds.add(game.getGameId());
      serverNames.add(game.getServerName());
      readyStatuses.add(game.getJoinable());
      usersLists.add(game.getUserList());
    }

    HashMap<String, Object> Severs = new HashMap<>();

    Severs.put("type", "serverData");
    Severs.put("serverData", new ServerEvent(serverIds, serverNames, readyStatuses, usersLists));
    
    Gson gson = new Gson();
    String json = gson.toJson(Severs);

    conn.send(json);
  }

  // display lobby menu for the user when the user enters their name
  // initially empty for the first user
  // As more users join it may be displayed with the current games
  public void displayLobby(WebSocket conn) {

    List<Integer> serverIds = new ArrayList<>();
    List<String> serverNames = new ArrayList<>();
    List<Boolean> readyStatuses = new ArrayList<>();
    List<List<String>> usersLists = new ArrayList<>();

    for (Game game : concurrentGames) {
      serverIds.add(game.getGameId());
      serverNames.add(game.getServerName());
      readyStatuses.add(game.getJoinable());
      usersLists.add(game.getUserList());
    }

    // display the lobby menu for the user
    HashMap<String, Object> Severs = new HashMap<>();


    Severs.put("type", "serverData");
    Severs.put("serverData", new ServerEvent(serverIds, serverNames, readyStatuses, usersLists));

    Gson gson = new Gson();
    String json = gson.toJson(Severs);

    System.out.println("Severs: " + json);


    conn.send(json);
  }

  // public void joinGame(Game concurrentGame, User id) {
  //   // TODO implement
  // }

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