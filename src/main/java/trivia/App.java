package trivia;
//--------------------------------
//MD5
import java.security.MessageDigest;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import java.util.logging.Level;
import java.util.logging.Logger;
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
  public static String md5Encode(String texto) {
    String output = "";
    try {
      byte[] defaultBytes = texto.getBytes();
      MessageDigest algorithm = MessageDigest.getInstance("MD5");
      algorithm.reset();
      algorithm.update(defaultBytes);
      byte messageDigest[] = algorithm.digest();

      StringBuffer hexString = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++) {
        hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
      }
      //String foo = messageDigest.toString();
      
      output = hexString + "";
      
    } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
      System.out.println("Error");
    }
    return output;
  }
  public static void main( String[] args ){ 

		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    // CSS,INAGES,JS.
    staticFiles.location("/public");

    //Iicio de metodos GET
    //----------------------------------------------------------------------------------------------------------
    //Pagina principal.
    get("/index", (req, res) -> {
      Map map = new HashMap();
      map.put("title", "Bienvenido a Preguntado$");
    	map.put("username", req.queryParams("username"));
    	if(req.session().attribute("username")!=null){
    		map.put("userId", (Integer)req.session().attribute("userId"));
    		map.put("play", "jugar");
    		map.put("logout","Salir");
    	}
      return new ModelAndView(map,"./views/index.html");
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    //Pagina de log de usuarios.
    get("/login", (req, res) -> {
      Map map = new HashMap();
      map.put("title", "Bienvenido a Preguntado$");
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
      Map map = new HashMap();
      map.put("title", "Bienvenido a Preguntado$");
    	if(req.session().attribute("username")!=null){
    		map.put("play", "jugar");
    		map.put("logout","Salir");
    	}
      return new ModelAndView(map, "./views/registrar.html");
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    //Rankign de usuarios.
    get("/ranking", (req, res) -> {
      Map map = new HashMap();
      map.put("title", "Bienvenido a Preguntado$");
    	if(req.session().attribute("username")!=null){
    		map.put("id", req.session().attribute("userId"));
    		map.put("play", "jugar");
    		map.put("logout","Salir");
    	}
      Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
      List<User> users = User.findAll();
      Base.close();
      return new ModelAndView(map, "./views/ranking.html");
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    //Desconectarse.
    get("/logout", (req, res) -> {
      Map logout = new HashMap();
      logout.put("username", null);
    	if(req.session().attribute("username")!=null){
	 			req.session().removeAttribute("username");
        req.session().removeAttribute("userId");
        return new ModelAndView(logout,"./views/index.html"); 
    	}
      return new ModelAndView(logout,"./views/index.html"); 
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
      String passMD5 = md5Encode(password);
      String namee = req.queryParams("username");
    	req.session(true);

	    //Verificamos si existe algun usuario con ese username y esa pass.
	    List<User> unico = User.where("username = ? and password = ?", namee, passMD5);
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
      //Obtenemos el primer juego comenzado (si no tiene juegos iniciados creamos uno nuevo)
      Game game_now = u.getGameInProgress();
      game_now.saveIt();
			Category cat = (new Category()).randomCat();
   		Question que = cat.getQuestion();
    	String[] resQ = {(String)que.get("description"),(String)que.get("a1"),(String)que.get("a2"),(String)que.get("a3"),(String)que.get("a4")};
    	Integer correct = (Integer)que.get("correct_a");
    	//System.out.println("ID DEL JUEGO: " + (Integer)game_now.get("id"));
	    res_play.put("categ", cat.get("name"));
      res_play.put("username", ((String)u.get("username")).toUpperCase());
	    res_play.put("game_id", game_now.get("id"));
	    res_play.put("desc", resQ[0]);
	    res_play.put("a1", resQ[1]);
	    res_play.put("a2", resQ[2]);
	    res_play.put("a3", resQ[3]);
	    res_play.put("a4", resQ[4]);
	    res_play.put("correct", correct);
	    res_play.put("qid", que.get("id"));
	    res_play.put("corrects", game_now.get("corrects"));
	    res_play.put("incorrects", game_now.get("incorrects"));
      res_play.put("logout", "Salir");
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

      List<Game> games = Game.where("id = ?", req.queryParams("gameId"));
      Game game_now = games.get(0);
	    List<User> users = User.where("id = ?", (Integer)req.session().attribute("userId"));
	    User user_now = users.get(0);
	    List<Question> question = Question.where("id = ?", req.queryParams("qId"));
	    Question question_now = question.get(0);

			game_now.set("question_number", (Integer)game_now.get("question_number")+1).saveIt();
			int answer = Integer.valueOf(req.queryParams("answer"));

      //Chequeamos si es la ultima pregunta.
			if((Integer)game_now.get("question_number")==5){
				game_now.set("in_progress", false).saveIt();

			}
      //Validamos la respuesta
      user_now.answerAQuestion(question_now, answer, game_now);
      //user_now.set("score",)
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
      String passMD5= md5Encode(p1);
	  	User u = new User(result[0],result[1], passMD5);
	  	u.saveIt();
	    questt.put("id", u.getId());
      req.session(true);
      req.session().attribute("userId",(Integer)unico.get(0).get("id"));
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
