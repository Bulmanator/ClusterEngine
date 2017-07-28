package com.cluster.engine.Network;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.Semaphore;

/**
 * @author Matthew Threlfall
 */
public class Connection extends Thread{
    private Socket s;
    private boolean connected;
    private Vector<String> messages;
    private Semaphore locked;

    public Connection(Socket socket) {
        this.s = socket;
        connected = true;
    }

    public void run(){
        try {
            DataInputStream in = new DataInputStream(s.getInputStream());
            while (connected) {
                messages.add(in.readUTF());
            }
            in.close();
            s.close();
        } catch(Exception e) {e.printStackTrace();}
    }

    public void sendMessage(String message) throws IOException {
        DataOutputStream out = new DataOutputStream(s.getOutputStream());
        out.writeUTF(message);
        out.close();
    }

    public Vector<String> getMessages() {
        return messages;
    }

    public boolean isConnected() {
        return connected;
    }

    public void clearMessages() {
        messages.clear();
    }

    public void disconnect() throws IOException{
        locked.release();
        connected = false;
        s.close();
    }
}
