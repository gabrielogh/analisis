package trivia;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.validation.UniquenessValidator;
import trivia.User;
import trivia.Question;
import trivia.Game;
import trivia.Category;
import trivia.PlayGame;
import trivia.LoginServer;



@WebSocket
public class EchoWebSocket {
    private String sender, msg;
    private static Map<Session,User> usersOnline = new HashMap<Session,User>();

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = App.nextUserName;
        if(App.userUsernameMap.size()<=1){
            App.userUsernameMap.put(user, username);
            if(App.userUsernameMap.size()==2){
              App.nextUserNumber = App.nextUserNumber + 2;
              App.create_vs_game(App.userUsernameMap);
              App.cambiarTurno(sender = username, msg = (" Responde"));
            }
        }
        else if ((App.userUsernameMap.size()>1) && (App.userUsernameMap.size()%2 == 0)){
            App.userUsernameMap.put(user, username);
            App.nextUserNumber = App.nextUserNumber + 2;
            App.create_vs_game(App.userUsernameMap);
            App.cambiarTurno(sender = username, msg = (" Responde"));
        }
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = App.userUsernameMap.get(user);
        App.userUsernameMap.remove(user);
        App.cambiarTurno(sender = "Server", msg = (username + " Salio"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        JSONObject obj = new JSONObject(message);
        String description = new String(obj.getString("message"));
        if (description.equals("newGame")) {
            Versusmode.Versusmode(obj.getInt("user1_id"),obj.getInt("user2_id"));
            Base.close();
            //App.broadcastMessage("Nuevo_Juego",obj.getInt("user1ID"));
        }
        App.cambiarTurno(sender = App.userUsernameMap.get(user), msg = message);
    }



}