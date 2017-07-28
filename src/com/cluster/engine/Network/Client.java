package com.cluster.engine.Network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Matthew Threlfall
 */
public class Client extends Thread {
    private Connection c;

    public Client(String host, int port) throws IOException {
        c = new Connection(new Socket(host, port));
        c.start();
    }

    public boolean isConnected() {
        return c.isConnected();
    }

    public ArrayList<String> getMessages() {
        ArrayList<String> messages = new ArrayList<>();
        for(int i = 0; i < c.getMessages().size(); i++)
        {
            messages.add(c.getMessages().get(i));
        }
        c.clearMessages();
        return messages;
    }

    public void sendMessage(String message) throws IOException {
        c.sendMessage(message);
    }
}
