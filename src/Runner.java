import java.util.ArrayList;
import java.util.List;

import com.ian4d.ai2048.Board;
import com.ian4d.ai2048.Game;
import com.ian4d.ai2048.players.AllDown;
import com.ian4d.ai2048.players.AllLeft;
import com.ian4d.ai2048.players.AllRight;
import com.ian4d.ai2048.players.AllUp;
import com.ian4d.ai2048.players.IPlayer;
import com.ian4d.ai2048.players.Pattern1;
import com.ian4d.ai2048.players.RandomPattern;


public class Runner {

	public static void main(String[] args) {
		Board board = Board.createBoard(4, 11);
		
		List<IPlayer> players = new ArrayList<IPlayer>();
		players.add(new RandomPattern());
		players.add(new AllDown());
		players.add(new AllLeft());
		players.add(new AllRight());
		players.add(new AllUp());
		players.add(new Pattern1());
		
		Game game = Game.newGame(4, 11, players.get(0));
		int winCount = 0;
		int loseCount = 0;
		while (winCount + loseCount < 100) {
			while (!game.makeMove()) {
				game.printBoard();
				game.startNextTurn();
			}
			winCount = game.getWinCount();
			loseCount = game.getLoseCount();
			game.restart();
		}
		System.out.println("Win Count: " + winCount);
		System.out.println("Lose Count: " + loseCount);
	}

}
