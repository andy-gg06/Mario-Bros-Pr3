package tp1.logic.gameobjects;

import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Land extends GameObject {
	
	private static final String NAME = Messages.LAND_NAME;
	private static final String SHORTCUT = Messages.LAND_SHORTCUT;

	// CONSTRUCTOR -----------------------------------------------------------------------------------
	public Land(Position pos) {
		super(pos, NAME, SHORTCUT);
		this.isSolid = true;
	}
	
	protected Land() {
		this(null);
	}

	// METHODS ---------------------------------------------------------------------------------------
	@Override
	public String getIcon() { return Messages.LAND; }
	
	// Land doesn't interact with or receive from any objects
	@Override
	public boolean interactWith(GameItem other) {
	     boolean canInteract = other.isInPosition(this.pos);
	     if (canInteract) {
	          other.receiveInteraction(this);
	     }
	     return canInteract;
	}

	@Override
	public void update() {}
	
	// METHODS ADD LAND ------------------------------------------------------------------------------
	protected GameObject copy(GameWorld game, Position pos) { return new Land(pos); }
}
