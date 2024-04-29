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
import java.util.concurrent.atomic.AtomicReference;

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
		gameInfo.add("cellClicked", null);
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
			gameInfo.add("cellClicked", null);

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
			gameInfo.add("cellClicked", null);
			String gameInfoJson = gson.toJson(gameInfo);
			
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
			gameInfo.add("cellClicked", null);
			String gameInfoJson = gson.toJson(gameInfo);

			broadcast(gameInfoJson);
			
		}
		else if (receivedMessage.getButtonType().equals("PlayAgain")) {
			int gameId = receivedMessage.getGameId();
			AtomicBoolean readyStatus = new AtomicBoolean(false);
			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
					readyStatus.set(gameInstance.gameStart());
				}
			});
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
			String username = receivedMessage.getUserName();

			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
					gameInstance.gameChatToJsonString(chatMessage, username);
				}
			});

			JsonArray allGameDataArray = new JsonArray();
			for (Game gameInstance : concurrentGames) {
				String gameDataString = gameInstance.gameDataToString();
				JsonObject gameDataObject = gson.fromJson(gameDataString, JsonObject.class);
				allGameDataArray.add(gameDataObject);
			}
			JsonObject gameInfo = new JsonObject();
			gameInfo.add("ChatData", allGameDataArray);
			gameInfo.add("cellClicked", null);
			String gameInfoJson = gson.toJson(gameInfo);

			broadcast(gameInfoJson);
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
			cellClickedData.addProperty("row", y1);
			cellClickedData.addProperty("col", x1);
			cellClickedData.addProperty("username", username);
			cellClickedData.addProperty("color", color);

			String gameInfoJson = gson.toJson(cellClickedData);
			broadcast(gameInfoJson);
		}
		else if (receivedMessage.getType().equals("CellClicked2nd")){

			AtomicBoolean wordFound = new AtomicBoolean(false);

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
					wordFound.set(gameInstance.checkWord(x1, y1, x2, y2, username));
				}
			});
			
			// Create a JsonObject to hold the clicked cell data
			JsonObject cellClickedData = new JsonObject();
			cellClickedData.addProperty("type", "CellClicked2nd");
			cellClickedData.addProperty("gameId", gameId);
			cellClickedData.addProperty("row1", y1);
			cellClickedData.addProperty("col1", x1);
			cellClickedData.addProperty("row2", y2);
			cellClickedData.addProperty("col2", x2);
			cellClickedData.addProperty("username", username);
			cellClickedData.addProperty("color", color);
			cellClickedData.addProperty("wordFound", wordFound.get());

			JsonArray allGameDataArray = new JsonArray();
			for (Game gameInstance : concurrentGames) {
				String gameDataString = gameInstance.gameDataToString();
				JsonObject gameDataObject = gson.fromJson(gameDataString, JsonObject.class);
				allGameDataArray.add(gameDataObject);
			}
			JsonObject gameInfo = new JsonObject();
			gameInfo.add("gameData", allGameDataArray);
			gameInfo.add("cellClicked", cellClickedData);
			String gameInfoJson = gson.toJson(gameInfo);

			broadcast(gameInfoJson);	

		}

		// // need to send update data about user ready status to javascript
		// // send data to update the lobby menu
		// updateLobby(conn);

		else if (receivedMessage.getType().equals("EndGame")) {
			int gameId = receivedMessage.getGameId();
			AtomicReference<String> endGameData = new AtomicReference<>();

			// find the game with the matching gameId
			concurrentGames.forEach(gameInstance -> {
				if (gameInstance.getGameId() == gameId) {
					endGameData.set(gameInstance.gameEnd(gameId));
				}
			});

			concurrentGames.removeIf(gameInstance -> gameInstance.getGameId() == gameId);

			// Create a JsonObject to hold the clicked cell data
			JsonObject endGameDataJson = new JsonObject();
			endGameDataJson.addProperty("type", "EndGame");
			endGameDataJson.addProperty("gameId", gameId);
			endGameDataJson.addProperty("endGameData", endGameData.get());

			String gameInfoJson = gson.toJson(endGameDataJson);
			broadcast(gameInfoJson);
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