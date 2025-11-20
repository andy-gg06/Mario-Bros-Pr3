package tp1.logic;

/**
 * Represents the allowed actions in the game
 *
 */
public enum Action {
	LEFT(-1,0), RIGHT(1,0), DOWN(0,1), UP(0,-1), STOP(0,0), NONE(-1, -1);

	private int x;
	private int y;

	// CONSTRUCTOR -----------------------------------------------------------------------------------
	private Action(int x, int y) {
		this.x=x;
		this.y=y;
	}

	// METHODS ---------------------------------------------------------------------------------------
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
