package com.ian4d.ai2048.players;

import com.ian4d.ai2048.Board;

public class AllLeft implements IPlayer {

	@Override
	public void makeMove(Board board) {
		board.moveLeft();
	}

}
