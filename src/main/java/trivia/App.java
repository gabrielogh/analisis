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
			System.out.println("Bienvenido a Triviagame. Que desea hacer?:");
			System.out.println("1- Registrarme");
			System.out.println("2- Jugar contra la maquina(NOT AVALIBLE)");
			System.out.println("3- Jugar contra personas(NOT AVALIBLE)");
			System.out.println("4- Modificar dificultad(NOT AVALIBLE)");
			System.out.println("5- Salir.");
			System.out.println("Seleccione el numero de opcion deseada");
			
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
