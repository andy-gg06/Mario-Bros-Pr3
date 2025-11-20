package tp1.logic;

import tp1.logic.gameobjects.*;

public class Game implements GameModel, GameStatus, GameWorld {

	public static final int DIM_X = 30;
	public static final int DIM_Y = 15;

	private Mario mario;
	private int remainingTime;
	private int points;
	private int numLives;
	private int nLevel;
	private boolean wins;
	private boolean isFinished;
	private boolean isReset;

	private GameObjectContainer gameObjects = new GameObjectContainer();

	// CONSTRUCTOR -----------------------------------------------------------------------------------
	public Game(int nLevel) {
		this.remainingTime = 100;
		this.points = 0;
		this.numLives = 3;
		this.nLevel = nLevel;
		this.wins = false;
		this.isFinished = false;
		this.isReset = false;

		switch (this.nLevel) {
		case 0: this.initLevel0();
		break;
		case 1: this.initLevel1();
		break;
		case 2: this.initLevel2();
		break;
		case -1: this.initLevel_createMap();
		break;
		}
	}

	// METHODS ---------------------------------------------------------------------------------------
	public String positionToString(int col, int row) {
		Position pos = new Position(row, col);
		return this.gameObjects.postitionToString(pos);
	}
	
	public boolean isSolidAt(int col, int row) {
		return this.gameObjects.isSolidAt(col, row);
    }

	public int remainingTime() { return this.remainingTime; }

	public int points() { return this.points; }
	
	public void addPoints(int x) { this.points += x; }

	public int numLives() { return this.numLives; }
	
	public int nLevel() { return this.nLevel; }
	
	public boolean playerWins() { return this.wins; }

	public boolean playerLoses() { return numLives() <= 0; }
	
	public boolean isReset() { return this.isReset; }
	
	// Not mandatory but recommended
	public void exit() { this.isFinished = true; }
	
	public boolean isFinished() {
		return playerWins() || playerLoses() || this.isFinished;
	}

	public void reset(int nLevel) {
		this.remainingTime = 100;
		this.isReset = false;
		/*
		 * nLevel = -3: reset actual level (no level inserted)
		 * nLevel != -3: new level inserted -> reset level inserted
		 */
		if (nLevel != -3)
			this.nLevel = nLevel;
		
		switch (this.nLevel) {
		case 0: this.initLevel0();
		break;
		case 1: this.initLevel1();
		break;
		case 2: this.initLevel2();
		break;
		case -1: this.initLevel_createMap();
		break;
		}
	}

	public void update() {
		this.remainingTime--;
		this.gameObjects.update(this);
		
		if(!mario.isAlive() && !this.isReset)
			marioDead();
		
		if (this.isReset) {
	        reset(this.nLevel);
	        this.isReset = false;
		}
	}
	
	public void marioExited() {
		this.points += remainingTime * 10;
		this.wins = true;
		this.remainingTime = 0;
	}
	
	public void marioDead() {
		this.numLives--;
		if (!playerLoses()) {
			this.isReset = true;
			//reset(this.nLevel);
		}
	}
	
	public void addAction(Action dir) {
		this.mario.addAction(dir);
	}
	
	public void mushroomOut(int col, int row) {
		gameObjects.add_new(new MushRoom(this, new Position(row, col)));
	}
	
	@Override
	public void doInteraction(GameItem other) {
		this.gameObjects.doInteraction(other, this);
	}
	
	// METHODS CREATE MOOD ---------------------------------------------------------------------------
	public boolean addObject(String[] objWords) {
		GameObject gameobject = GameObjectFactory.parse(objWords, this);
		if (gameobject != null) {
			this.gameObjects.add(gameobject);
			return true;
		}
		return false;
	}
	public void isMario(Mario obj) {
		this.mario.dead();
		this.mario = obj;
	}
	
	public boolean rowInLimits(int n) {
		return n >= 0 && n < DIM_Y;
	}
	
	public boolean colInLimits(int n) {
		return n >= 0 && n < DIM_X;
	}

	// -----------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		// TODO returns a textual representation of the object
		return "TODO: Hola soy el game";
	}
	
	// LEVEL FUNCTONS --------------------------------------------------------------------------------

	// Level 0
	private void initLevel0() {
		this.nLevel = 0;
		this.remainingTime = 100;

		// 1. Map
		gameObjects = new GameObjectContainer();
		for(int col = 0; col < 15; col++) {
			gameObjects.add(new Land(new Position(13,col)));
			gameObjects.add(new Land(new Position(14,col)));
		}

		gameObjects.add(new Land(new Position(Game.DIM_Y-3,9)));
		gameObjects.add(new Land(new Position(Game.DIM_Y-3,12)));
		for(int col = 17; col < Game.DIM_X; col++) {
			gameObjects.add(new Land(new Position(Game.DIM_Y-2, col)));
			gameObjects.add(new Land(new Position(Game.DIM_Y-1, col)));
		}

		gameObjects.add(new Land(new Position(9,2)));
		gameObjects.add(new Land(new Position(9,5)));
		gameObjects.add(new Land(new Position(9,6)));
		gameObjects.add(new Land(new Position(9,7)));
		gameObjects.add(new Land(new Position(5,6)));

		// Final jump
		int tamX = 8, tamY= 8;
		int posIniX = Game.DIM_X-3-tamX, posIniY = Game.DIM_Y-3;

		for(int col = 0; col < tamX; col++) {
			for (int fila = 0; fila < col+1; fila++) {
				gameObjects.add(new Land(new Position(posIniY- fila, posIniX+ col)));
			}
		}

		gameObjects.add(new ExitDoor(new Position(Game.DIM_Y-3, Game.DIM_X-1)));

		// 3. Characters
		this.mario = new Mario(this, new Position(Game.DIM_Y-3, 0));
		gameObjects.add(this.mario);

		gameObjects.add(new Goombas(this, new Position(0, 19)));
	}

	// Level 1
	private void initLevel1() {
		this.initLevel0();
		this.nLevel = 1;
		gameObjects.add(new Goombas(this, new Position(4, 6)));
		gameObjects.add(new Goombas(this, new Position(12, 6)));
		gameObjects.add(new Goombas(this, new Position(12, 8)));
		gameObjects.add(new Goombas(this, new Position(10, 10)));
		gameObjects.add(new Goombas(this, new Position(12, 11)));
		gameObjects.add(new Goombas(this, new Position(12, 14)));
	}
	
	// Level 2
	private void initLevel2() {
		this.initLevel1();
		this.nLevel = 2;
		gameObjects.add(new Box(this, new Position(9, 4)));
		gameObjects.add(new MushRoom(this, new Position(12, 8)));
		gameObjects.add(new MushRoom(this, new Position(2, 20)));
	}
	
	// Level -1: create a new map
	private void initLevel_createMap() {
		this.nLevel = -1;
		this.remainingTime = 100;
		this.numLives = 3;
		this.points = 0;
		
		gameObjects = new GameObjectContainer();
	}

}
