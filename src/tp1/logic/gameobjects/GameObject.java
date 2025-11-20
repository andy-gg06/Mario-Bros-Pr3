package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;

public abstract class GameObject implements GameItem { // TODO

	protected Position pos; // If you can, make it private.
	private boolean isAlive;
	protected GameWorld game;
	protected boolean isSolid;
	
	private String name;
	private String shorcut;

	// CONSTRUCTOR -----------------------------------------------------------------------------------
	public GameObject(GameWorld game, Position pos, String name, String shorcut) {
		this(pos, name, shorcut);
		this.game = game;
	}

	public GameObject(Position pos, String name, String shorcut) {
		this.pos = pos;
		this.isAlive = true;
		this.name = name;
		this.shorcut = shorcut;
	}
	
	// METHODS ---------------------------------------------------------------------------------------
	
	public boolean isSolid() { return this.isSolid; }
	
	public boolean isAlive() { return this.isAlive; }
	
	protected String getName() { return name; }
	protected String getShortcut() { return shorcut; }
	
	protected boolean matchObjectName(String name) {
		return getShortcut().equalsIgnoreCase(name) ||
			   getName().equalsIgnoreCase(name);
	}
	
	@Override
	public boolean isInPosition(Position p) { return this.pos.equals(p); }

	public void dead(){ this.isAlive = false; }
	
	public abstract String getIcon();
	
	public abstract void update();
	
	// Receive the interaction from other object
	public boolean receiveInteraction(Land obj) { return false; } // No object interact with land
	
	// ExitDoor <-> Mario (bidirectional and same function)
	public boolean receiveInteraction(ExitDoor obj) { return false; }
	
	public boolean receiveInteraction(Mario obj) { return false; }
	
	// Goombas <-> Mario (bidirectional but different)
	public boolean receiveInteraction(Goombas obj) { return false; }
	
	public boolean receiveInteraction(MushRoom obj) { return false; }
	
	public boolean receiveInteraction(Box obj) { return false; }
	
	// METHODS CREATE MOOD -----------------------------------------------------------------------------
	protected abstract GameObject copy(GameWorld game, Position pos);
	
	public GameObject parse(String[] objWords, GameWorld game) { // useful for simple objects like Land and ExitDoor
		if (!this.matchObjectName(objWords[1]) || objWords.length > 2)
			return null;
		
		String[] position = objWords[0].trim().substring(1, objWords[0].length() - 1).split(","); // return (row, col)
		
		if (!isNumeric(position[0]) || !isNumeric(position[1]))
			return null;
		
		int row = Integer.parseInt(position[0].trim());
		int col = Integer.parseInt(position[1].trim());
		
		if (!game.rowInLimits(row) || !game.colInLimits(col))
			return null;
		
		GameObject o = this.copy(game, new Position(row, col));
		
		return o;
	}
	
	protected boolean isNumeric(String s) {
		return s.matches("\\d+");
	}
}
