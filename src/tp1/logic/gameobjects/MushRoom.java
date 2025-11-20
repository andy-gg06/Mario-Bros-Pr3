package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class MushRoom extends MovingObject  {
	private static final String NAME = Messages.MUSHROOM_NAME;
	private static final String SHORTCUT = Messages.MUSHROOM_SHORTCUT;

	// CONSTRUTOR -----------------------------------------------------------------------------------
	public MushRoom(GameWorld game, Position pos) {
		super(game, pos, NAME, SHORTCUT);
		this.isSolid = false;
		this.dir = Action.RIGHT;
	}
	
	protected MushRoom() {
		this(null, null);
	}

	// METHODS --------------------------------------------------------------------------------------
	@Override
	public String getIcon() {  return Messages.MUSHROOM; }
	
	
	@Override
	public void update() {
		this.automaticMovement();
	}

	@Override
	// MushRoom receive interaction from Mario: mushroom disappear -> bigger mario or not
	public boolean receiveInteraction(Mario obj) {
		if (!this.isAlive() || !obj.isAlive())
			return false;
		obj.receiveInteraction(this);
		return true;
	}
	
	// MushRoom interact with Mario: bigger Mario or disappear
	public boolean interactWith(GameItem other) {
		boolean canInteract = other.isInPosition(this.pos);
		if (canInteract)
			other.receiveInteraction(this);
		return canInteract;
	}
	
	// METHODS CREATE MOOD ----------------------------------------------------------------------------
	protected MovingObject copy(GameWorld game, Position pos) { return new MushRoom(game, pos); }
	
}
