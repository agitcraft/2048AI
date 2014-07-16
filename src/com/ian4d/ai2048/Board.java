package com.ian4d.ai2048;

import java.util.ArrayList;
import java.util.List;

public class Board {

	public static final int DEFAULT_DIM = 4;
	public static final int MAX_POWER = 11;
	private static final int BASE = 2;
	
	/**
	 * Creates a new board
	 * @param dim The size of each side of the board
	 * @param maxPower The maximum power of 2 which the user is trying to reach. 
	 * @return
	 */
	public static Board createBoard(int dim, int maxPower) {
		Board board = new Board();
		board.dim = dim;
		board.maxPower = maxPower;
		board.init();
		return board;
	}
	
	private int[][] tiles;
	private int dim = DEFAULT_DIM;
	private int maxPower = MAX_POWER;
	private boolean gameOver = false;
	private boolean canMoveLeft = false;
	private boolean canMoveRight = false;
	private boolean canMoveUp = false;
	private boolean canMoveDown = false;
	
	// String representaions of empty tiles
	private List<String> emptyTiles = new ArrayList<String>();
	
	/**
	 * Initializes the state of the game
	 */
	public void init() {
		gameOver = false;
		empty();
		
		// Fill a random tile and update legal move set
		hasLegalMoves();
	}
	
	/**
	 * Empties the board
	 */
	public void empty() {
		emptyTiles.clear();
		tiles = new int[dim][];
		// populate the board with empties
		for (int i = 0; i < dim; i++) {
			tiles[i] = new int[dim];
			for (int j = 0; j < dim; j++) {
				tiles[i][j] = 0;
				emptyTiles.add(i + "," + j);
			}
		}
	}
	
