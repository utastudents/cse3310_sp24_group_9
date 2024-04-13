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

    // displayLobby(conn);
    // updateLobby(conn);
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
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
    } else if (receivedMessage.getType().equals("CreateGame")) {
      Game game = new Game();

      game.createGame();

      if (receivedMessage.getButtonType().equals("Confirm")) {
        String serverName = receivedMessage.getSeverName();

        game.setServerName(serverName);
        game.setGameId(ServerID++);

        // add the new game to lobby list
        concurrentGames.add(game);

        updateLobby(conn);

        // display the game waiting room
        game.gameWaiting(ServerID);

      } else if (receivedMessage.getButtonType().equals("Join")) {

        int gameId = receivedMessage.getGameId();

        // find the game with the matching gameId
        concurrentGames.forEach(gameInstance -> {
          if (gameInstance.getGameId() == gameId) {
            gameInstance.addUser(receivedMessage.getUserID(), receivedMessage.getUserName());
          }
        });

        game.gameWaiting(gameId);
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


  // When a game is created, and confirmed the lobby menu is updated with the new game added.
  public void updateLobby(WebSocket conn) {
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

    // PLEASE ADD game.getServerName(), game.getisReady(), game.getUserList method
    // to Game class PLEASE PLEASE PLEASE PLEASE
    // for (Game game : concurrentGames) {
    // serverIds.add(game.getGameId());
    // serverNames.add(game.getServerName());
    // readyStatuses.add(game.isReady());
    // usersLists.add(game.getUserList());
    // }

    HashMap<String, Object> Severs = new HashMap<>();

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

    // PLEASE ADD game.getServerName(), game.getisReady(), game.getUserList method to Game class PLEASE PLEASE PLEASE PLEASE
    // for (Game game : concurrentGames) {
    // serverIds.add(game.getGameId());
    // serverNames.add(game.getServerName());
    // readyStatuses.add(game.isReady());
    // usersLists.add(game.getUserList());
    // }

    // display the lobby menu for the user
    HashMap<String, Object> Severs = new HashMap<>();

    Severs.put("serverData", new ServerEvent(serverIds, serverNames, readyStatuses, usersLists));

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
