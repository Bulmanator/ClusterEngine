package com.cluster.engine.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Matthew Threlfall
 */

public class Server extends Thread {
    private int port;
    private int maxConnections;
    private ServerSocket serverSocket;
    private Vector<Connection> connections;
    private boolean open;

    public Server(int port, int maxConnections) throws IOException {
        this.port = port;
        this.maxConnections  = maxConnections;
        serverSocket = new ServerSocket(port);
        connections = new Vector<>();
    }

    public void run() {
        try {
            while (open) {
                connections.add(new Connection(serverSocket.accept()));
            }
        } catch(Exception e) {e.printStackTrace();}
    }

    public void sendMessage(String message) throws IOException{
        for(int i = 0; i < connections.size(); i ++) {
            connections.get(i).sendMessage(message);
        }
    }

    public boolean isOpen() {
        return open;
    }
}
