package trivia;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import java.util.Scanner; 


public class Category extends Model {
  static{
    validatePresenceOf("username").message("Por favor, ingrese un usuario");
    validatePresenceOf("password").message("Por favor, ingrese una contraseña");
  }

	public static void selectCategory(){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		Scanner sc = new Scanner(System.in);	
		User u = new User();
		int pas,pas2;
		System.out.print("\033[H\033[2J");
		System.out.flush();
		Base.close();
	}


}
