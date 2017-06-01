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
  
    Map map = new HashMap();
    map.put("title", "Bienvenido a Preguntado$");
    map.put("register", "Sistema de registro de usuarios.");

    get("/login", (req, res) -> {
      return new ModelAndView(map, "./views/login.html");
    }, new MustacheTemplateEngine()
    );

 		get("/index", (req, res) -> {
      return new ModelAndView(map,"./views/index.html");
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
			// Se cargan los parámetros de la query (URL)
			String name = req.queryParams("username");
			String email = req.queryParams("email");
			String pass = req.queryParams("password");
			String body = req.body();
			System.out.println(name);
			System.out.println(pass);
			System.out.println(email);
			User u = new User(name,email, pass);
   		u.saveIt();
   		Base.close();
   		return u;
			});
		/*
    User u = new User("Gabriel","gabriel.ogh@gmail.com", "12345");
    u.saveIt();
    Game g = new Game((Long)u.getId());
    g.saveIt();
    g.playGame();*/
		/*  
    //game.play();
		*/
 		Base.close();

		}
}
