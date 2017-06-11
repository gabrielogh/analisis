package trivia;
//--------------------------------
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.validation.UniquenessValidator;
import trivia.User;
import trivia.Question;
import trivia.Game;
import trivia.Category;
//----------------------------------
import java.util.List;
import java.util.HashMap;
import java.util.Map;
//--------------------------------
import static spark.Spark.*;
import static spark.Spark.staticFileLocation;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
//---------------------------------

public class App{
  public static void main( String[] args ){ 


		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");

		
    staticFiles.location("/public");
    /*
 	  //Creamos una categoria, una pregunta y sus respuesta a modo de poder testear le game.
    Category c = new Category("Historia");
    c.saveIt();
    Question q = new Question("Quien fue el anterior presidente de Argentina?", "Macri", "Menem", "Cristina", "Marcelo Tinelli", 3);
    q.set("category_id", c.get("id"));
    q.saveIt();
    String[] catResult = {(String)q.get("description"), (String)q.get("a1"), (String)q.get("a2"), (String)q.get("a3"), (String)q.get("a4")};
    
     	  //Creamos una categoria, una pregunta y sus respuesta a modo de poder testear le game.
    Category ciencia = new Category("Ciencia");
    ciencia.saveIt();
    Question qciencia = new Question("Cual de los siguientes numeros binarios representa el 10 decimal?", "0000010", "0001010", "11111111", "0001000", 2);
    qciencia.set("category_id", ciencia.get("id"));
    qciencia.saveIt();
    
     	  //Creamos una categoria, una pregunta y sus respuesta a modo de poder testear le game.
    Category ent = new Category("Entrenenimiento");
    ent.saveIt();
    Question qent = new Question("Como le dicen a Macri?", "Pato", "Raro", "Malo", "Gatooooooo", 4);
    qent.set("category_id", ent.get("id"));
    qent.saveIt();
    
     	  //Creamos una categoria, una pregunta y sus respuesta a modo de poder testear le game.
    Category cdep = new Category("Deporte");
    cdep.saveIt();
    Question qdep = new Question("Por cuantos goles le ganaria River a Boca si jugaran hoy?", "1-0", "2-0", "4-0", "Boca no asistiria", 4);
    qdep.set("category_id", cdep.get("id"));
    qdep.saveIt();
    
    Question q= new
    Map quest = new HashMap();
    quest.put("desc", catResult[0]);
    quest.put("a1", catResult[1]);
    quest.put("a2", catResult[2]);
    quest.put("a3", catResult[3]);
    quest.put("a4", catResult[4]);
    */

    //Mensaje de Bienvenida
    Map map = new HashMap();
    map.put("logo", "Preguntado$");
    map.put("title", "Bienvenido a Preguntado$");

    //Iicio de metodos GET
    //----------------------------------------------------------------------------------------------------------
    //Pagina principal.
    get("/index", (req, res) -> {
    		map.put("username", req.queryParams("username"));
    	if(req.session().attribute("username")!=null){
    		map.put("userId", (Integer)req.session().attribute("userId"));
    		map.put("play", "jugar");
    		map.put("logout","Salir");
    	}
      return new ModelAndView(map,"./views/index.html");
    }, new MustacheTemplateEngine()
    );
    //----------------------------------------------------------------------------------------------------------
    //Pagina de log de usuarios.
    get("/login", (req, res) -> {
    	if(req.session().attribute("username")!=null){
    		map.put("id", req.session().attribute("userId"));
    		map.put("play", "jugar");
    		map.put("logout","Salir");
    	}
      return new ModelAndView(map, "./views/login.html");
    }, new MustacheTemplateEngine()
    );
    //----------------------------------------------------------------------------------------------------------
    //Pagina de registro de usuarios.
    get("/registrar", (req, res) -> {
    	if(req.session().attribute("username")!=null){
    		map.put("play", "jugar");
    		map.put("logout","Salir");
    	}
      return new ModelAndView(map, "./views/registrar.html");
    }, new MustacheTemplateEngine()
    );
    //----------------------------------------------------------------------------------------------------------
    //Rankign de usuarios.
    get("/ranking", (req, res) -> {
    	if(req.session().attribute("username")!=null){
    		map.put("id", req.session().attribute("userId"));
    		map.put("play", "jugar");
    		map.put("logout","Salir");
    	}
      return new ModelAndView(map, "./views/ranking.html");
    }, new MustacheTemplateEngine()
    );
    //----------------------------------------------------------------------------------------------------------
    //Desconectarse.
    get("/logout", (req, res) -> {
    	if(req.session().attribute("username")!=null){
	 			req.session(false);
    	}
      return new ModelAndView(map, "./views/ranking.html");
    }, new MustacheTemplateEngine()
    );

    //----------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------
    //Inicio de sesiones.
    post("/logger", (req,res) -> {
    	Map resLogin = new HashMap();
    	//Si ya estmos conectados, no podemos conectarnos nuevamente.
    	if(req.session().attribute("username")!=null){
    		resLogin.put("id", (Integer)req.session().attribute("userId"));
    		resLogin.put("play", "jugar");
    		resLogin.put("error","Ya estas conectado como: "+ req.session().attribute("username"));
	    	return new ModelAndView(resLogin,"./views/login.html");
    	}

    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    	String password = req.queryParams("password");
      String namee = req.queryParams("username");
    	req.session(true);

	    //Verificamos si existe algun usuario con ese username y esa pass.
	    List<User> unico = User.where("username = ? and password = ?", namee,password);
	    Boolean result2 = unico.size()==0;
			
			if(!result2){
	      req.session().attribute("username", namee);
	      req.session().attribute("password", password);
	      req.session().attribute("userId",(Integer)unico.get(0).get("id"));
    		resLogin.put("play", "Jugar");
    		Base.close();
	    }
	    else{
	    	Base.close();
	    	resLogin.put("error","Usuario o password incorrectos");
	    	return new ModelAndView(resLogin,"./views/login.html");
	    }
    	
    	return new ModelAndView(resLogin,"./views/index.html");   	
    }, new MustacheTemplateEngine()
    );
    //----------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------
    //Modulo que entrega una pregunta y obtiene una respuesta.
    get("/play", (req, res) -> {
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
      Map res_play = new HashMap();

      //Obtenemos el Usuario actual
      List<User> user_now = User.where("id = ?", (Integer)req.session().attribute("userId"));
      User u = user_now.get(0);
      System.out.println("ID DEL USER: " + (Integer)u.get("id"));
      //Obtenemos el primer juego comenzado (si no tiene juegos iniciados creamos uno nuevo)
      Game game_now = u.getGameInProgress();
      game_now.saveIt();
			Category cat = (new Category()).randomCat();
   		Question que = cat.getQuestion();
    	String[] resQ = {(String)que.get("description"),(String)que.get("a1"),(String)que.get("a2"),(String)que.get("a3"),(String)que.get("a4")};
    	Integer correct = (Integer)que.get("correct_a");
    	//System.out.println("ID DEL JUEGO: " + (Integer)game_now.get("id"));
	    res_play.put("categ", (String)cat.get("name"));
	    res_play.put("game_id", game_now.get("id"));
	    res_play.put("desc", resQ[0]);
	    res_play.put("a1", resQ[1]);
	    res_play.put("a2", resQ[2]);
	    res_play.put("a3", resQ[3]);
	    res_play.put("a4", resQ[4]);
	    res_play.put("correct", correct);
	    res_play.put("qid", que.get("id"));
	    res_play.put("corrects",(Integer)game_now.get("corrects"));
	    res_play.put("incorrects",(Integer)game_now.get("incorrects"));
      Base.close();
      return new ModelAndView(res_play, "./views/play.html");
    }, new MustacheTemplateEngine()
    );
    //----------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------
    //Metodo para responder una pregunta
    post("/answer", (req,res) -> {
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");

      //System.out.println("ID DEL JUEGO POST: " + (Integer)req.queryParams("gameId"));
      List<Game> games = Game.where("id = ?", req.queryParams("gameId"));
      Game game_now = games.get(0);
	    List<User> users = User.where("id = ?", (Integer)req.session().attribute("userId"));
	    User user_now = users.get(0);
	    List<Question> question = Question.where("id = ?", req.queryParams("qId"));
	    Question question_now = question.get(0);

			Map resAnswer = new HashMap();

			game_now.set("question_number", (Integer)game_now.get("question_number")+1).saveIt();
			Integer answer = Integer.valueOf(req.queryParams("answer"));

			if((Integer)game_now.get("question_number")==5){
				game_now.set("in_progress", false).saveIt();

			}

      System.out.println("RESPUESTA CORRECTA: " + (Integer)question_now.get("correct_a"));
      System.out.println("RESPUESTA ACTUAL: " + answer);
      System.out.println("RESULTADO: "+ question_now.validateA(answer));

			if(question_now.validateA(answer)){
				user_now.set("c_questions", (Integer)user_now.get("c_questions")+1).saveIt();
        game_now.set("corrects", (Integer)game_now.get("corrects")+1).saveIt();
	    }
	    else{
				user_now.set("i_questions", (Integer)user_now.get("i_questions")+1).saveIt();
        game_now.set("incorrects", (Integer)game_now.get("incorrects")+1).saveIt();
	    }
 			Base.close();
 			String link = "play?id=" + req.session().attribute("userId");
  		res.redirect(link);
  		return null;	
    });
    //----------------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------------
  	// Método para tratar los posts de /users (Creación de usuarios).
  	post("/registering", (req, res) -> {
  	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
       
  	// Se cargan los parámetros de la query (URL) en un arreglo
    String[] result = {req.queryParams("username"),req.queryParams("email"),(String)req.queryParams("password"),(String)req.queryParams("password2")};
  	String body = req.body();
  	Map questt = new HashMap();
    System.out.println("Password 1: " + result[2]);
    System.out.println("Password 2: " + result[3]);

    if(result[2] != result[3]){
      questt.put("error","Las contraseñas no coincide, vuelva a intentarlo");
      return new ModelAndView(questt,"./views/registrar.html");
    }
    List<User> unico = User.where("username = ? or mail = ? ", result[0], result[1]);
    Boolean result2 = unico.size()==0;
  	if(result2){
	  	User u = new User(result[0],result[1], result[2]);
	  	u.saveIt();
	    questt.put("id", u.getId());
	    Base.close();
	  }
    else{
    	Base.close();
    	questt.put("error","Ese usuario o e-mail ya existe, intente con otro");
    	return new ModelAndView(questt,"./views/registrar.html");
    }

  	
    return new ModelAndView(questt, "./views/play.html");
  	}, new MustacheTemplateEngine());
		
  	Base.close();
  }
}
