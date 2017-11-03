package trivia;
import java.util.Random;
import java.util.List;
import org.javalite.activejdbc.Model;


public class Category extends Model{
  static{
    validatePresenceOf("name").message("Por favor, ingrese un nombre de Categoria.");
  }
 	public Category(){

 	}
	public Category(String name){
  	set("name",name);
  }

  public Category randomCat(){
  	List<Category> list = findAll(); 
  	Random r = new Random();
  	return list.get(r.nextInt(list.size()));
  }

  public Question getQuestion(){
  	List<Question> questions = this.getAll(Question.class);
    Random r = new Random();
    Integer i = r.nextInt(questions.size());
    return questions.get(i);
  }
}