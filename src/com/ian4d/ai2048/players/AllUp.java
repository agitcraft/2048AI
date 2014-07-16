package com.ian4d.ai2048.players;

import com.ian4d.ai2048.Board;

public class AllUp implements IPlayer {

	@Override
	public void makeMove(Board board) {
		board.moveUp();
	}

}
