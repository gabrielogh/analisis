package trivia;
//--------------------------------
import org.javalite.activejdbc.Base;
import trivia.User;
import java.util.Scanner;
import trivia.Question;
import trivia.Game;
import trivia.Category;
import org.javalite.activejdbc.Base;
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
    map.put("name", "Gabriel");
    map.put("server", "Bienvenido a Preguntado$");



    get("/index", (req, res) -> {
      return new ModelAndView(map, "./views/login.html");
    }, new MustacheTemplateEngine()
    );

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

    get("/question", (req, res) -> {
      return new ModelAndView(quest, "./views/test.mustache");
    }, new MustacheTemplateEngine()
    );


    User u = new User("Gabriel","gabriel.ogh@gmail.com", "12345");
    u.saveIt();
    Game g = new Game((Long)u.getId());
    g.saveIt();
    g.playGame();
		/*  
		Category c = new Category();
 		Question q = new Question();
 		Answer a = new Answer();
 		Answer b = new Answer();
 		Game g = new Game();
 		User u = new User();


 		c.set("name", "Historia");
 		c.saveIt();
 		
 		q.set("id_category", c.getId());
 		q.set("name", "Hola");
 		q.saveIt();

 		a.set("description", "Respuesta correcta");
 		a.set("state", true);
 		a.set("question_id", q.getId());

 		b.set("description", "Respuesta incorrecta");
 		b.set("state", false);
 		b.set("question_id", q.getId());

 		u.set("username", "yo");
 		u.set("password", 123);
 		u.saveIt();

 		g.u.getId());
 		g.saveIt();
    */
    //game.play();

 		Base.close();

		}
}
