package com.ian4d.ai2048.players;

import com.ian4d.ai2048.Board;

public class Pattern1 implements IPlayer {

	
	int count = 0;
	
	@Override
	public void makeMove(Board board) {
		switch (count % 4) {
		case 0:
			board.moveLeft();
			break;
		case 1:
			board.moveUp();
			break;
		case 2:
			board.moveRight();
			break;
		case 3:
			board.moveDown();
			break;
		}
		count++;
	}

}
