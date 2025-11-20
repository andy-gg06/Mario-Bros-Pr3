package tp1.logic;

import tp1.logic.gameobjects.GameItem;
import tp1.logic.gameobjects.Mario;

public interface GameWorld {
	public String positionToString(int col, int row);
	public boolean isSolidAt(int col, int row);
	public void marioDead();
	public void marioExited();
	public void addPoints(int x);
	public boolean isReset();
	public void doInteraction(GameItem other);
	public void mushroomOut(int col, int row);
	
	public boolean addObject(String[] objWords);
	public void isMario(Mario obj);
	public boolean rowInLimits(int n);
	public boolean colInLimits(int n);
}