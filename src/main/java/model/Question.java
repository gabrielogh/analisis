package trivia;
import org.javalite.activejdbc.Model;

public class Question extends Model {
  static{
		validatePresenceOf("description").message("Error: Debe cargar la pregunta");	
		validatePresenceOf("a1").message("Por favor, debe cargar 4 respuestas");
		validatePresenceOf("a2").message("Por favor, debe cargar 4 respuestas");
		validatePresenceOf("a3").message("Por favor, debe cargar 4 respuestas");
		validatePresenceOf("a4").message("Por favor, debe cargar 4 respuestas");
		validatePresenceOf("correct_a").message("Por favor, debe cargar el numero de respuesta correcta");
  }
  /**
  *Constructo de la clase Question
  **/
  public Question(){

  }
  
  public Question(String description,String ans1, String ans2, String ans3, String ans4, int correct){
		validatePresenceOf("description").message("Error: Debe cargar la pregunta");	
		validatePresenceOf("a1").message("Por favor, debe cargar 4 respuestas");
		validatePresenceOf("a2").message("Por favor, debe cargar 4 respuestas");
		validatePresenceOf("a3").message("Por favor, debe cargar 4 respuestas");
		validatePresenceOf("a4").message("Por favor, debe cargar 4 respuestas");
		validatePresenceOf("correct_a").message("Por favor, debe cargar el numero de respuesta correcta");

	  set("description", description);
    set("a1",ans1);
    set("a2",ans2);
    set("a3",ans3);
    set("a4",ans4);
    set("correct_a", correct);
	}

  public  boolean validateA(int n){
    return n == (int)this.get("correct_a");
  }
}