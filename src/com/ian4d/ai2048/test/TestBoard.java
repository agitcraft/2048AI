/**
 * 
 */
package com.ian4d.ai2048.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ian4d.ai2048.Board;

/**
 * @author zerominusminus
 *
 */
public class TestBoard {
	
	private int small_dim = 2;
	private int standard_dim = 4;
	private int large_dim = 16;
	
	private int small_power = 5;
	private int standard_power = 11;
	private int large_power = 30;
	
	Board board = Board.createBoard(standard_dim, standard_power);

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		board.empty();
	}

	
	@Test
	public void initBoard() {
		// confirm that all but 1 tile is empty
		board.init();
		int[][] tiles = board.getTiles();
		int dim = board.getDim();
		int emptyTiles = 0;
		int filledTiles = 0;
		assertEquals(err("Dimension not correctly set"), standard_dim, dim);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (tiles[i][j] == 0) emptyTiles++;
				else if (tiles[i][j] != 0) filledTiles++;
			}
		}
		assertEquals(err("Board not correctly initialized"), dim*dim - 1, emptyTiles);
		assertEquals(err("Board not correctly initialized"), 1, filledTiles);
	}
	
	
	@Test
	public void moveBoardLeft() {
		int dim = standard_dim;
		
		board.empty();
		board.setTileValue(0, dim-1, 2);
		board.moveLeft();
		
		assertEquals(err("Failed to move tile left"), 2, board.getTileValue(0, 0));
		assertEquals(err("Failed to move tile left"), 0, board.getTileValue(0, dim-1));
		
		board.setTileValue(0, 1, 2);
		board.moveLeft();
		
		assertEquals(err("Failed to merge tiles left"), 4, board.getTileValue(0, 0));
		
		board.setTileValue(0, 1, 4);
		board.setTileValue(0, 2, 4);
		board.setTileValue(0, 3, 4);
		
		board.moveLeft();
		
		assertEquals(err("Failed to merge tiles left"), 8, board.getTileValue(0, 0));
		assertEquals(err("Failed to merge tiles left"), 8, board.getTileValue(0, 1));
		assertEquals(err("Failed to merge tiles left"), 0, board.getTileValue(0, 2));
		assertEquals(err("Failed to merge tiles left"), 0, board.getTileValue(0, 3));
		
	}

	@Test
	public void moveBoardRight() {
		int dim = standard_dim;
		int power = standard_power;
		
		board.empty();
		board.setTileValue(0, 0, 2);
		board.moveRight();
		
		assertEquals(err("Failed to move tile right"), 0, board.getTileValue(0, 0));
		assertEquals(err("Failed to move tile right"), 2, board.getTileValue(0, dim-1));
		
		board.setTileValue(0, dim-2, 2);
		board.moveRight();
		
		assertEquals(err("Failed to merge tiles right"), 4, board.getTileValue(0, dim-1));
		
		board.setTileValue(0, dim-2, 4);
		board.setTileValue(0, dim-3, 4);
		board.setTileValue(0, dim-4, 4);
		
		board.moveRight();
		
		assertEquals(err("Failed to merge tiles left"), 8, board.getTileValue(0, dim-1));
		assertEquals(err("Failed to merge tiles left"), 8, board.getTileValue(0, dim-2));
		assertEquals(err("Failed to merge tiles left"), 0, board.getTileValue(0, dim-3));
		assertEquals(err("Failed to merge tiles left"), 0, board.getTileValue(0, dim-4));
		
	}
	
	@Test
	public void moveBoardUp() {
		int dim = standard_dim;
		int power = standard_power;
		
		board.empty();
		board.setTileValue(dim - 1, 0, 2);
		board.moveUp();
		
		assertEquals(err("Failed to move tile up"), 2, board.getTileValue(0, 0));
		assertEquals(err("Failed to move tile up"), 0, board.getTileValue(dim - 1, 0));
		
		board.setTileValue(dim - 1, 0, 2);
		board.moveUp();
		
		assertEquals(err("Failed to merge tiles up"), 4, board.getTileValue(0, 0));
		
		board.setTileValue(1, 0, 4);
		board.setTileValue(2, 0, 4);
		board.setTileValue(3, 0, 4);
		
		board.moveUp();
		
		assertEquals(err("Failed to merge tiles up"), 8, board.getTileValue(0, 0));
		assertEquals(err("Failed to merge tiles up"), 8, board.getTileValue(1, 0));
		assertEquals(err("Failed to merge tiles up"), 0, board.getTileValue(2, 0));
		assertEquals(err("Failed to merge tiles up"), 0, board.getTileValue(3, 0));
	}
	
	@Test
	public void moveBoardDown() {
		
		int dim = standard_dim;
		int power = standard_power;
		
		board.empty();
		board.setTileValue(0, 0, 2);
		board.moveDown();
		
		assertEquals(err("Failed to move tile down"), 2, board.getTileValue(dim - 1, 0));
		assertEquals(err("Failed to move tile down"), 0, board.getTileValue(0, 0));
		
		board.setTileValue(0, 0, 2);
		board.moveDown();
		
		assertEquals(err("Failed to merge tiles down"), 4, board.getTileValue(dim - 1, 0));
		
		board.setTileValue(dim-2, 0, 4);
		board.setTileValue(dim-3, 0, 4);
		board.setTileValue(dim-4, 0, 4);
		
		board.moveDown();
		
		assertEquals(err("Failed to merge tiles down"), 8, board.getTileValue(dim - 1, 0));
		assertEquals(err("Failed to merge tiles down"), 8, board.getTileValue(dim - 2, 0));
		assertEquals(err("Failed to merge tiles down"), 0, board.getTileValue(dim - 3, 0));
		assertEquals(err("Failed to merge tiles down"), 0, board.getTileValue(dim - 4, 0));
	}
	
	@Test
	public void emptyBoard() {
		int dim = standard_dim;
		board.empty();
		for (int i = 0; i < board.getDim(); i++) {
			for (int j = 0; j < board.getDim(); j++) {
				assertEquals(err("Failed to empty board"), 0, board.getTileValue(i, j));
			}
		}
	}
	
	@Test
	public void moveTile() {
		int dim = standard_dim;
		int tl = (int)Math.floor(dim/2);
		
		board.empty();
		board.setTileValue(tl, tl, 2);
		
		board.moveTileRight(tl, tl);
		assertEquals(err("Failed to move tile right"), 2, board.getTileValue(tl, tl+1));
		assertEquals(err("Failed to move tile right"), 0, board.getTileValue(tl, tl));
		
		board.moveTileDown(tl, tl+1);
		assertEquals(err("Failed to move tile down"), 2, board.getTileValue(tl+1, tl+1));
		assertEquals(err("Failed to move tile down"), 0, board.getTileValue(tl, tl+1));
		
		board.moveTileLeft(tl+1, tl+1);
		assertEquals(err("Failed to move tile left"), 2, board.getTileValue(tl+1, tl));
		assertEquals(err("Failed to move tile left"), 0, board.getTileValue(tl+1, tl+1));
		
		board.moveTileUp(tl+1, tl);
		assertEquals(err("Failed to move tile up"), 2, board.getTileValue(tl, tl));
		assertEquals(err("Failed to move tile up"), 0, board.getTileValue(tl+1, tl));
	}
	
	@Test
	public void setEmptyTile() {
		board.empty();
		board.addEmptyTile(0, 0);
		assertTrue(err("Failed to add empty tile"), board.getEmptyTiles().contains("0,0"));
		board.removeEmptyTile(0, 0);
		assertFalse(err("Failed to remove empty tile"), board.getEmptyTiles().contains("0,0"));
	}
	
	@Test
	public void tilesCanMerge() {
		board.empty();
		board.setTileValue(0, 0, 2);
		board.setTileValue(0, 1, 2);
		assertTrue(err("Failed to approve tile merge"), board.canTilesMerge(0, 0, 0, 1));
		assertFalse(err("Failed to reject tile merge"), board.canTilesMerge(0, 1, 0, 2));
	}
	
	@Test
	public void canMove() {
		board.empty();
		board.setTileValue(0, 0, 2);
		
		assertTrue(err("Failed to acknowledge moves"), board.hasLegalMoves());
		assertTrue(err("Failed to detect fair move"), board.canMoveRight());
		assertTrue(err("Failed to detect fair move"), board.canMoveDown());
		assertFalse(err("Failed to reject bad move"), board.canMoveUp());
		assertFalse(err("Failed to reject bad move"), board.canMoveLeft());
		
		board.moveRight();
		board.moveDown();

		assertTrue(err("Failed to acknowledge moves"), board.hasLegalMoves());
		assertTrue(err("Failed to detect fair move"), board.canMoveUp());
		assertTrue(err("Failed to detect fair move"), board.canMoveLeft());
		assertFalse(err("Failed to reject bad move"), board.canMoveRight());
		assertFalse(err("Failed to reject bad move"), board.canMoveDown());
		
		int dim = board.getDim();
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				board.setTileValue(i, j, i*dim + j + 1);
			}
		}
		assertFalse(err("Failed to recognize lack of moves"), board.hasLegalMoves());
		
	}
	
	@Test
	public void getEmptyTiles() {
		// confirm all tiles are empty
	}
	
	private String err(String str) {
		return str + "\n" + board.toString();
	}
	
	

}