	/**
	 * Moves the tiles on board left
	 * return true if the board changed
	 */
	public boolean moveLeft() {
		
		// Block invalid moves
		if (!canMoveLeft)
			return false;
		
		boolean result = false;
		for (int i = 0; i < dim; i++) {
			for (int j = 1; j < dim; j++) {		// We can skip the left most column
				
				// Skip empty tiles
				if (isTileEmpty(i, j))
					continue;
				
				// Stepping indices
				int k = j;
				
				// Slide this value as far left as it will go
				while (isValidLocation(i, k-1) && isTileEmpty(i, k - 1) && moveTileLeft(i, k)) {
					k--;
					result = true;
				}
				
				// If the tile came to rest to the right of a tile with the 
				// same value, nullify it
				if (canTilesMerge(i, k, i, k - 1)) {
					mergeLeft(i, k);
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * Moves the tiles on board right
	 * return true if the board changed
	 */
	public boolean moveRight() {
		
		// Block invalid moves
		if (!canMoveRight)
			return false;
		
		boolean result = false;
		for (int i = 0; i < dim; i++) {
			for (int j = dim-2; j >= 0; j--) {		// We can skip the right most column
				
				// Skip empty tiles
				if (isTileEmpty(i, j))
					continue;
				
				// Stepping indices
				int k = j;
				
				// Slide this value as far right as it will go
				while (isValidLocation(i, k + 1) && isTileEmpty(i, k + 1) && moveTileRight(i, k)) {
					k++;
					result = true;
				}
				
				// If the tile came to rest to the left of a tile with the 
				// same value, nullify it
				if (canTilesMerge(i, k, i, k + 1)) {
					mergeRight(i, k);
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * Moves the tiles on board up
	 * return true if the board changed
	 */
	public boolean moveUp() {
		
		// Block invalid moves
		if (!canMoveUp)
			return false;
		
		boolean result = false;
		for (int j = 0; j < dim; j++) {
			for (int i = 1; i < dim; i++) {	// Can skip the top most row
				
				// Skip empty tiles
				if (isTileEmpty(i, j))
					continue;
				
				// Stepping indices
				int k = i;
				
				// Slide this value as far up as it will go
				while (isValidLocation(k - 1, j) && isTileEmpty(k - 1, j) && moveTileUp(k, j)) {
					k--;
					result = true;
				}
				
				// If the tile came to rest below a tile with the 
				// same value, nullify it
				if (canTilesMerge(k, j, k - 1, j)) {
					mergeUp(k, j);
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * Moves the tiles on the board down
	 * @return true if the board changed
	 */
	public boolean moveDown() {
		
		// Block invalid moves
		if (!canMoveDown)
			return false;
		
		boolean result = false;
		for (int j = 0; j < dim; j++) {
			for (int i = dim-2; i >= 0; i--) {	// Can skip the bottom most row
				
				// Skip empty tiles
				if (isTileEmpty(i, j))
					continue;
				
				// Stepping indices
				int k = i;
				
				// Slide this value as far down as it will go
				while (isValidLocation(k + 1, j) && isTileEmpty(k + 1, j) && moveTileDown(k, j)) {
					k++;
					result = true;
				}
				
				// If the tile came to rest above a tile with the 
				// same value, nullify it
				if (canTilesMerge(k, j, k + 1, j)) {
					mergeDown(k, j);
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * Populates a random tile with a 2 or a 4
	 */
	public boolean fillRandomTile(int value) {
		String tileSet = emptyTiles.get((int)Math.floor(Math.random()*emptyTiles.size()));
		if (!tileSet.equals(null) && tileSet.length() > 0) {
			String[] tileBreak = tileSet.split(",");
			int row = Integer.valueOf(tileBreak[0]);
			int col = Integer.valueOf(tileBreak[1]);
			removeEmptyTile(row, col);
			setTileValue(row, col, value);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks for any remaining legal moves on the board
	 * @return
	 */
	public boolean hasLegalMoves() {
		
		canMoveRight = false;
		canMoveLeft = false;
		canMoveUp = false;
		canMoveDown = false;

		// Scan left to right
		int i, j;
		for (i = 0; i < dim; i++) {
			for (j = 1; j < dim-1; j++) {
				int tile = getTileValue(i, j);
				int left = getTileValue(i, j - 1);
				int right = getTileValue(i, j + 1);
				if ((left != 0 && tile == 0) || (tile != 0 && right == 0)) {
					canMoveRight = true;
				}
				if ((right != 0 && tile == 0) || (tile != 0 && left == 0)) {
					canMoveLeft = true;
				}
				
				if (tile != 0 && (left == tile || right == tile)) {
					canMoveLeft = true;
					canMoveRight = true;
				}
			}
		}
		
		for (j = 0; j < dim; j++) {
			for (i = 1; i < dim - 1; i++) {
				int tile = getTileValue(i, j);
				int up = getTileValue(i - 1, j);
				int down = getTileValue(i + 1, j);
				if ((up != 0 && tile == 0) || (tile != 0 && down == 0)) {
					canMoveDown = true;
				}
				if ((down != 0 && tile == 0) || (tile != 0 && up == 0)) {
					canMoveUp = true;
				}
				if (tile != 0 && (up == tile || down == tile)) {
					canMoveUp = true;
					canMoveDown = true;
				}
				
			}
		}
		
		return canMoveRight || canMoveLeft || canMoveUp || canMoveDown;
	}
	
	/**
	 * Checks the contents of a tile for emptiness
	 * @param row The row being checked
	 * @param col The column being checked
	 * @return true if the tile is empty
	 */
	public boolean isTileEmpty(int row, int col) {
		return tiles[row][col] == 0;
	}
	
	/**
	 * Adds a tile to the empty tile set
	 * @param row The row being added
	 * @param col The column being added
	 * @return true if successful
	 */
	public boolean addEmptyTile(int row, int col) {
		return emptyTiles.add(row + "," + col);
	}
	
	/**
	 * Removes a tile from the empty tile set
	 * @param row The row being removed
	 * @param col The column being removed
	 * @return true if successful
	 */
	public boolean removeEmptyTile(int row, int col) {
		return emptyTiles.remove(row + "," + col);
	}
	
	/**
	 * Sets the tile at row, col to value
	 * @param row The row to set
	 * @param col The column to set
	 * @param value The value to set at that location
	 * @return true if successful
	 */
	public boolean setTileValue(int row, int col, int value) {
		if (isValidLocation(row, col)) {
			tiles[row][col] = value;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the value stored at row, col
	 * @param row The row to get
	 * @param col The column to get
	 * @return The value stored at row, col
	 */
	public int getTileValue(int row, int col) {
		if (isValidLocation(row, col)) {
			return tiles[row][col];
		} else {
			return -1;
		}
	}
	
	/**
	 * Checks the validity of a row and column index
	 * @param row The row position
	 * @param col The column position
	 * @return true if the row and column indices are within bounds
	 */
	public boolean isValidLocation(int row, int col) {
		return (row >=0 && row < dim && col >= 0 && col < dim);
	}
	
	/**
	 * Moves the tile at row and column up one spot
	 * @param row The row position
	 * @param col The column position
	 * @return true if the move was successful
	 */
	public boolean moveTileUp(int row, int col) {
		if (isValidLocation(row, col) && isValidLocation(row-1, col)) {
			tiles[row-1][col] = tiles[row][col];
			tiles[row][col] = 0;
			addEmptyTile(row, col);
			removeEmptyTile(row - 1, col);
			return true;
		}
		return false;
	}
	
	/**
	 * Moves the tile at row and column down one spot
	 * @param row The row position
	 * @param col The column position
	 * @return true if the move was successful
	 */
	public boolean moveTileDown(int row, int col) {
		if (isValidLocation(row, col) && isValidLocation(row+1, col)) {
			tiles[row+1][col] = tiles[row][col];
			tiles[row][col] = 0;
			addEmptyTile(row, col);
			removeEmptyTile(row + 1, col);
			return true;
		}
		return false;
	}
	
	/**
	 * Moves the tile at row and column left one spot
	 * @param row The row position
	 * @param col The column position
	 * @return true if the move was successful
	 */
	public boolean moveTileLeft(int row, int col) {
		if (isValidLocation(row, col) && isValidLocation(row, col-1)) {
			tiles[row][col-1] = tiles[row][col];
			tiles[row][col] = 0;
			addEmptyTile(row, col);
			removeEmptyTile(row, (col-1));
			return true;
		}
		return false;
	}
	
	/**
	 * Moves the tile at row and column right one spot
	 * @param row The row position
	 * @param col The column position
	 * @return true if the move was successful
	 */
	public boolean moveTileRight(int row, int col) {
		if (isValidLocation(row, col) && isValidLocation(row, col+1)) {
			tiles[row][col+1] = tiles[row][col];
			tiles[row][col] = 0;
			addEmptyTile(row, col);
			removeEmptyTile(row, col + 1);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether two tiles can be merged into one
	 * @param row1
	 * @param col1
	 * @param row2
	 * @param col2
	 * @return
	 */
	public boolean canTilesMerge(int row1, int col1, int row2, int col2) {
		return isValidLocation(row1, col1) && isValidLocation(row2, col2) && (tiles[row1][col1] == tiles[row2][col2]);
	}
	
	/**
	 * Merges the tile at row, col into the tile to the left of it
	 * @param row
	 * @param col
	 * @return true if successful
	 */
	public boolean mergeLeft(int row, int col) {
		try {
			tiles[row][col-1] *= BASE;
			tiles[row][col] = 0;
			addEmptyTile(row, col);
			removeEmptyTile(row, col-1);
			return true;
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Merges the tile at row, col into the tile ot the right of it
	 * @param row
	 * @param col
	 * @return true if successful
	 */
	public boolean mergeRight(int row, int col) {
		try {
			tiles[row][col+1] *= BASE;
			tiles[row][col] = 0;
			addEmptyTile(row, col);
			removeEmptyTile(row, col+1);
			return true;
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Merges the tile at row, col into the tile above it
	 * @param row
	 * @param col
	 * @return true if successful
	 */
	public boolean mergeUp(int row, int col) {
		try {
			tiles[row-1][col] *= BASE;
			tiles[row][col] = 0;
			addEmptyTile(row, col);
			removeEmptyTile(row-1, col);
			return true;
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Merges the tile at row, col into the tile below it
	 * @param row
	 * @param col
	 * @return true if successful
	 */
	public boolean mergeDown(int row, int col) {
		try {
			tiles[row+1][col] *= BASE;
			tiles[row][col] = 0;
			addEmptyTile(row, col);
			removeEmptyTile(row+1, col);
			return true;
		} catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * @return the canMoveLeft
	 */
	public boolean canMoveLeft() {
		return canMoveLeft;
	}

	/**
	 * @return the canMoveRight
	 */
	public boolean canMoveRight() {
		return canMoveRight;
	}

	/**
	 * @return the canMoveUp
	 */
	public boolean canMoveUp() {
		return canMoveUp;
	}

	/**
	 * @return the canMoveDown
	 */
	public boolean canMoveDown() {
		return canMoveDown;
	}

	/**
	 * @return the tiles
	 */
	public int[][] getTiles() {
		return tiles;
	}

	/**
	 * @param tiles the tiles to set
	 */
	public void setTiles(int[][] tiles) {
		this.tiles = tiles;
	}

	/**
	 * @return the dim
	 */
	public int getDim() {
		return dim;
	}

	/**
	 * @return the maxPower
	 */
	public int getMaxPower() {
		return maxPower;
	}

	/**
	 * @return the emptyTiles
	 */
	public List<String> getEmptyTiles() {
		return emptyTiles;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				sb.append(tiles[i][j]);
				sb.append("\t");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
