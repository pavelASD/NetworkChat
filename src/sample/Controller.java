package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button deleteAll;

    @FXML
    private TextField enterText;

    @FXML
    private Button sendMessage;

    @FXML
    private TextArea showText;


    @FXML
    void initialize() {}

    Socket socket;
    DataInputStream in;
    DataOutputStream out;

    final String IP_ADDRESS = "localhost";
    final int PORT = 8189;

    //подключение к серверу
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());


        } catch (IOException e) {
            e.printStackTrace();
        }
    showText.setEditable(false); // запрещаем пользователю писать внутри textarea (окно вывода сообщений)

        new Thread(new Runnable() {
            @Override
            public void run() {
                    try {
                while (true){
                        String string = in.readUTF();
                        if (string.equals("/end")){
                            break;
                        } else if (string.equals("/stop")){
                            out.writeUTF("server operation stopped");
                            socket.close();
                        }
                        showText.appendText(string+"\n");
                }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                            in.close();
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }).start();

        sendMessageByEnter();
        sendMessage();
        deleteAllMessage();
    }



public void sendMessage(){
        sendMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    out.writeUTF(enterText.getText());
                    enterText.clear();
                    enterText.requestFocus();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

public void deleteAllMessage(){
        deleteAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                enterText.clear();
            }
        });
}

public void sendMessageByEnter(){
    enterText.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode().getCode() == 10){
                try {
                    out.writeUTF(enterText.getText());
                    enterText.clear();
                    enterText.requestFocus();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    }
}
