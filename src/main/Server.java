package main;

import main.environment.Environment;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int port;
    private ServerSocket server;

    private boolean isServerRunning = false;

    public Server() {
        port = Environment.PORT;
    }

    public Server(int port) {
        this.port = port;
    }

    private void run() {
        System.out.println("Waiting for a client...");
        while (true) {
            try {
                Socket socket = server.accept();
                System.out.println("\nNew client connected");

                ClientThread clientThread = new ClientThread(socket);
                clientThread.start();

                /*try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    System.out.println("Enter");

                    if (in.ready()) {
                        String message = in.readLine();

                        String[] args = message.split(",");
                        float firstArg = Float.parseFloat(args[0]);
                        float secondArg = Float.parseFloat(args[1]);

                        System.out.println("first: " + firstArg);
                        System.out.println("second: " + secondArg);

                        double result = Math.multiply(firstArg, secondArg);

                        System.out.println("result: " + result);

                        out.write(String.valueOf(result));
                        out.flush();
                    }
                } catch (IOException ex) {
                    System.err.println("Server: Unknown Input/Output Error!");
                    ex.printStackTrace();
                }*/
            } catch (IOException ex) {
                System.err.println("Server: Unknown Client Connecting Input/Output Error!");
                ex.printStackTrace();
            }
        }

//            System.err.println("Server: Unknown Input/Output Error!");
    }

    public void startServer() throws IOException {
        isServerRunning = true;
        server = new ServerSocket(port);
        run();
    }

    public void stopServer() throws IOException {
        isServerRunning = false;
        server.close();
    }

    class ClientThread extends Thread {

        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String message;

        ClientThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            boolean isRun = true;
            try {
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                message = receiveMessage();
                System.out.println("Message: " + message);

                String[] args = message.split(",");
                float firstArg = Float.parseFloat(args[0]);
                float secondArg = Float.parseFloat(args[1]);

                double result = Math.multiply(firstArg, secondArg);
                sendMessage(result);

                System.out.println("Result: " + result);

                in.close();
                out.close();
            } catch (IOException ex) {
                System.err.println("Server: Unknown Input/Output Error!");
                ex.printStackTrace();
            }
            close();
        }

        public void sendMessage(String message) {
            out.println(message);
            out.flush();
        }

        public void sendMessage(Double message) {
            out.println(message);
            out.flush();
        }

        public String receiveMessage() {
            String message = null;
            try {
                message = in.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return message;
        }

        private void close() {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
