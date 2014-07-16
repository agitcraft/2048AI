package com.ian4d.ai2048;

import com.ian4d.ai2048.players.IPlayer;

public class Game {

	public static Game newGame(int dimension, int maxScore, IPlayer player) {
		Game game = new Game();
		game.board = Board.createBoard(dimension, maxScore);
		game.player = player;
		game.boardDimension = dimension;
		game.boardMaxScore = maxScore;
		return game;
	}

	private Board board;
	private IPlayer player;
	private int winCount = 0;
	private int loseCount = 0;
	private int boardDimension;
	private int boardMaxScore;
	
	/**
	 * Returns true if the game is over
	 * @return
	 */
	public boolean makeMove() {
		player.makeMove(board);
		if (!checkFailure()) {
			if (checkVictory()) {
				System.out.println("GAME OVER: YOU WIN");
				winCount++;
				return true;
			} else {
				System.out.println("GAME OVER: YOU LOSE");
				loseCount++;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Starts the next turn by adding a new randomly filled tile to the board
	 * @return
	 */
	public boolean startNextTurn() {
		board.fillRandomTile(Math.random() < .9 ? 2 : 4);
		return true;
	}
	
	/**
	 * Determines whether the player has won the game
	 * @return
	 */
	public boolean checkVictory() {
		for (int i = 0; i < boardDimension; i++) {
			for (int j = 0; j < boardDimension; j++) {
				if (board.getTileValue(i, j) == Math.pow(2, boardMaxScore));
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether or not the game has ended
	 * @return
	 */
	public boolean checkFailure() {
		return (board.getEmptyTiles().size() == 0 && !board.hasLegalMoves());
	}
	
	/**
	 * Restarts the game
	 */
	public void restart() {
		board = Board.createBoard(boardDimension, boardMaxScore);
	}
	
	/**
	 * Prints the current game board to standard output
	 */
	public void printBoard() {
		System.out.println(board.toString());
	}

	/**
	 * @return the winCount
	 */
	public int getWinCount() {
		return winCount;
	}

	/**
	 * @return the loseCount
	 */
	public int getLoseCount() {
		return loseCount;
	}
	
}
