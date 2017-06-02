package trivia;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import java.util.List;
import java.util.Scanner;
import java.security.MessageDigest;
public class User extends Model {
  static{
    validatePresenceOf("username").message("Por favor, ingrese un usuario");
    validatePresenceOf("password").message("Por favor, ingrese una contraseña");
  }
  //Constructor
  public User(){

  }
  //Metodo que registra un usuario
  public User(String user, String mail, String pass){
    validatePresenceOf("username").message("Por favor, ingrese un usuario");
    validatePresenceOf("password").message("Por favor, ingrese una contraseña");
    set("username", user);
    set("mail", mail);
    set("password", pass);
    set("score",0);
    set("c_questions",0);
    set("i_questions",0);
  }
  public Game createGameForUser(){
    return new Game((Long)this.get("id"));
  }

  /*
  public List<Game> getGame(){
    return this.getAll(Game.class);
  }*/

  //Metodo que utiliza un usuario para responder una pregunta.
  public boolean answerQuestion(Question q){
    q.showQuestion();
    
    if (q.validateA(4)){
      this.set("c_questions", (Integer) this.get("c_questions")+1).saveIt();
      //this.set("score",(Integer) this.get("score")+1).saveIt();
      return true;
    }
    this.set("i_questions", (Integer) this.get("i_questions")+1).saveIt();
    return false;
  }

}
