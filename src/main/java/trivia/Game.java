package trivia;
import org.javalite.activejdbc.Model;
import java.util.List;

public class Game extends Model{
  static{
    validatePresenceOf("user_id").message("Error");
  }
  //Constructor de la clase sin parametros.
 	public Game(){

 	}

 	//Constructor de la clase con parametros
  public Game(Integer id){
  	set("question_number",0);
  	set("user_id",id);
    set("in_progress",true);
    set("incorrects",0);
    set("corrects",0);
  }
  //Metodo que retorna una categoria Random
  public Category getRandomCat(){
  	return (new Category()).randomCat();
  }
}