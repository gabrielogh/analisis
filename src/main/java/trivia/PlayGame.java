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
import spark.QueryParamsMap;
import java.util.HashMap;
import java.util.Map;


public class PlayGame{
	User jugador;
	Game juego;
	Map results;

	public PlayGame(Request req, Response res){
		Map res_play = new HashMap();
    User aux = new User();
    jugador = aux.getUserById((Integer)req.session().attribute("userId"));
    juego = jugador.getGameInProgress();
    juego.saveIt();
    results = play(juego, jugador, req, res);

	}

	public Map getResults(){
		return results;
	}

	private Map play(Game g, User u, Request req, Response res){
		Map res_play = new HashMap();
	  Category cat;
    Question que;
    String[] resQ = new String[5];
    Integer correct;

   if((int)g.get("question_number")==0){
      res_play.put("newgame","Nuevo juego iniciado");
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
      res_play.put("newgame", "Juego en curso");
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
    res_play.put("admin", req.session().attribute("admin"));

    return res_play;
	}
}