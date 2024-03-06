package src.main.WordSearch;

import java.net.http.WebSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class App extends WebSocketServer{   

    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
        // setConnectionLostTimeout(0);
    }

    public static void main(String[] args) {
        int port = 9080;
        HttpServer H = new HttpServer(port, "./html");
        H.start();
        System.out.println("http Server started on port:" + port);

        // create and start the websocket server

        port = 9880;
        App A = new App(port);
        A.start();
        System.out.println("websocket Server started on port: " + port);

    }
}
