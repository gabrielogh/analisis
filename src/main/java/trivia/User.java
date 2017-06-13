package trivia;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.validation.UniquenessValidator;
import java.util.List;
import java.util.Scanner;
import java.security.MessageDigest;
public class User extends Model {
  static{
    validatePresenceOf("username").message("Por favor, ingrese un usuario");
    validatePresenceOf("password").message("Por favor, ingrese una contraseña");
    validatePresenceOf("mail").message("Por favor, ingrese un email");
  }
  //Constructor
  public User(){

  }
  //Metodo que registra un usuario
  public User(String user, String mail, String pass){
    set("username", user);
    set("mail", mail);
    set("password", pass);
    set("score",0);
    set("c_questions",0);
    set("i_questions",0);
  }

  public Game createGameForUser(){
    return new Game((Integer)this.get("id"));
  }

  
  public Game getGameInProgress(){
    List<Game> user_games = this.getAll(Game.class);
    int i = 0;
    while ((i< user_games.size())){
      Game g = user_games.get(i);
      if((Boolean)g.get("in_progress")== true){
        return g;
      }
      i++;
    }
    Game g = this.createGameForUser();
    return g; 
  }

  public void answerAQuestion(Question q, int a, Game g){
      if(q.validateA(a)){
        this.set("c_questions", (Integer)this.get("c_questions")+1).saveIt();
        g.set("corrects", (Integer)g.get("corrects")+1).saveIt();
      }
      else{
        this.set("i_questions", (Integer)this.get("i_questions")+1).saveIt();
        g.set("incorrects", (Integer)g.get("incorrects")+1).saveIt();
      }
  }

}
