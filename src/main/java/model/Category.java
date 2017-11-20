package trivia;
import java.util.Random;
import java.util.List;
import org.javalite.activejdbc.Model;
import spark.Request;
import spark.Response;
import spark.QueryParamsMap;
import java.util.HashMap;
import java.util.Map;


public class Category extends Model{
  static{
    validatePresenceOf("name").message("Por favor, ingrese un nombre de Categoria.");
  }
  public Category(){}

  public Category(String name){set("name",name);}

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

  public Map newCategory(Request req, Response res){
    String name_newCat = req.queryParams("name_cat");
    String body = req.body();
    Map result = new HashMap();

    if(name_newCat == null){
      result.put("error","<div class='alert alert-danger'><strong>Error!</strong> Usted es o se hace? Solo debe completar un campo!!!.</div>");
      return result;
    }
    List<Category> unica = Category.where("name = ? ", name_newCat);
    Boolean noExiste = unica.size()==0;
    if(noExiste){
      Category c = new Category(name_newCat);
      c.saveIt();
      result.put("success","<div class='alert alert-success' id='alert-success'><strong>Hecho!</strong> Categoria cargada con exito!</div>");
    }
    else{
      result.put("error","<div class='alert alert-danger' id='alert-danger'><strong>Error!</strong> La categoria ya se encuentra registrada.</div>");
      return result;
    }
    return result;
  }
}