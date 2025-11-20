package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Box extends GameObject {
	
	private boolean empty;
	
	
	private static final String NAME = Messages.BOX_NAME;
	private static final String SHORTCUT = Messages.BOX_SHORTCUT;

	// CONSTRUCTOR -----------------------------------------------------------------------------------
	public Box(GameWorld game, Position pos) {
		super(game, pos, NAME, SHORTCUT);
		this.isSolid = true;
		this.empty = false;
	}
	
	protected Box() {
		this(null, null);
	}

	// METHODS ---------------------------------------------------------------------------------------
	public boolean isEmpty() { return this.empty; }
	
	@Override
	public String getIcon() {
		if (this.empty)
			return Messages.EMPTY_BOX;
		else
			return Messages.BOX;
	}
	
	@Override
	public boolean interactWith(GameItem other) {
		Position below = new Position(this.pos.get_row() + 1, this.pos.get_col());
		boolean canInteract = other.isInPosition(below);
		if (canInteract) {
		     other.receiveInteraction(this);
		}
		return canInteract;
	}
	
	@Override
	public boolean receiveInteraction(Mario obj) {
		if (this.empty)
			return false;
		
		if (obj.isCrashing())  {// && obj.isCrashing()) {
			game.addPoints(50);
			game.mushroomOut(this.pos.get_col(), this.pos.get_row() - 1);
			this.empty = true;
			obj.mario_crashBox();
		}
		return true;
	}

	@Override
	public void update() {}
	
	// METHODS ADD LAND ------------------------------------------------------------------------------
	protected GameObject copy(GameWorld game, Position pos) { return new Box(game, pos); }
	
	public GameObject parse(String[] objWords, GameWorld game) {
		if (!this.matchObjectName(objWords[1]) || objWords.length > 3)
			return null;
		
		String[] position = objWords[0].trim().substring(1, objWords[0].length() - 1).split(","); // return (row, col)
		
		if (!isNumeric(position[0]) || !isNumeric(position[1]))
			return null;
		
		int row = Integer.parseInt(position[0].trim());
		int col = Integer.parseInt(position[1].trim());
		
		if (!game.rowInLimits(row) || !game.colInLimits(col))
			return null;
		
		Box box = new Box(game, new Position(row, col));
		
		if (objWords.length >= 3) { // empty or not
			String string_empty = objWords[2];
			if (string_empty.equalsIgnoreCase("F") || string_empty.equalsIgnoreCase("FULL"))
                box.empty = false;
            else if (string_empty.equalsIgnoreCase("E") || string_empty.equalsIgnoreCase("EMPTY"))
                box.empty = true;
		}
		return box;
	}
}
