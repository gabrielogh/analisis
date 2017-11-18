package trivia;
import java.util.List;
import org.javalite.activejdbc.Model;

public class Game extends Model{
  static{
    validatePresenceOf("user_id").message("Error");
  }
  //Constructor de la clase sin parametros.
 	public Game(){

 	}

 	//Constructor de la clase con parametros
  public Game(Integer id_user){
  	set("question_number",0);
  	set("user_id",id_user);
    set("in_progress",true);
    set("incorrects",0);
    set("corrects",0);
    Category cat = (new Category()).randomCat();
    Question que = cat.getQuestion();
    set("current_question_id", (Integer)que.get("id"));
    set("current_question_state", false);
  }

  public Question getCurrentQuestion(){
    List<Question> question_now = Question.where("id = ?", (Integer)this.get("current_question_id"));
    Question q = question_now.get(0);
    return q;
  }

  public Integer getGameId(){
    return this.getInteger("id");
  }

  public Category getCurrentCategory(){
    List<Question> question_now = Question.where("id = ?", (Integer)this.get("current_question_id"));
    Question q = question_now.get(0);
    List<Category> questions = Category.where("id = ?", (Integer)q.get("category_id"));
    Category cat = questions.get(0);
    return cat;
  }
  //Metodo que retorna una categoria Random
  public Category getRandomCat(){
  	return (new Category()).randomCat();
  }
}