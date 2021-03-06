package trivia;
//--------------------------------
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.validation.UniquenessValidator;
import trivia.User;
import trivia.Question;
import trivia.Game;
import trivia.Category;
import trivia.Md5Cipher;
import trivia.PlayGame;
import trivia.LoginServer;
import trivia.Versusmode;
import trivia.EchoWebSocket;

//----------------------------------
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
//--------------------------------
import static spark.Spark.*;
import static spark.Spark.staticFileLocation;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import static j2html.TagCreator.*;
import com.google.gson.Gson;
//---------------------------------

public class App{

  // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
  static Map<Session, String> userUsernameMap = new ConcurrentHashMap<>();
  static String nextUserName; //Assign to username for next connecting user
  static int nextUserNumber = 0;
  

  public static void create_vs_game(Map data){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    String[] userNames = (String[])data.values().toArray(new String[data.size()]);
    User p1 = new User();
    User p2 = new User();
    p1 = p1.getUserByName(userNames[nextUserNumber]);
    p2 = p2.getUserByName(userNames[nextUserNumber+1]);
    Versusmode play = new Versusmode(p1, p2);
    play.saveIt();
    Base.close();
  }

  //Sends a message from one user to all users, along with a list of current usernames
  public static void cambiarTurno(String sender, String message) {
    userUsernameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
        try {

            session.getRemote().sendString(String.valueOf(new JSONObject()
                .put("userMessage", createHtmlMessageFromSender(sender, message))
                .put("userTurno", nextUserName)
                .put("userlist", userUsernameMap.values())
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
  }

  private static String createHtmlMessageFromSender(String sender,String message) {
      return article(b("Turno: " + sender),p(message)).render();
  }

  public static void main( String[] args ){
    // CSS,IMAGES,JS.
    staticFiles.location("/public");
    
    webSocket("/App", EchoWebSocket.class);
    
    before((req, res)->{
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    });

    after((req, res) -> {
      Base.close();
    });


    //Iicio de metodos GET
    //----------------------------------------------------------------------------------------------------------
    //Pagina principal.
    get("/index", (req, res) -> {

      Map map = new HashMap();
      map.put("title", "Bienvenido a Preguntado$");
    	if(req.session().attribute("username")!=null){
    		map.put("userId", (int)req.session().attribute("userId"));
        map.put("username", req.queryParams("username"));
    		map.put("play", "<li><a href='/play'>Jugar</a></li>");
    		map.put("logout","<li><a href='/logout'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
        if((String)req.session().attribute("admin") != null){
          map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
        }
    	}
      return new ModelAndView(map,"./views/index.html");
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    get("/playonline", (req, res) -> {
      Map map = new HashMap();
      map.put("title", "Bienvenido a Preguntado$");
      nextUserName = (String)req.session().attribute("username");
      return new ModelAndView(map,"./views/playOnline.html");
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    //Pagina de log de usuarios.
    get("/login", (req, res) -> {
      Map map = new HashMap();
      map.put("title", "Bienvenido a Preguntado$");
    	if(req.session().attribute("username")!=null){
    		map.put("id", req.session().attribute("userId"));
    		map.put("admin", req.session().attribute("admin"));
        map.put("play", "<li><a href='/play'>Jugar</a></li>");
        map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
        if((String)req.session().attribute("admin") != null){
          map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
        }
    	}
      return new ModelAndView(map, "./views/login.html");
    }, new MustacheTemplateEngine());

    //----------------------------------------------------------------------------------------------------------
    /**
     * Panel de Administracion.
     * @param. username.
     * @pre. Session Started & AccesLevel == 5.
     * @post. -.
     */
    get("/administrate", (req, res) -> {
      Map map = new HashMap();
      map.put("title", "Panel de Administracion");
      if(req.session().attribute("username")!=null) {
        map.put("id", req.session().attribute("userId"));
        map.put("play", "<li><a href='/play'>Jugar</a></li>");
        map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
        if((String)req.session().attribute("admin") != null){
          map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
        }
      }
      else{
	 			return new ModelAndView(map, "./views/index.html");
      }
      return new ModelAndView(map, "./views/adminPanel/administrate.html");
    }, new MustacheTemplateEngine());

   //----------------------------------------------------------------------------------------------------------
    /**
     * Panel de Administracion.
     * @param. username.
     * @pre. Session Started & AccesLevel == 5.
     * @post. -.
     */
    get("/generatec", (req, res) -> {
      Map map = new HashMap();
      map.put("title", "Panel de Administracion");
      if(req.session().attribute("username")!=null) {
        map.put("id", req.session().attribute("userId"));
        map.put("play", "<li><a href='/play'>Jugar</a></li>");
        map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
        if((String)req.session().attribute("admin") != null){
          map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
        }
        else{
          return new ModelAndView(map, "./views/adminPanel/generate_cat.html");
        }
      }
      else{
        return new ModelAndView(map, "./views/index.html");
      }
      return new ModelAndView(map, "./views/adminPanel/administrate.html");
    }, new MustacheTemplateEngine());

       //----------------------------------------------------------------------------------------------------------
    /**
     * Panel de Administracion.
     * @param. username.
     * @pre. Session Started & AccesLevel == 5.
     * @post. -.
     */
    get("/generateq", (req, res) -> {
      Map map = new HashMap();
      map.put("title", "Panel de Administracion");
      if(req.session().attribute("username")!=null) {
        map.put("id", req.session().attribute("userId"));
        map.put("play", "<li><a href='/play'>Jugar</a></li>");
        map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
        if((String)req.session().attribute("admin") != null){
          map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
        }
        else{
          return new ModelAndView(map, "./views/index.html");
        }
      }
      else{
        return new ModelAndView(map, "./views/adminPanel/generate_quest.html");
      }
      return new ModelAndView(map, "./views/adminPanel/administrate.html");
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * User register.
     * @param username.
     * @pre. true.
     * @post. Error(User logged) | User exists | User saved.
     */
    get("/registrar", (req, res) -> {
      Map map = new HashMap();
      map.put("title", "Bienvenido a Preguntado$");
    	if(req.session().attribute("username")!=null){
        map.put("play", "<li><a href='/play'>Jugar</a></li>");
        map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
        if((String)req.session().attribute("admin") != null){
          map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
        }
    	}
      return new ModelAndView(map, "./views/registrar.html");
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * Ranking.
     * @param.
     * @pre. true.
     * @post. --
     */
    get("/ranking",(req,res)->{
      Map rank = new HashMap();
      List<User> top_10 = User.findBySQL("select * from users order by c_questions desc limit 10");
      rank.put("ranking",top_10);
      rank.put("play", "<li><a href='/play'>Jugar</a></li>");
      rank.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
      if((String)req.session().attribute("admin") != null){
        rank.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
      }
      return new ModelAndView(rank,"./views/ranking.html");
    },new MustacheTemplateEngine());
  
    //----------------------------------------------------------------------------------------------------------
    /**
     * Fin de sesiones.
     * @param. Session, username, password.
     * @pre. Session initializen.
     * @post. User logged out.
     */
    get("/logout", (req, res) -> {
      Map logout = new HashMap();
      logout.put("username", null);
    	if(req.session().attribute("username")!=null){
	 			req.session().removeAttribute("username");
        req.session().removeAttribute("userId");
        req.session().removeAttribute("admin");
        req.session().removeAttribute("play");
        req.session().removeAttribute("admin");
        return new ModelAndView(logout,"./views/index.html"); 
    	}
      return new ModelAndView(logout,"./views/index.html"); 
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * Inicio de sesiones.
     * @param. Session, username, password.
     * @pre. true.
     * @post. Error(User logged) / User logged.
     */
    post("/logger", (req,res) -> {
    	Map resLogin = new HashMap();
      LoginServer login = new LoginServer(req,res);
      resLogin = login.getResults();
      if((String)resLogin.get("error") != null){
        return new ModelAndView(resLogin,"./views/login.html"); 
      }
      if((String)resLogin.get("admin") != null){
        return new ModelAndView(resLogin,"./views/adminPanel/administrate.html"); 
      }    
    	return new ModelAndView(resLogin,"./views/index.html");   	
    }, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * 
     * Modulo que entrega una pregunta y obtiene una respuesta.
     * @param. user id.
     * @pre. user logged.
     * @post. question_number inrease. User profile updated.
     */
    get("/play", (req, res) -> {
      Map res_play = new HashMap();
      PlayGame starter = new PlayGame(req, res);
      res_play = starter.getResults();
      return new ModelAndView(res_play, "./views/play.html");
    }, new MustacheTemplateEngine());
    
    /**
     * Metodo para responder una pregunta
     * @param. game id., user id, question id
     * @pre. user logged, game.get("in_progress") = true, .
     * @post. question_number inrease. User profile updated.
     */
    post("/answer", (req,res) -> {
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
    User newUser = new User();
    Map result = newUser.registerUser(req,res);
    if((String)result.get("error") != null){
      return new ModelAndView(result,"./views/registrar.html"); 
    }
    if((String)result.get("success") != null){
      return new ModelAndView(result,"./views/registrar.html"); 
    }
    return new ModelAndView(result, "./views/index.html");
  	}, new MustacheTemplateEngine());
		
    //Fin.
  }
}
