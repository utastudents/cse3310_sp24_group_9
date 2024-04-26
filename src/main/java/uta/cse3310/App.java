package uta.cse3310;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.gson.Gson;

public class App extends WebSocketServer {

	// List of games that are currently running
	private Vector<Game> concurrentGames = new Vector<Game>();

	// ServerEvent object to be used to display the lobby menu
	// initially empty until a game is created
	// ServerEvent serverEvent = new ServerEvent(null, null, null, null, null);
	private WordGrid wordGrid;// a ref to the wordgrid class NEW
	private int ServerID = 1;

	private int UserID = 0;

	public App(int port) {
		super(new InetSocketAddress(port));
		this.wordGrid = new WordGrid(); //NEW 
	}

	public App(InetSocketAddress address) {
		super(address);
	}

	public App(int port, Draft_6455 draft) {
		super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress()
		// + " connected");
		System.out.println(conn + " connected");

		Gson gson = new Gson();
		JsonArray allGameDataArray = new JsonArray();
		for (Game gameInstance : concurrentGames) {
			String gameDataString = gameInstance.gameDataToString();
			JsonObject gameDataObject = gson.fromJson(gameDataString, JsonObject.class);
			allGameDataArray.add(gameDataObject);
		}
		JsonObject gameInfo = new JsonObject();
		gameInfo.add("gameData", allGameDataArray);
		String gameInfoJson = gson.toJson(gameInfo);
		
		// conn.send(gameInfoJson);
		broadcast(gameInfoJson);
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

		// print out the message received
		// System.out.println("Message received: " + receivedMessage);


		// checks type of message received
		if (receivedMessage.getButtonType().equals("Confirm")) {
			Game game = new Game();

			game.gameMenu();
			String serverName = receivedMessage.getSeverName();

			game.setServerName(serverName);
			game.setGameId(ServerID++);
			// game.gameWaiting(ServerID);

			// add the new game to lobby list
			if (concurrentGames.size() < 5) {
				concurrentGames.add(game);
			}

			// loop through all the games and print the server name
			JsonArray allGameDataArray = new JsonArray();
			for (Game gameInstance : concurrentGames) {
				String gameDataString = gameInstance.gameDataToString();
				JsonObject gameDataObject = gson.fromJson(gameDataString, JsonObject.class);
				allGameDataArray.add(gameDataObject);
			}

			// Create a JsonObject to contain the allGameDataArray
			JsonObject gameInfo = new JsonObject();
			gameInfo.add("gameData", allGameDataArray); // Add the JsonArray to the JsonObject

			// Convert the JsonObject to JSON
			String gameInfoJson = gson.toJson(gameInfo);

			// conn.send(gameInfoJson);
			broadcast(gameInfoJson);
		} else if (receivedMessage.getButtonType().equals("Join")) {

			int gameId = receivedMessage.getGameId();
			String username = receivedMessage.getUserName();
			UserID++;

			// find the game with the matching gameId
			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
					gameInstance.addUser(UserID, username);
				}
			});

			JsonArray allGameDataArray = new JsonArray();
			for (Game gameInstance : concurrentGames) {
				String gameDataString = gameInstance.gameDataToString();
				JsonObject gameDataObject = gson.fromJson(gameDataString, JsonObject.class);
				allGameDataArray.add(gameDataObject);
			}
			JsonObject gameInfo = new JsonObject();
			gameInfo.add("gameData", allGameDataArray);
			String gameInfoJson = gson.toJson(gameInfo);
			
			// conn.send(gameInfoJson);
			broadcast(gameInfoJson);

			// game.gameWaiting(gameId);
		} else if (receivedMessage.getButtonType().equals("Leave")) {

			int gameId = receivedMessage.getGameId();
			String username = receivedMessage.getUserName();

			// find the game with the matching gameId
			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
					gameInstance.removeUser(username);
				}
			});

		}

		// this checks if the user is ready
		else if (receivedMessage.getButtonType().equals("Ready")) {
			int gameId = receivedMessage.getGameId();
			String username = receivedMessage.getUserName();

			// find the game with the matching gameId
			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
					gameInstance.readyFlip(username);
				}
			});

			JsonArray allGameDataArray = new JsonArray();
			for (Game gameInstance : concurrentGames) {
				String gameDataString = gameInstance.gameDataToString();
				JsonObject gameDataObject = gson.fromJson(gameDataString, JsonObject.class);
				allGameDataArray.add(gameDataObject);
			}
			JsonObject gameInfo = new JsonObject();
			gameInfo.add("gameData", allGameDataArray);
			String gameInfoJson = gson.toJson(gameInfo);

			// conn.send(gameInfoJson);
			broadcast(gameInfoJson);
			
		}

		else if (receivedMessage.getButtonType().equals("StartGame")) {
			int gameId = receivedMessage.getGameId();
			AtomicBoolean readyStatus = new AtomicBoolean(false);

			// find the game with the matching gameId
			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
					readyStatus.set(gameInstance.gameStart());
				}
			});

			JsonObject combinedMessage = new JsonObject();

			if (readyStatus.get()) {

				// Add start game message
				JsonObject startGameMessage = new JsonObject();
				startGameMessage.addProperty("type", "StartGame");
				startGameMessage.addProperty("gameId", gameId);
				combinedMessage.add("startGame", startGameMessage);
			} else {
				
				// Send a message to the client that the game cannot start
				JsonObject errorMessage = new JsonObject();
				errorMessage.addProperty("type", "ErrorMessage");
				errorMessage.addProperty("message", "Not enough players are ready to start the game.");
				combinedMessage.add("error", errorMessage);
			}

			// Convert the combined JSON object to a string
			String combinedJson = gson.toJson(combinedMessage);

			// Broadcast the combined JSON to all connected clients
			broadcast(combinedJson);

		} 
		 else if (receivedMessage.getButtonType().equals("Chat")) {
			int gameId = receivedMessage.getGameId();
			String chatMessage = receivedMessage.getMessage();
			int userId = receivedMessage.getUserID();
			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
					gameInstance.gameChatToJsonString(chatMessage, userId);
				}
			});
		} 
		 else if (receivedMessage.getType().equals("FoundWord")) {
		 int gameId = receivedMessage.getGameId();
		 int userId = receivedMessage.getUserID();
		 int x1 = receivedMessage.getX1();
		 int y1 = receivedMessage.getY1();
		 int x2 = receivedMessage.getX2();
		 int y2 = receivedMessage.getY2();

		// // find the game with the matching gameId
		 concurrentGames.forEach(gameInstance -> {
		 if (gameInstance.getGameId() == gameId) {
		//  gameInstance.checkWord(x1, y1, x2, y2, userId);
		 }
		 });
		 }
		// // need to send update data about user to javascript

		// // conn.send(jsonFoundWord);


		else if (receivedMessage.getType().equals("Chat")) {
			int gameId = receivedMessage.getGameId();
			String chatMessage = receivedMessage.getMessage();
			int userId = receivedMessage.getUserID();

			final JsonObject[] chatData = { null };
			// // find the game with the matching gameId
			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
			//  	chatData[0] = gameInstance.gameChat(message, userId);
				}

			// // need to send update data about game chat to javascript
			conn.send(chatData[0].toString());
			});
		}

		else if (receivedMessage.getType().equals("CellClicked1st")){
			int gameId = receivedMessage.getGameId();
			int x1 = receivedMessage.getX1();
			int y1 = receivedMessage.getY1();
			String username = receivedMessage.getUserName();
			String color = receivedMessage.getColor();
			
			// Create a JsonObject to hold the clicked cell data
			JsonObject cellClickedData = new JsonObject();
			cellClickedData.addProperty("type", "CellClicked1st");
			cellClickedData.addProperty("gameId", gameId);
			cellClickedData.addProperty("coordinate1", x1);
			cellClickedData.addProperty("coordinate2", y1);
			cellClickedData.addProperty("username", username);
			cellClickedData.addProperty("color", color);
		
			// Convert the JsonObject to JSON string
			String cellClickedJson = cellClickedData.toString();

			broadcast(cellClickedJson);
		}
		else if (receivedMessage.getType().equals("CellClicked2nd")){

			boolean wordFound = false;

			int gameId = receivedMessage.getGameId();
			int x1 = receivedMessage.getX1();
			int y1 = receivedMessage.getY1();
			int x2 = receivedMessage.getX2();
			int y2 = receivedMessage.getY2();
			String username = receivedMessage.getUserName();
			String color = receivedMessage.getColor();

			System.out.println("Cell clicked at x: " + x1 + " y: " + y1 + " by user: " + username);
			System.out.println("Cell clicked at x: " + x2 + " y: " + y2 + " by user: " + username);

			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
					// wordFound = gameInstance.checkWord(x1, y1, x2, y2, username);
				}
			});
			
			// Create a JsonObject to hold the clicked cell data
			JsonObject cellClickedData = new JsonObject();
			cellClickedData.addProperty("type", "CellClicked2nd");
			cellClickedData.addProperty("gameId", gameId);
			cellClickedData.addProperty("coordinate1", x2);
			cellClickedData.addProperty("coordinate2", y2);
			cellClickedData.addProperty("username", username);
			cellClickedData.addProperty("color", color);
		
			// Convert the JsonObject to JSON string
			String cellClickedJson = cellClickedData.toString();

			broadcast(cellClickedJson);
		}

		// // need to send update data about user ready status to javascript
		// // send data to update the lobby menu
		// updateLobby(conn);

		 else if (receivedMessage.getType().equals("GameEnd")) {
		 int gameId = receivedMessage.getGameId();

		// // find the game with the matching gameId
		 concurrentGames.forEach(gameInstance -> {
		 if (gameInstance.getGameId() == gameId) {
		 gameInstance.gameEnd();
		 }
		 });

		 }
		// // request for a hint to be send to the game
		// else if (receivedMessage.getType().equals("Hint")) {
		// int gameId = receivedMessage.getGameId();

		// char[] hint = new char[1];
		// // find the game with the matching gameId
		// concurrentGames.forEach(gameInstance -> {
		// if (gameInstance.getGameId() == gameId) {
		// // hint[0] = gameInstance.hintWordGrid();
		// }

		// // json object to send hint to the user
		// HashMap<String, Object> hintMessage = new HashMap<>();
		// hintMessage.put("type", "Hint");
		// hintMessage.put("hint", hint[0]);

		// Gson gsonHint = new Gson();
		// String jsonHint = gsonHint.toJson(hintMessage);

		// // send hint to the all users in the game
		// conn.send(jsonHint);
		// });
		// }
		// Inside the section where a player leaves the game

		// new adding
		/*
		 * concurrentGames.forEach(gameInstance -> {
		 * if (gameInstance.getGameId() == gameId) {
		 * gameInstance.removeUser(receivedMessage.getUserID());
		 * 
		 * // Get the updated player list for the game
		 * List<String> updatedPlayerList = gameInstance.getUserListAsString();
		 * 
		 * // Construct the message to send to clients
		 * HashMap<String, Object> message = new HashMap<>();
		 * message.put("type", "PlayerListUpdate");
		 * message.put("gameId", gameId);
		 * message.put("players", updatedPlayerList);
		 * 
		 * // Convert the message to JSON
		 * Gson gson = new Gson();
		 * String jsonMessage = gson.toJson(message);
		 * 
		 * // Send the updated player list to all clients
		 * broadcast(jsonMessage);
		 * }
		 * });
		 */

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
	

	// When a game is created, and confirmed the lobby menu is updated with the new
	// game added.
	// similar to displayLobby might
	// public void updateLobby(WebSocket conn) {
	// 	// TODO implement

	// 	List<Integer> serverIds = new ArrayList<>();
	// 	List<String> serverNames = new ArrayList<>();
	// 	List<Boolean> readyStatuses = new ArrayList<>();
	// 	List<List<String>> usersLists = new ArrayList<>();
	// 	List<List<String>> userReadyLists = new ArrayList<>();

	// 	for (Game game : concurrentGames) {
	// 		serverIds.add(game.getGameId());
	// 		serverNames.add(game.getServerName());
	// 		readyStatuses.add(game.getJoinable());
	// 		usersLists.add(game.getUserList());
	// 		userReadyLists.add(game.getUserReadyListAsString());
	// 	}

	// 	HashMap<String, Object> Severs = new HashMap<>();

	// 	Severs.put("type", "serverData");
	// 	Severs.put("serverData", new ServerEvent(serverIds, serverNames, readyStatuses, usersLists, userReadyLists));

	// 	Gson gson = new Gson();
	// 	String json = gson.toJson(Severs);

	// 	conn.send(json);
	// }

	// display lobby menu for the user when the user enters their name
	// initially empty for the first user
	// As more users join it may be displayed with the current games
	// public void displayLobby(WebSocket conn) {

	// 	System.out.println("Displaying lobby menu");
	// 	System.out.println(conn);

	// 	List<Integer> serverIds = new ArrayList<>();
	// 	List<String> serverNames = new ArrayList<>();
	// 	List<Boolean> readyStatuses = new ArrayList<>();
	// 	List<List<String>> usersLists = new ArrayList<>();
	// 	List<List<String>> userReadyLists = new ArrayList<>();

	// 	for (Game game : concurrentGames) {
	// 		serverIds.add(game.getGameId());
	// 		serverNames.add(game.getServerName());
	// 		readyStatuses.add(game.getJoinable());
	// 		usersLists.add(game.getUserList());
	// 		userReadyLists.add(game.getUserReadyListAsString());
	// 	}

	// 	// display the lobby menu for the user
	// 	HashMap<String, Object> Severs = new HashMap<>();

	// 	Severs.put("type", "serverData");
	// 	Severs.put("serverData", new ServerEvent(serverIds, serverNames, readyStatuses, usersLists, userReadyLists));

	// 	Gson gson = new Gson();
	// 	String json = gson.toJson(Severs);

	// 	System.out.println("Severs: " + json);

	// 	conn.send(json);
	// }

	public static void main(String[] args) {

		// Set up the http server
		int port = 9009;
		HttpServer H = new HttpServer(port, "./html");
		H.start();
		System.out.println("http Server started on port: " + port);

		// create and start the websocket server
		port = 9109;
		App A = new App(port);
		A.setReuseAddr(true);
		A.start();
		System.out.println("websocket Server started on port: " + port);
	}
}