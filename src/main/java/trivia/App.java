package trivia;
import org.javalite.activejdbc.Base;
import trivia.User;
import java.util.Scanner;


/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args ){
			Scanner sc = new Scanner(System.in);	
			int option = 99;
			int opcionSalida;
		while (option != 5) {
			System.out.print("\033[H\033[2J");
			System.out.flush();
			System.out.println("Welcome to Triviagame. What do you want to do?");
			System.out.println(" 1- Sign up");
			System.out.println(" 2- Play solo(NOT AVAILABLE)");
			System.out.println(" 3- Multiplayer(NEVER AVAILABLE)");
			System.out.println(" 4- Select dificulty (NOT AVAILABLE)");
			System.out.println(" 5- Exit");
			System.out.println("Select an option please");
			
			option = sc.nextInt();
			switch (option) {

				case 1:

					User.createUser();

				case 5:

					break;

			}	

		}
		
	}
}
