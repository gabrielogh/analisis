package trivia;

import trivia.User;
import trivia.Game;
import trivia.Category;
import trivia.Question;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.QueryParamsMap;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.eclipse.jetty.websocket.api.Session;

public class PlayGame{
	private static User jugador;
	private static Game juego;
	private static Map results;

	public static ModelAndView playGame(Request req, Response res){
    User aux = new User();

    if(req.session().attribute("username")!=null){
      jugador = aux.getUserById((Integer)req.session().attribute("userId"));
      juego = jugador.getGameInProgress(false);
      juego.saveIt();
    }
    results = play(juego, jugador, req, res);
    return new ModelAndView(results, "./views/play.html");
	}

	public static Map getResults(){
		return results;
	}

  public static JSONObject generateQuestion(Game game){
    JSONObject json = new JSONObject();
    Category cat;
    Question que;
    Integer correct;

    if((Integer)game.get("question_number")==0){
      que = game.getCurrentQuestion();
      cat = game.getCurrentCategory();
      correct = (Integer)que.get("correct_a");
      json.put("token", "sendQuestion");
      json.put("user_id", game.getInteger("user_id"));
      json.put("question_id", que.getInteger("id"));
      json.put("question", que.getString("description"));
      json.put("option1", que.getString("a1"));
      json.put("option2", que.getString("a2"));
      json.put("option3", que.getString("a3"));
      json.put("option4", que.getString("a4")); 
      json.put("correct", que.getInteger("correct_a"));
      return json;
    }
    else{
      //if((Boolean)game.get("current_question_state")){
        cat = (new Category()).randomCat();
        que = cat.getQuestion();
        game.set("current_question_id",  que.getInteger("id"));
        game.set("current_question_state", false);
        game.saveIt();
      //}
      /*else{
        que = game.getCurrentQuestion();
        cat = game.getCurrentCategory();
      }*/
      json.put("user_id", game.getInteger("user_id"));
      json.put("token","sendQuestion");
      json.put("question_id", que.getInteger("id"));
      json.put("question", que.getString("description"));
      json.put("option1", que.getString("a1"));
      json.put("option2", que.getString("a2"));
      json.put("option3", que.getString("a3"));
      json.put("option4", que.getString("a4")); 
      json.put("correct", que.getString("correct_a"));
    }
    return json;
  }

	private static Map play(Game g, User u, Request req, Response res){
		Map res_play = new HashMap();
	  Category cat;
    Question que;
    String[] resQ = new String[5];
    Integer correct;

   if((int)g.get("question_number")==0){
      res_play.put("newgame","<div class='alert alert-success' id='alert-success'><strong>Atento!</strong> Nuevo Juego Iniciado!</div>");
      que = g.getCurrentQuestion();
      cat = g.getCurrentCategory();
      resQ[0] = (String)que.get("description");
      resQ[1] = (String)que.get("a1");
      resQ[2] = (String)que.get("a2");
      resQ[3] = (String)que.get("a3");
      resQ[4] = (String)que.get("a4");
      correct = (Integer)que.get("correct_a");
    }
    else{
      if((Boolean)g.get("current_question_state")){
        cat = (new Category()).randomCat();
        que = cat.getQuestion();
        g.set("current_question_id", (Integer)que.get("id"));
        g.set("current_question_state", false);
        g.saveIt();

   		}
   		else{
        que = g.getCurrentQuestion();
        cat = g.getCurrentCategory();
   		}
      resQ[0] = (String)que.get("description");
      resQ[1] = (String)que.get("a1");
      resQ[2] = (String)que.get("a2");
      resQ[3] = (String)que.get("a3");
      resQ[4] = (String)que.get("a4");
      correct = (Integer)que.get("correct_a");
    }
    res_play.put("categ", cat.get("name"));
    res_play.put("username", ((String)u.get("username")).toUpperCase());
    res_play.put("game_id", g.get("id"));
    res_play.put("desc", resQ[0]);
    res_play.put("a1", resQ[1]);
    res_play.put("a2", resQ[2]);
    res_play.put("a3", resQ[3]);
    res_play.put("a4", resQ[4]);
    res_play.put("correct", correct);
    res_play.put("qid", que.get("id"));
    res_play.put("corrects", g.get("corrects"));
    res_play.put("incorrects", g.get("incorrects"));
    res_play.put("logout", "Salir");
    if(req.session().attribute("admin") != null){
      req.session().attribute("admin", true);
      res_play.put("admin","<li><a href='/administrate'>Administrar</a></li>");
    }

    return res_play;
	}

  public static ModelAndView answer(Request req, Response res){
    List<Game> games = Game.where("id = ?", req.queryParams("gameId"));
    Game game_now = games.get(0);
    List<User> users = User.where("id = ?", (Integer)req.session().attribute("userId"));
    User user_now = users.get(0);
    List<Question> question = Question.where("id = ?", req.queryParams("qId"));
    Question question_now = question.get(0);

    game_now.set("question_number", (Integer)game_now.get("question_number")+1).saveIt();
    int answer = Integer.valueOf(req.queryParams("answer"));

    if((Integer)game_now.get("question_number")==5){
      game_now.set("in_progress", false).saveIt();

    }
    user_now.answerAQuestion(question_now, answer, game_now);
    
    return playGame(req, res);
  }

  public static ModelAndView playOnline(Request req, Response res){
    Map map = new HashMap();
    map.put("title", "Bienvenido a Preguntado$");
    if(req.session().attribute("username")!=null){
      map.put("user_id", (Integer)req.session().attribute("userId"));
      map.put("username", (String)req.session().attribute("username"));
    }
    else{
      return new ModelAndView(map,"./views/index.html");
    }
    return new ModelAndView(map,"./views/playOnline.html");
  }

}