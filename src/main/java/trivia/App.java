package trivia;
//--------------------------------
import org.javalite.activejdbc.Base;
import trivia.User;
import java.util.Scanner;
import trivia.Question;
import trivia.Game;
import trivia.Category;
//----------------------------------
import java.util.List;
import java.util.HashMap;
import java.util.Map;
//--------------------------------
import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
//---------------------------------

public class App{
    public static void main( String[] args ){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    //Mensaje de Bienvenida
    Map map = new HashMap();
    map.put("title", "Bienvenido a Preguntado$");
    map.put("register", "Sistema de registro de usuarios.");

    //Pagina principal.
    get("/index", (req, res) -> {
      return new ModelAndView(map,"./views/index.html");
    }, new MustacheTemplateEngine()
    );

    //Pagina de registro de usuarios.
    get("/login", (req, res) -> {
      return new ModelAndView(map, "./views/login.html");
    }, new MustacheTemplateEngine()
    );



 	//Creamos una categoria, una pregunta y sus respuesta a modo de poder testear le game.
    Category c = new Category("Historia");
    c.saveIt();
    Question q = new Question("Quien es el actual presidente de Argentina?", "Macri", "Menem", "Cristina", "Marcelo Tinelli", 1);
    q.set("category_id", c.get("id"));
    q.saveIt();
    String descrip = (String)q.get("description");
    String an1 = (String)q.get("a1");
    String an2 = (String)q.get("a2");
    String an3 = (String)q.get("a3");
    String an4 = (String)q.get("a4");
    Map quest = new HashMap();
    quest.put("desc", descrip);
    quest.put("a1", an1);
    quest.put("a2", an2);
    quest.put("a3", an3);
    quest.put("a4", an4);


    //Modulo que entrega una pregunta y obtiene una respuesta.
    get("/question", (req, res) -> {
      return new ModelAndView(quest, "./views/test.mustache");
    }, new MustacheTemplateEngine()
    );


	// Método para tratar los posts de /users (Creación de usuarios)
	post("/login2", (req, res) -> {
	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    
	// Se cargan los parámetros de la query (URL) en un arreglo
    String[] result = {req.queryParams("username"),req.queryParams("username"),req.queryParams("email"),req.queryParams("password")};
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
