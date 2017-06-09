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
    get("/play", (req, res) -> {
      Map res_play = new HashMap();
      res_play.put("username",req.queryParams("username"));
      return new ModelAndView(res_play, "./views/play.html");
    }, new MustacheTemplateEngine()
    );

    post("/logger", (req,res) -> {
    	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    	req.session(true);
    	String password = req.queryParams("password");
      String namee = req.queryParams("username");
      //Verificamos si existe algun usuario con ese username y esa pass.
	    List<User> unico = User.where("username = ? and password = ?", namee,password);
	    Boolean result2 = unico.size()==0;
			Map resLogin = new HashMap();
			if(!result2){
	      req.session().attribute("username", namee);
	      req.session().attribute("password", password);
    		resLogin.put("username",namee);
    		resLogin.put("play", "Jugar");
	    }
	    else{
	    	System.out.println("Usuario incorrecto");
	    }
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
  	Map questt = new HashMap();
    List<User> unico = User.where("username = ? or mail = ? ", result[0], result[1]);
    Boolean result2 = unico.size()==0;
  	if(result2){
	  	User u = new User(result[0],result[1], result[2]);
	  	u.saveIt();
	    Category cat = (new Category()).randomCat();
	    Question que = cat.getQuestion();
	    String[] resQ = {(String)que.get("description"),(String)que.get("a1"),(String)que.get("a2"),(String)que.get("a3"),(String)que.get("a4")};
	    questt.put("categ", cat.get("name"));
	    questt.put("usernamee", result[0]);
	    questt.put("desc", resQ[0]);
	    questt.put("a1", resQ[1]);
	    questt.put("a2", resQ[2]);
	    questt.put("a3", resQ[3]);
	    questt.put("a4", resQ[4]);
	    questt.put("play", "play");
	    questt.put("Usuario", "Usuario");
	    questt.put("id", u.getId());
	  }
	  else{
	  	System.out.println("El usuario ya existe");
	  }
  	Base.close();
    return new ModelAndView(questt, "./views/play.html");
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
