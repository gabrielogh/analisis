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
import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import org.eclipse.jetty.websocket.api.Session;
//---------------------------------

/**
 * This class represents our Java Application.
 * when a user navigates the web this class controls said navigation.
 * It also takes care of keeping track of users online and of users playing.
 */

public class App{

  // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
  static Map<Session,User> userUsernameMap = new ConcurrentHashMap<>();
  static Map<Session,User> usersPlaying = new ConcurrentHashMap<>();

  public static void main( String[] args ){

    staticFiles.location("/public");
    webSocket("/playOnline", EchoWebSocket.class);
    
    before((req, res)->{Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");});
    after((req, res) -> {Base.close();});

    //GET methods init
    //----------------------------------------------------------------------------------------------------------
    //Dashboard page.
    /**
     * Panel de Administracion.
     * @param. -.
     * @pre. -.
     * @post. main page view loaded.
     */
    get("/", LoginServer::index,new MustacheTemplateEngine());
    /**
     * View for a Online games (multiplayer with 1vs1 mode).
     * @param. username.
     * @pre. Session Started (username!=null).
     * @post. PlayOnline view loaded.
     */
    //----------------------------------------------------------------------------------------------------------
    get("/playonline", PlayGame::playOnline,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * LoginIn form for registered users.
     * @param. -.
     * @pre. -.
     * @post. Login page view loaded.
     */
    get("/login", LoginServer::login,new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * AdminPanel for Admin Users.
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
    //----------------------------------------------------------------------------------------------------------
    /**
     * Método que permite al administrador generar nuevas preguntas.
     * @param. username., user id, question id, password, password2
     * @pre. username != "", password == password2, email != "".
     * @post. User registered / Error.
     */
    //post("/sendquestion", LoginServer::sendQuestion, new MustacheTemplateEngine());
    //----------------------------------------------------------------------------------------------------------
    /**
     * Método que permite al administrador generar categorias.
     * @param. username., user id, question id, password, password2
     * @pre. username != "", password == password2, email != "".
     * @post. User registered / Error.
     */
    post("/sendcategory", LoginServer::sendCategory, new MustacheTemplateEngine());
  }
}