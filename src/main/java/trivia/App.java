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
	private static final String SESSION_NAME = "username";
  public static void main( String[] args ){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");

		
    staticFiles.location("/public");

 	  //Creamos una categoria, una pregunta y sus respuesta a modo de poder testear le game.
    Category c = new Category("Historia");
    c.saveIt();
    Question q = new Question("Quien fue el anterior presidente de Argentina?", "Macri", "Menem", "Cristina", "Marcelo Tinelli", 3);
    q.set("category_id", c.get("id"));
    q.saveIt();
    String[] catResult = {(String)q.get("description"), (String)q.get("a1"), (String)q.get("a2"), (String)q.get("a3"), (String)q.get("a4")};
    
    Map quest = new HashMap();
    quest.put("desc", catResult[0]);
    quest.put("a1", catResult[1]);
    quest.put("a2", catResult[2]);
    quest.put("a3", catResult[3]);
    quest.put("a4", catResult[4]);

    //Mensaje de Bienvenida
    Map map = new HashMap();
    map.put("logo", "Preguntado$");
    map.put("title", "Bienvenido a Preguntado$");
    map.put("register", "Sistema de registro de usuarios.");

    //Pagina principal.
    get("/index", (req, res) -> {

      return new ModelAndView(map,"./views/index.html");
    }, new MustacheTemplateEngine()
    );

    //Pagina de log de usuarios.
    get("/login", (req, res) -> {
      return new ModelAndView(map, "./views/login.html");
    }, new MustacheTemplateEngine()
    );

    //Pagina de registro de usuarios.
    get("/registrar", (req, res) -> {
      return new ModelAndView(map, "./views/registrar.html");
    }, new MustacheTemplateEngine()
    );

    //Modulo que entrega una pregunta y obtiene una respuesta.
    get("/question", (req, res) -> {
      return new ModelAndView(quest, "./views/test.mustache");
    }, new MustacheTemplateEngine()
    );

    post("/logger", (req,res) -> {
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    	req.session(true); 
    	String namee = req.session().attribute(SESSION_NAME);
    	String password = req.queryParams("password");
      System.out.println(namee);
    	//if((name!=null) && (password != null)){
    		Map resLogin = new HashMap();
    		resLogin.put("username",namee);
    		
    	//}
    	Base.close();
    	return new ModelAndView(resLogin,"./views/index.html");   	
    }, new MustacheTemplateEngine()
    );

  	// Método para tratar los posts de /users (Creación de usuarios).
  	post("/registering", (req, res) -> {
  	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
       
  	// Se cargan los parámetros de la query (URL) en un arreglo
    String[] result = {req.queryParams("username"),req.queryParams("email"),req.queryParams("password")};
  	String body = req.body();
  	User u = new User(result[0],result[1], result[2]);
  	u.saveIt();
    Category cat = (new Category()).randomCat();
    Question que = cat.getQuestion();
    String[] resQ = {(String)que.get("description"),(String)que.get("a1"),(String)que.get("a2"),(String)que.get("a3"),(String)que.get("a4")};
    Map questt = new HashMap();
    questt.put("usernamee", result[0]);
    questt.put("desc", resQ[0]);
    questt.put("a1", resQ[1]);
    questt.put("a2", resQ[2]);
    questt.put("a3", resQ[3]);
    questt.put("a4", resQ[4]);
  	Base.close();
    return new ModelAndView(questt, "./views/test.mustache");
  	}, new MustacheTemplateEngine());
		

    /*
    User u = new User("Gabriel","gabriel.ogh@gmail.com", "12345");
    u.saveIt();
    Game g = new Game((Long)u.getId());
    g.saveIt();
    g.playGame(); 
    //game.play();
  	*/
  	Base.close();
  }
}
