package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler {

    Socket socket;

    DataOutputStream out;
    DataInputStream in;

    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Runnable() {
                @Override
                public void run() {
                        try {
                    while (true){
                        String string;
                            string = in.readUTF();
                        if (string.equals("/end")){
                            out.writeUTF("/end");
                            break;
                        } else if (string.equals("/stop")){
                            out.writeUTF("/stop");
                            socket.close();
                        }
                        System.out.println("user send message "+string);
                        out.writeUTF("server: "+string);
                        }
                        } catch (IOException e) {
                            e.printStackTrace();
                    } finally {
                            try {
                                in.close();
                                out.close();
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
