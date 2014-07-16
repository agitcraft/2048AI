package com.ian4d.ai2048.players;

import com.ian4d.ai2048.Board;

public class RandomPattern implements IPlayer {

	@Override
	public void makeMove(Board board) {
		int rand = (int)Math.floor(Math.random()*3);
		switch (rand) {
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
	}

}
