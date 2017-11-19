package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import trivia.User;
import trivia.Question;
import trivia.Game;
import trivia.Category;
import trivia.PlayGame;
import trivia.LoginServer;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.json.JSONObject;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;

/**
 * This class represents a multiplayer game.
 * when two players try to start a game this class creates a multiplayer game
 * creating a single game for each user but allowing to answer a question one by one (waiting his turn).
 */

public class Versusmode extends Model{

	/**
	 * Constructor of the class.
	 * @param. both users.
	 * @pre. Users logged.
	 * @post. New VersusMode created on the DB.
	 */

  public Versusmode(){};
	
  public Versusmode(Integer p1_id, Integer p2_id){
    Game g1 = new Game(p1_id);
		Game g2 = new Game(p2_id);
    g1.saveIt();
    g2.saveIt();
    set("game_p1_id", g1.getGameId());
    set("game_p2_id", g2.getGameId());
    set("p1_id", p1_id);
    set("p2_id", p2_id);
    set("turn", 1);
    set("in_progress",true);
    set("question_number", 0);
	}

  public void incQuestionNumber(){ this.set("question_number", this.getQuestionNumber()+1);};
  public Integer getQuestionNumber(){return this.getInteger("question_number");};
	public  Game getGameP1(){ return Game.findById(this.getInteger("game_p1_id"));};
	public  Game getGameP2(){ return Game.findById(this.getInteger("game_p2_id"));};
	public  User getPlayer1(){ return User.getUserById(this.getInteger("p1_id"));};
	public  User getPlayer2(){ return User.getUserById(this.getInteger("p2_id"));};

  public void getQuestion(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    Game game_p1 = Game.findById(this.getInteger("game_p1_id"));
    Game game_p2 = Game.findById(this.getInteger("game_p2_id"));
    Session u2 = getKeyByValue(App.usersPlaying, User.findById(game_p2.getInteger("user_id")));
    Session u1 = getKeyByValue(App.usersPlaying, User.findById(game_p1.getInteger("user_id")));
    if(this.getInteger("turn")==1){
      JSONObject question = PlayGame.generateQuestion(game_p1);
      question.put("turn", 1).put("game_id", this.getInteger("id"));
      Base.close();
      try{
        u1.getRemote().sendString(String.valueOf(question));
        u2.getRemote().sendString(String.valueOf(question));
      } catch(Exception e){
        e.printStackTrace();
      }   
    }
    else{
      JSONObject question = PlayGame.generateQuestion(game_p2);
      question.put("turn", 2).put("game_id", this.getInteger("id"));
      Base.close();
      try{
        u2.getRemote().sendString(String.valueOf(question));
        u1.getRemote().sendString(String.valueOf(question));
      } catch(Exception e){
        e.printStackTrace();
      } 
    }
  }

  public Session getKeyByValue(Map<Session,User> map, User user){
   for (Map.Entry<Session, User> entry : map.entrySet()) {
      if (user.getUserId().equals((entry.getValue()).getUserId())) {
          return entry.getKey();
      }
    }
    return null;
  }

  public static boolean answer(Integer user_id, Integer question_id, int answer){
    return true;
  } 

	/**
	 * This method returns the winner number of the game.
	 * if player1 wins, return 1, if player2 wins, return 2, if its a draw return 0
	 * if the game isn't finished, return -1
	 * @param.
	 * @pre. 
	 * @post. Number of winner (1-2) or -1 for error.
	 */
	public JSONObject getFinalResults(){
    JSONObject results = new JSONObject();
    Game g1 = Game.findById(this.get("game_p1_id"));
    Game g2 = Game.findById(this.get("game_p2_id"));
    int winner_id, loser_id;
    Integer user1_id = g1.getInteger("user_id");
    Integer user2_id = g2.getInteger("user_id");

    if(g1.getInteger("corrects")>g2.getInteger("corrects")){
      results.put("loser", user2_id).put("winner", user1_id).put("player", 1);
    }
    else if(g1.getInteger("corrects")<g2.getInteger("corrects")){
      results.put("loser", user1_id).put("winner", user2_id).put("player", 2);
    }
    else{
      results.put("loser", 0).put("winner", 0);
    }
    results.put("token","gameFinished")
    .put("corrects_p1", g1.getInteger("corrects"))
    .put("corrects_p2", g2.getInteger("corrects"))
    .put("incorrects_p1", g1.getInteger("incorrects"))
    .put("incorrects_p2", g2.getInteger("incorrects"));
    return results;
	}

  public void sendResults(JSONObject data){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    Session p1 = getSession(App.usersPlaying, this.getPlayer1());
    Session p2 = getSession(App.usersPlaying, this.getPlayer2());
    User player1 = this.getPlayer1();
    User player2 = this.getPlayer2();
    JSONObject results = new JSONObject();
    try{
      if(this.getInteger("question_number")==10){
        Base.close();
      System.out.println("EL JUEGO TERMINO, ENVIANDO RESULTADOS");
        p1.getRemote().sendString(String.valueOf(data));
        p2.getRemote().sendString(String.valueOf(data));
      }else{
        if(data.getInt("turn") == 1){
        results.put("token","showResult").put("game_id",this.getInteger("id")).put("correct",data.get("result")).put("user_id", player1.getInteger("id"));
        Base.close();
        System.out.println(String.valueOf(results));
        p1.getRemote().sendString(String.valueOf(results));
        }
        else{
          results.put("token","showResult").put("game_id",this.getInteger("id")).put("correct",data.get("result")).put("user_id", player2.getInteger("id"));
          Base.close();
          p2.getRemote().sendString(String.valueOf(results));
        }
      }
    } catch(Exception e){
      e.printStackTrace();
    }         
  }

  public JSONObject answerQuestion(JSONObject data){
    JSONObject result = new JSONObject();
    if(data.getInt("turn")==1){
      this.set("turn",2).saveIt();
      User player1 = this.getPlayer1();
      Game g1 = this.getGameP1();
      Question q = Question.findById(data.get("question_id"));
      System.out.println("EL JUEGO ENTRO A LA PREGUNTA: " + this.getInteger("question_number"));
      Boolean answer = player1.answerOnline(q, data.getInt("answer"), g1);
      this.set("question_number", this.getInteger("question_number")+1).saveIt();
      System.out.println("EL JUEGO PASA A LA PREGUNTA: " + this.getInteger("question_number"));
      if(this.getInteger("question_number")==10){
        return this.getFinalResults();
      }
      result.put("result", answer).put("turn",1);
    }
    else{
      this.set("turn",1).saveIt();
      User player2 = this.getPlayer2();
      Game g2 = this.getGameP2();
      Question q = Question.findById(data.getInt("question_id"));
      System.out.println("EL JUEGO ENTRO A LA PREGUNTA: " + this.getInteger("question_number"));
      Boolean answer = player2.answerOnline(q, data.getInt("answer"), g2);
      this.set("question_number", this.getInteger("question_number")+1).saveIt();
      System.out.println("EL JUEGO PASA A LA PREGUNTA: " + this.getInteger("question_number"));
      if(this.getInteger("question_number")==10){
        return this.getFinalResults();
      }
      result.put("result", answer).put("turn",2);
    }
    return result;
  }

  public static Session getSession(Map<Session,User> map, User u){
   for (Map.Entry<Session, User> entry : map.entrySet()) {
      if (u.get("id").equals((entry.getValue()).getInteger("id"))) {
          return entry.getKey();
      }
    }
    return null;
  }
}