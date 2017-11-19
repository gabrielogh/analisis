package trivia;

import trivia.User;
import trivia.Md5Cipher;
import org.javalite.activejdbc.Base;
import java.util.List;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.QueryParamsMap;
import java.util.HashMap;
import java.util.Map;

public class LoginServer{
	private static ModelAndView results;

	public LoginServer(Request req, Response res){
  	results = loginOn(req,res);
	}

	public static ModelAndView getResults(){
		return results;
	}


  public static ModelAndView index(Request req, Response res){
    Map map = new HashMap();
    map.put("title", "Bienvenido a Preguntado$");
    if(req.session().attribute("username")!=null){
      map.put("userId", (int)req.session().attribute("userId"));
      map.put("username", req.queryParams("username"));
      map.put("play", "<li><a href='/play'>Jugar</a></li>");
      map.put("playonline", "<li><a href='/playonline'>Jugar Online</a></li>");
      map.put("logout","<li><a href='/logout'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
      if(req.session().attribute("admin") != null){
        map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
      }
    }
    return new ModelAndView(map,"./views/index.html");
  }

  public static ModelAndView login(Request req, Response res){
    Map map = new HashMap();
    map.put("title", "Bienvenido a Preguntado$");
    if(req.session().attribute("username")!=null){
      map.put("id", req.session().attribute("userId"));
      map.put("admin", req.session().attribute("admin"));
      map.put("play", "<li><a href='/play'>Jugar</a></li>");
      map.put("playonline", "<li><a href='/playonline'>Jugar Online</a></li>");
      map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
      if((String)req.session().attribute("admin") != null){
        map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
      }
    }
    return new ModelAndView(map, "./views/login.html");
  }

  public static ModelAndView generatec(Request req, Response res){
    Map map = new HashMap();
    map.put("title", "Panel de Administracion");
    if(req.session().attribute("admin") != null) {
      map.put("id", req.session().attribute("userId"));
      map.put("play", "<li><a href='/play'>Jugar</a></li>");
      map.put("playonline", "<li><a href='/playonline'>Jugar Online</a></li>");
      map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
      map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
    }
    else{
      return new ModelAndView(map, "./views/index.html");
    }
    return new ModelAndView(map, "./views/adminPanel/generate_cat.html");
  }

  public static ModelAndView generateq(Request req, Response res){
    Map map = new HashMap();
    map.put("title", "Panel de Administracion");
    if(req.session().attribute("username")!=null) {
      map.put("id", req.session().attribute("userId"));
      map.put("play", "<li><a href='/play'>Jugar</a></li>");
      map.put("playonline", "<li><a href='/playonline'>Jugar Online</a></li>");
      map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
      if(req.session().attribute("admin") != null){
        map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
        return new ModelAndView(map, "./views/adminPanel/generate_quest.html");
      }
      else{
        return new ModelAndView(map, "./views/index.html");
      }
    }
    else{
      return new ModelAndView(map, "./views/index.html");
    }
  }

  public static ModelAndView administrate(Request req, Response res){
      Map map = new HashMap();
      map.put("title", "Panel de Administracion");
      if(req.session().attribute("username")!=null) {
        map.put("id", req.session().attribute("userId"));
        map.put("play", "<li><a href='/play'>Jugar</a></li>");
        map.put("playonline", "<li><a href='/playonline'>Jugar Online</a></li>");
        map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
        if(req.session().attribute("admin") != null){
          map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
        }
      }
      else{
        return new ModelAndView(map, "./views/index.html");
      }
      return new ModelAndView(map, "./views/adminPanel/administrate.html");
  }

	public static ModelAndView loginOn(Request req, Response res){
		Map resLogin = new HashMap();
  	if(req.session().attribute("username")!=null){
  		resLogin.put("id", (Integer)req.session().attribute("userId"));
  		resLogin.put("play", "<li><a href='/play'>Jugar</a></li>");
      resLogin.put("playonline", "<li><a href='/playonline'>Jugar Online</a></li>");
  		resLogin.put("error","<div class='alert alert-danger' id='alert-danger'><strong>Error!</strong> Ya estas conectado como: " + req.session().attribute("username") + ".</div>");
    	return new ModelAndView(resLogin, "./views/login.html");
  	}

  	String password = req.queryParams("password");
    Md5Cipher hashPass = new Md5Cipher(password);
    String passMD5 = hashPass.getHash();
    String namee = req.queryParams("username");

    //Verificamos si existe algun usuario con ese username y esa pass.
    List<User> unico = User.where("username = ? and password = ?", namee, passMD5);
    Boolean result2 = unico.size()==0;
		
		if(!result2){
      req.session(true);
      req.session().attribute("username", namee);
      req.session().attribute("userId",unico.get(0).getId());
      resLogin.put("play", "<li><a href='/play'>Jugar</a></li>");
      resLogin.put("playonline", "<li><a href='/playonline'>Jugar Online</a></li>");
      resLogin.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
      User isAdm = unico.get(0);
      if((Integer)isAdm.get("acces_level") == 5){
        req.session().attribute("admin", true);
        resLogin.put("admin","<li><a href='/administrate'>Administrar</a></li>");
        return new ModelAndView(resLogin,"./views/adminPanel/administrate.html"); 
      }
    }
    else{
      String s = "<div class='alert alert-danger' id='alert-danger'><strong>Error!</strong> Usuario o password incorrectos.</div>";
    	resLogin.put("error",s);
    	return new ModelAndView(resLogin, "./views/login.html");
    }  
    return new ModelAndView(resLogin, "./views/login.html");
	}

  public static ModelAndView logOut(Request req, Response res){
    Map logout = new HashMap();
    logout.put("username", null);
    if(req.session().attribute("username")!=null){
      req.session().removeAttribute("username");
      req.session().removeAttribute("userId");
      req.session().removeAttribute("admin");
      req.session().removeAttribute("play");
      req.session().removeAttribute("admin");
    }
    return new ModelAndView(logout,"./views/index.html"); 
  }

  public static ModelAndView registrar(Request req, Response res){
    Map map = new HashMap();
    map.put("title", "Bienvenido a Preguntado$");
    if(req.session().attribute("username")!=null){
      map.put("play", "<li><a href='/play'>Jugar</a></li>");
      map.put("playonline", "<li><a href='/playonline'>Jugar Online</a></li>");
      map.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
      if(req.session().attribute("admin") != null){
        map.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
      }
    }
    return new ModelAndView(map, "./views/registrar.html");
  }

  public static ModelAndView registrarUser(Request req, Response res){
    User newUser = new User();
    Map result = newUser.registerUser(req,res);
    if((String)result.get("error") != null){
      return new ModelAndView(result,"./views/registrar.html"); 
    }
    if((String)result.get("success") != null){
      return new ModelAndView(result,"./views/registrar.html"); 
    }
    return new ModelAndView(result, "./views/index.html");
  }

  public static ModelAndView sendCategory(Request req, Response res){
    Category cat = new Category();
    Map result = cat.newCategory(req,res);
    if((String)result.get("error") != null){
      return new ModelAndView(result,"./views/adminPanel/generate_cat.html"); 
    }
    if((String)result.get("success") != null){
      return new ModelAndView(result,"./views/adminPanel/generate_cat.html"); 
    }
    return new ModelAndView(result, "./views/index.html");
  }

  public static ModelAndView ranking(Request req, Response res){
    Map rank = new HashMap();
    List<User> top_10 = User.findBySQL("select * from users order by c_questions desc limit 10");
    rank.put("ranking",top_10);
    rank.put("play", "<li><a href='/play'>Jugar</a></li>");
    rank.put("playonline", "<li><a href='/playonline'>Jugar Online</a></li>");
    rank.put("logout","<li><a href='/login'><span class='glyphicon glyphicon-off'></span> Salir</a></li>");
    if(req.session().attribute("admin") != null){
      rank.put("admin", "<li><a href='/administrate'>Administrar</a></li>");
    }
    return new ModelAndView(rank,"./views/ranking.html");
  }

}