package tp1.logic;

public interface GameStatus {
	public static final int DIM_X = 30;
	public static final int DIM_Y = 15;
	
	public String positionToString(int col, int row);
	public int remainingTime();
	public int points();
	public int numLives();
	public boolean playerWins();
	public boolean playerLoses();
}
