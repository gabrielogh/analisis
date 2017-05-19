package trivia;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import java.util.Scanner; 


public class User extends Model {
  static{
    validatePresenceOf("username").message("Por favor, ingrese un usuario");
    validatePresenceOf("password").message("Por favor, ingrese una contraseña");
  }

  //Metodo de registro de usuarios.
	public static void createUser(){
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
		Scanner sc = new Scanner(System.in);	
		User u = new User();
		int pas,pas2;
		System.out.print("\033[H\033[2J");
		System.out.flush();
		System.out.println("Ingrese un nombre de usuario: ");
		String nombre = sc.nextLine(); 
		u.set("username", nombre);
			do{
				System.out.println("Ingrese una contraseña: ");
				pas = sc.nextInt();
				System.out.println("Vuelva a ingresar su contraseña: ");
				pas2 = sc.nextInt();
				if(pas != pas2){
					System.out.println("Error: Las contraseñas no coinciden, vuelva a intentarlo");
				}
			    
			}
			while (pas != pas2);
			u.set("password", pas);
			u.saveIt();
			Base.close();
	}


}
