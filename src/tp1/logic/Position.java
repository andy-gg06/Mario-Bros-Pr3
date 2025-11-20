package tp1.logic;

/*
 * TODO: Immutable class to encapsulate and manipulate positions in the game board
 */
public class Position {

	private int col;
	private int row;

	// CONSTRUTOR -----------------------------------------------------------------------------------
	public Position(int row, int col) {
		this.col = col;
		this.row = row;
	}

	// METHODS --------------------------------------------------------------------------------------
	public boolean equals(Position other) {
		return this.col == other.col && this.row == other.row;
	}
	
	public int get_col() { return this.col; }
	
	public int get_row() { return this.row; }
	
	public void moveLeft() { this.col--; }
	
	public void moveRight() { this.col++; }
	
	public void moveUp() { this.row--; }
	
	public void moveDown() { this.row++; }
}
