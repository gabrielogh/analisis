package trivia;
//--------------------------------
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.validation.UniquenessValidator;
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
    get("/index", LoginServer::index,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    get("/playOnline", PlayGame::playOnline,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    //Pagina de log de usuarios.
    get("/login", LoginServer::login,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * Panel de Administracion.
     * @param. username.
     * @pre. Session Started & AccesLevel == 5.
     * @post. -.
     */
    get("/administrate", LoginServer::administrate,new MustacheTemplateEngine());
   //----------------------------------------------------------------------------------------------------------
    /**
     * Panel de Administracion.
     * @param. username.
     * @pre. Session Started & AccesLevel == 5.
     * @post. -.
     */
    get("/generatec", LoginServer::generatec,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * Panel de Administracion.
     * @param. username.
     * @pre. Session Started & AccesLevel == 5.
     * @post. -.
     */
    get("/generateq", LoginServer::generateq,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * User register.
     * @param username.
     * @pre. true.
     * @post. Error(User logged) | User exists | User saved.
     */
    get("/registrar", LoginServer::registrar,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * Ranking.
     * @param.
     * @pre. true.
     * @post. --
     */
    get("/ranking", LoginServer::ranking,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * Fin de sesiones.
     * @param. Session, username, password.
     * @pre. Session initializen.
     * @post. User logged out.
     */
    get("/logout", LoginServer::logOut,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * Inicio de sesiones.
     * @param. Session, username, password.
     * @pre. true.
     * @post. Error(User logged) / User logged.
     */
    post("/logger", LoginServer::loginOn, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * 
     * Modulo que entrega una pregunta y obtiene una respuesta.
     * @param. user id.
     * @pre. user logged.
     * @post. question_number inrease. User profile updated.
     */
    get("/play", PlayGame::playGame,new MustacheTemplateEngine());
    
    /**
     * Metodo para responder una pregunta
     * @param. game id., user id, question id
     * @pre. user logged, game.get("in_progress") = true, .
     * @post. question_number inrease. User profile updated.
     */
    post("/answer", PlayGame::answer, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
  	 * Método para tratar los posts de /users (Creación de usuarios).
     * @param. username., user id, question id, password, password2
     * @pre. username != "", password == password2, email != "".
     * @post. User registered / Error.
     */
    post("/registering", LoginServer::registrarUser, new MustacheTemplateEngine());
		
    //Fin.
  }
}
