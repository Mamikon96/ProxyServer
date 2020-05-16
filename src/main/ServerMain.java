package main;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
