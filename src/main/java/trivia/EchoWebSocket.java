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

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = App.nextUserName;
        if(App.userUsernameMap.size()<=1){
            App.userUsernameMap.put(user, username);
            if(App.userUsernameMap.size()==2){
            		App.create_vs_game(App.userUsernameMap);
                //App.cambiarTurno(sender = username, msg = (" Responde"));
            }
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
        if(App.nextUserNumber == 1){
            App.nextUserNumber++;
            App.nextUserName = App.userUsernameMap.get(2);
        }
        else{
            App.nextUserNumber--;
            App.nextUserName = App.userUsernameMap.get(1);          
        }
        
        App.cambiarTurno(sender = App.userUsernameMap.get(user), msg = message);
    }

}