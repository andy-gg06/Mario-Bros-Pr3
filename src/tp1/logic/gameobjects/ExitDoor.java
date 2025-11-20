package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class ExitDoor extends GameObject {
	
	private static final String NAME = Messages.EXIT_DOOR_NAME;
	private static final String SHORTCUT = Messages.EXIT_DOOR_SHORTCUT;

	// CONSTRUCTOR -----------------------------------------------------------------------------------
	public ExitDoor(Position pos) {
		super(pos, NAME, SHORTCUT);
		this.isSolid = false;
	}
	
	protected ExitDoor() {
		this(null);
	}

	// METHODS ---------------------------------------------------------------------------------------
	@Override
	public String getIcon() { return Messages.EXIT_DOOR; }
	
	// ExitDoor doesn't interact with any object, only receive
	public boolean interactWith(GameItem other) {
	     boolean canInteract = other.isInPosition(this.pos);
	     if (canInteract)
	          other.receiveInteraction(this);
	     return canInteract;
	}
	
	@Override
	public boolean receiveInteraction(Mario obj) {
		obj.receiveInteraction(this);
		return true;
	}

	@Override
	public void update() {}
	
	// METHODS ADD EXITDOOR --------------------------------------------------------------------------
	protected GameObject copy(GameWorld game, Position pos) { return new ExitDoor(pos); }
}
