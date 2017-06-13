package trivia;
//--------------------------------
//MD5
import java.security.MessageDigest;
import java.math.BigInteger;
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
    // CSS,INAGES,JS.
    staticFiles.location("/public");

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
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    //Pagina de registro de usuarios.
    get("/registrar", (req, res) -> {
    	if(req.session().attribute("username")!=null){
    		map.put("play", "jugar");
    		map.put("logout","Salir");
    	}
      return new ModelAndView(map, "./views/registrar.html");
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    //Rankign de usuarios.
    get("/ranking", (req, res) -> {
    	if(req.session().attribute("username")!=null){
    		map.put("id", req.session().attribute("userId"));
    		map.put("play", "jugar");
    		map.put("logout","Salir");
    	}
      return new ModelAndView(map, "./views/ranking.html");
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    //Desconectarse.
    get("/logout", (req, res) -> {
    	if(req.session().attribute("username")!=null){
	 			req.session().removeAttribute("username");
        req.session().removeAttribute("userId");
        Map logout = new HashMap();
        return new ModelAndView(logout,"./views/index.html"); 
    	}
      return new ModelAndView(map,"./views/index.html"); 
    }, new MustacheTemplateEngine());

    /**
     * Inicio de sesiones.
     * @param. Session, username, password.
     * @pre. true.
     * @post. Error(User logged) / User logged.
     */
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
			
      //Si encontramos un usuario cargamos la sesion.
			if(!result2){
	      req.session().attribute("username", namee);
	      req.session().attribute("password", password);
	      req.session().attribute("userId",(Integer)unico.get(0).get("id"));
    		resLogin.put("play", "Jugar");
        resLogin.put("logout","Salir");
    		Base.close();
	    }
	    else{
	    	Base.close();
	    	resLogin.put("error","Usuario o password incorrectos");
	    	return new ModelAndView(resLogin,"./views/login.html");
	    }
    	
    	return new ModelAndView(resLogin,"./views/index.html");   	
    }, new MustacheTemplateEngine());

    /**
     * 
     * Modulo que entrega una pregunta y obtiene una respuesta.
     * @param. user id.
     * @pre. user logged.
     * @post. question_number inrease. User profile updated.
     */
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
      res_play.put("username", ((String)u.get("username")).toUpperCase());
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
    }, new MustacheTemplateEngine());
    
    /**
     * Metodo para responder una pregunta
     * @param. game id., user id, question id
     * @pre. user logged, game.get("in_progress") = true, .
     * @post. question_number inrease. User profile updated.
     */
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
			int answer = Integer.valueOf(req.queryParams("answer"));

      //Chequeamos si es la ultima pregunta.
			if((Integer)game_now.get("question_number")==5){
				game_now.set("in_progress", false).saveIt();

			}
      //Validamos la respuesta
			if(question_now.validateA(answer)){
				user_now.set("c_questions", (Integer)user_now.get("c_questions")+1).saveIt();
        game_now.set("corrects", (Integer)game_now.get("corrects")+1).saveIt();
	    }
	    else{
				user_now.set("i_questions", (Integer)user_now.get("i_questions")+1).saveIt();
        game_now.set("incorrects", (Integer)game_now.get("incorrects")+1).saveIt();
	    }
 			Base.close();
 			String link = "play";
  		res.redirect(link);
  		return null;	
    });

    /**
  	 * Método para tratar los posts de /users (Creación de usuarios).
     * @param. username., user id, question id, password, password2
     * @pre. username != "", password == password2, email != "".
     * @post. User registered / Error.
     */
  	post("/registering", (req, res) -> {
  	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
       
  	// Se cargan los parámetros de la query (URL) en un arreglo
    String[] result = {req.queryParams("username"),req.queryParams("email"),(String)req.queryParams("password"),(String)req.queryParams("password2")};
  	String body = req.body();
  	Map questt = new HashMap();

    String p1 = new String(req.queryParams("password"));
    String p2 = new String(req.queryParams("password2"));

    if(!(p1.equals(p2))){
    	Base.close();
      questt.put("error","Las contraseñas no coinciden, vuelva a intentarlo");
      return new ModelAndView(questt,"./views/registrar.html");
    }
    List<User> unico = User.where("username = ? or mail = ? ", result[0], result[1]);
    Boolean result2 = unico.size()==0;
  	if(result2){
      /*
      //Codificacion password MD5.
      MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
      md.update(((String)result[2]).getBytes());
      byte[] digest = md.digest();
      String pas;
      for (byte b : digest) {
         pas=(pas+(Integer.toHexString(0xFF & b)));
      }
      */
	  	User u = new User(result[0],result[1], result[2]);
	  	u.saveIt();
	    questt.put("id", u.getId());
	    Base.close();
	  }
    else{
      User u = unico.get(0);
      String e = (String)u.get("mail");
      if(e.equals(result[1])){
      	Base.close();
      	questt.put("error","Ese e-mail ya se encuentra registrado, intente con otro");
      	return new ModelAndView(questt,"./views/registrar.html");
      }
      else{
        Base.close();
        questt.put("error","Ese usuario ya existe, intente con otro");
        return new ModelAndView(questt,"./views/registrar.html");
      }
    }

    return new ModelAndView(questt, "./views/play.html");
  	}, new MustacheTemplateEngine());
		
    //Fin.
  	Base.close();
  }
}
