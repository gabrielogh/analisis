package trivia;

import trivia.User;
import trivia.Game;
import trivia.Category;
import trivia.Question;
import trivia.Md5Cipher;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.QueryParamsMap;
import java.util.HashMap;
import java.util.Map;

public class LoginServer{
	private Map results;

	public LoginServer(Request req, Response res){
  	results = LoginOn(req,res);
	}

	public Map getResults(){
		return results;
	}

	private Map LoginOn(Request req, Response res){
		Map resLogin = new HashMap();
  	if(req.session().attribute("username")!=null){
  		resLogin.put("id", (Integer)req.session().attribute("userId"));
  		resLogin.put("play", "jugar");
  		resLogin.put("error","Ya estas conectado como: "+ req.session().attribute("username"));
    	return resLogin;
  	}

  	String password = req.queryParams("password");
    Md5Cipher hashPass = new Md5Cipher(password);
    String passMD5 = hashPass.getHash();
    System.out.println("LA CONTRASENA ES: " + passMD5);
    String namee = req.queryParams("username");
  	req.session(true);

    //Verificamos si existe algun usuario con ese username y esa pass.
    List<User> unico = User.where("username = ? and password = ?", namee, passMD5);
    Boolean result2 = unico.size()==0;
		
		if(!result2){
      req.session().attribute("username", namee);
      req.session().attribute("userId",(Integer)unico.get(0).get("id"));
  		resLogin.put("play", "Jugar");
      resLogin.put("logout","Salir");
      User isAdm = unico.get(0);
      if((Integer)isAdm.get("acces_level") == 5){
        req.session().attribute("admin","Adminsitrar");
      }
    }
    else{
    	resLogin.put("error","Usuario o password incorrectos");
    	return resLogin;
    }
    return resLogin;
	}

}