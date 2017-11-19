package trivia;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.validation.UniquenessValidator;
import org.json.JSONObject;
import org.json.JSONArray;


@WebSocket
public class EchoWebSocket {
  private static Map<Session,User> usersOnline = new HashMap<Session,User>();

  @OnWebSocketConnect
  public void onConnect(Session user) throws Exception {
    App.userUsernameMap.put(user, new User());
    //App.updateOnlineUsers("updateOnlineUsers");
  }

  @OnWebSocketClose
  public void onClose(Session user, int statusCode, String reason) {
    App.userUsernameMap.remove(user);
    App.usersPlaying.remove(user);
    App.updateOnlineUsers("updateOnlineUsers");
  }

  @OnWebSocketMessage
  public void onMessage(Session user, String message){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    JSONObject data = new JSONObject(message);
    String token = new String(data.getString("token"));

    switch(token){
      case "startGame":
        User p1 = User.findById((Integer)data.get("p1_id"));
        User p2 = User.findById((Integer)data.get("p2_id"));
        Session u1 = Versusmode.getSession(App.userUsernameMap, p1);
        Session u2 = Versusmode.getSession(App.userUsernameMap, p2);
        App.usersPlaying.put(u1,p1);
        App.usersPlaying.put(u2,p2);
        App.userUsernameMap.remove(u1);
        App.userUsernameMap.remove(u2);
        Versusmode v = new Versusmode((Integer)data.get("p1_id"), (Integer)data.get("p2_id"));
        v.saveIt();
        Base.close();
        App.updateOnlineUsers("updateOnlineUsers");
        v.getQuestion();
        break;

      case "answer":
        Versusmode vs = Versusmode.findById(data.getInt("game_id"));
        JSONObject res = vs.answerQuestion(data);
        Base.close();
        vs.sendResults(res);
        if(vs.getInteger("question_number")<10){
          vs.getQuestion();
        }
        break;

      case "putUser":
        User u = User.getUserByName((String)data.get("username"));
        App.userUsernameMap.put(user,u);
        Base.close();
        App.updateOnlineUsers("updateOnlineUsers");
        break;
      default:
       break;
    }
  }
}