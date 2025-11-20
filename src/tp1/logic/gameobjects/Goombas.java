package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Goombas extends MovingObject{
	
	private static final String NAME = Messages.GOOMBA_NAME;
	private static final String SHORTCUT = Messages.GOOMBA_SHORTCUT;

	// CONSTRUTOR -----------------------------------------------------------------------------------
	public Goombas(GameWorld game, Position pos) {
		super(game, pos, NAME, SHORTCUT);
		this.isSolid = false;
		this.dir = Action.LEFT;
	}
	
	protected Goombas() {
		this(null, null);
	}

	// METHODS --------------------------------------------------------------------------------------
	@Override
	public String getIcon() {  return Messages.GOOMBA; }
	
	
	@Override
	public void update() {
		this.automaticMovement();
	}
	
	@Override
	// Goomba receive interaction from Mario: goomba dies
	public boolean receiveInteraction(Mario obj) {
		if (!this.isAlive() || !obj.isAlive())
			return false;
		obj.receiveInteraction(this);
		return true;
	}
	
	// Goomba interact with Mario: kill Mario
	public boolean interactWith(GameItem other) {
		boolean canInteract = other.isInPosition(this.pos);
	    if (canInteract)
	    	other.receiveInteraction(this);
	    return canInteract;
	}
	
	// METHODS CREATE MOOD ---------------------------------------------------------------------------
	protected MovingObject copy(GameWorld game, Position pos) { return new Goombas(game, pos); }
	
}
