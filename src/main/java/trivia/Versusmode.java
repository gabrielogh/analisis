package trivia;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.Base;
import trivia.User;
import trivia.Question;
import trivia.Game;
import trivia.Category;
import trivia.PlayGame;
import trivia.LoginServer;

/**
 * This class represents a multiplayer game.
 * when two players try to start a game this class creates a multiplayer game
 * creating a single game for each user but allowing to answer a question one by one (waiting his turn).
 * @param. Users id.
 * @pre. Users logged.
 * @post. New VersusMode created on the DB.
 */

public class Versusmode extends Model{
	private User p1,p2;
	private Game g1,g2;
	private int questionNumber = 1;

	public Versusmode(User user1, User user2){
		p1 = user1;
		p2 = user2;
    g1 = p1.createGameForUser();
    g2 = p2.createGameForUser();
    g1.saveIt();
    g2.saveIt();
    set("game_p1_id", Integer.valueOf(((Long)g1.get("id")).intValue()));
    set("game_p2_id", Integer.valueOf(((Long)g2.get("id")).intValue()));
    set("turn", 1);
    set("in_progress",true);
	}

	/**
	 * This method returns the winner number of the game.
	 * if player1 wins, return 1, if player2 wins, return 2, if its a draw return 0
	 * if the game isn't finished, return -1
	 * @param.
	 * @pre. 
	 * @post. Number of winner (1-2) or -1 for error.
	 */
	public int getWinner(){
		if((Boolean)this.get("in_progress") == false){
			if((Integer)g1.get("corrects") > (Integer)g2.get("corrects")){
				return 1;
			}
			else if((Integer)g1.get("corrects") < (Integer)g2.get("corrects")){
				return 2;
			}
			else{
				return 0;
			}
		}
		return -1;
	}

}