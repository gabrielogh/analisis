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

  //Metodo que nos permite jugar una partida
  public void playGame(){
    User user = this.parent(User.class);
    if(!(this.get("in_progress").equals(false))){
      boolean respuesta_correcta = true;
      while((Integer)this.get("question_number")<5){ 
        Category c = getRandomCat();
        Question q = c.getQuestion();
        respuesta_correcta = user.answerQuestion(q);
        if(respuesta_correcta){
          user.set("c_question", (Integer)user.get("c_question")+1);
        }
        else{
          user.set("i_question", (Integer)user.get("i_question")+1);
        }
        this.set("question_number",(Integer)this.get("question_number")+1).saveIt();
      }
      if((Integer)this.get("question_number")==5){
        this.set("in_progress",false).saveIt();
      }
    }

  }
}