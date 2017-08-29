package trivia;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import java.util.List;

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
    set("win_rate",0);
    set("c_questions",0);
    set("i_questions",0);
    set("acces_level", 0);
  }

  //Metodo que crea un juego para un usuario
  public Game createGameForUser(){
    return new Game((Integer)this.get("id"));
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
  }

}
