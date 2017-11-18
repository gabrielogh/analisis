package trivia;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.QueryParamsMap;
import java.util.HashMap;
import java.util.Map;
import trivia.Md5Cipher;
import org.json.JSONObject;

public class User extends Model {
  static{
    validatePresenceOf("username").message("Por favor, ingrese un usuario");
    validatePresenceOf("password").message("Por favor, ingrese una contraseña");
    validatePresenceOf("mail").message("Por favor, ingrese un email");
  }
  //Constructor
  public User(){}

  public Map registerUser(Request req, Response res){
    String[] result = {req.queryParams("username"),req.queryParams("email"),(String)req.queryParams("password"),(String)req.queryParams("password2")};
    String body = req.body();
    Map questt = new HashMap();

    String p1 = new String(req.queryParams("password"));
    String p2 = new String(req.queryParams("password2"));

    if(!(p1.equals(p2))){
      questt.put("error","<div class='alert alert-danger'><strong>Error!</strong> Las contraseñas no coinciden.</div>");
      return questt;
    }
    List<User> unico = User.where("username = ? or mail = ? ", result[0], result[1]);
    Boolean result2 = unico.size()==0;
    if(result2){
      Md5Cipher hashPass = new Md5Cipher(p1);
      String passMD5 = hashPass.getHash();
      User u = new User(result[0],result[1], passMD5);
      u.saveIt();
      questt.put("id", u.getId());
      questt.put("success","<div class='alert alert-success' id='alert-success'><strong>Hecho!</strong> Usuario registrado con exito!</div>");
    }
    else{
      User u = unico.get(0);
      String e = (String)u.get("mail");
      if(e.equals(result[1])){
      questt.put("error","<div class='alert alert-danger' id='alert-danger'><strong>Error!</strong> Ese E-mail ya se encuentra registrado, pruebe con uno diferente.</div>");
        return questt;
      }
      else{
      questt.put("error","<div class='alert alert-danger' id='alert-danger'><strong>Error!</strong> Ese nombre de usuario ya existe, pruebe con uno diferente.</div>");
        return questt;
      }
    }
    return questt;
  }

  //Metodo que registra un usuario
  public User(String user, String mail, String pass){
    set("username", user);
    set("mail", mail);
    set("password", pass);
    set("win_rate",0);
    set("c_questions",0);
    set("i_questions",0);
    set("acces_level", 0);
  }

  public static User getUserById(Integer id){
      List<User> user_now = User.where("id = ?", id);
      User u = user_now.get(0);
      return u;
  }

  public JSONObject toJson(){
    Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "c4j0i20g");
    JSONObject result = new JSONObject();
    result.put("id", this.getInteger("id"));
    result.put("username", this.getString("username"));
    Base.close();
    return result;
  }

  public Integer getUserId(){
    return (Integer)this.get("id");
  }

  public static User getUserByName(String name){
      List<User> user_now = User.where("username = ?", name);
      User u = user_now.get(0);
      return u;
  }
  //Metodo que crea un juego para un usuario
  public Game createVsGame(){
    Game g = new Game((Integer)this.get("id"));
    g.set("vs_mode", false);
    return g;
  }

 //Metodo que crea un juego VS para un usuario
  public Game createGameForUser(){
    Game g = new Game((Integer)this.get("id"));
    g.set("vs_mode", true);
    return g;
  }
  //Metodo utilizado para el Ranking
  public String username(){
    return this.getString("username");
  }
  
  //Metodo utilizado para el Ranking
  public int c_questions(){
    return this.getInteger("c_questions");
  }

  public Double win_rate(){
    return (Double)this.get("win_rate");
  }

  //Devuelve el primer juego en progreso que tenga el usuario, si no tiene ninguno, crea uno.
  public Game getGameInProgress(boolean mode){
    List<Game> user_games = this.getAll(Game.class);
    int i = 0;
    while ((i< user_games.size())){
      Game g = user_games.get(i);
      if((Boolean)g.get("in_progress")== true && (Boolean)g.get("vs_mode")== mode){
        return g;
      }
      i++;
    }
    Game g = this.createGameForUser();
    return g; 
  }

  //Metodo que le permita a un usuario responder una pregunta.
  public void answerAQuestion(Question q, int a, Game g){
      if(q.validateA(a)){
        this.set("c_questions", (Integer)this.get("c_questions")+1).saveIt();
        g.set("corrects", (Integer)g.get("corrects")+1).saveIt();
      }
      else{
        this.set("i_questions", (Integer)this.get("i_questions")+1).saveIt();
        g.set("incorrects", (Integer)g.get("incorrects")+1).saveIt();
      }
      Double winR = ((((Integer)this.get("c_questions"))* 100) / ((Integer)this.get("c_questions") + (Integer)this.get("i_questions"))) * 1.0;
      this.set("win_rate", winR).saveIt();
      g.set("current_question_state", true).saveIt();
  }
  //Metodo que le permita a un usuario responder una pregunta en un juego online.
  public Boolean answerOnline(Question q, int a, Game g){
      Boolean res = q.validateA(a);
      if(res){
        this.set("c_questions", (Integer)this.get("c_questions")+1).saveIt();
        g.set("corrects", (Integer)g.get("corrects")+1).saveIt();
      }
      else{
        this.set("i_questions", (Integer)this.get("i_questions")+1).saveIt();
        g.set("incorrects", (Integer)g.get("incorrects")+1).saveIt();
      }
      Double winR = ((((Integer)this.get("c_questions"))* 100) / ((Integer)this.get("c_questions") + (Integer)this.get("i_questions"))) * 1.0;
      this.set("win_rate", winR).saveIt();
      g.set("current_question_state", true).saveIt();
      return res;
  }

}
