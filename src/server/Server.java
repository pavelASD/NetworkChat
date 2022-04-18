package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Server {

    private Vector<ClientHandler> clientHandlers;

    public void start() {
        ServerSocket server;
        Socket socket;
        clientHandlers = new Vector<>();

        try{
            server = new ServerSocket(8189);
            System.out.println("the server is running");

            while (true) {
                socket = server.accept();
                System.out.println("user connected");
                clientHandlers.add(new ClientHandler(socket));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
