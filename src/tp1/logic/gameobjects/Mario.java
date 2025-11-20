package tp1.logic.gameobjects;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;
import tp1.view.Messages;

public class Mario extends MovingObject  {
	
	private boolean isBig;
	private boolean jumping;
	private boolean crash_box;
	private Action last_dir_h;
	private List<Action> actList;
	
	private static final String NAME = Messages.MARIO_NAME;
	private static final String SHORTCUT = Messages.MARIO_SHORTCUT;
	
	// CONSTRUTOR -----------------------------------------------------------------------------------
	public Mario(GameWorld game, Position pos) {
		super(game, pos, NAME, SHORTCUT);
		this.isSolid = false;
		this.dir = Action.RIGHT;
		this.isBig = true;
		this.jumping = false;
		this.crash_box = false;
		this.last_dir_h = Action.RIGHT;
		this.actList = new ArrayList<>();
	}
	
	protected Mario() {
		this(null, null);
	}
	
	// METHODS --------------------------------------------------------------------------------------
	public boolean isBig() { return this.isBig; }
	
	public boolean isCrashing() { return this.crash_box && this.jumping; }
	
	public void mario_crashBox() {
		this.crash_box = false;
		this.jumping = false;
	}
	
	@Override
	public boolean isInPosition(Position pos) {
		Position upper = new Position(this.pos.get_row() - 1, this.pos.get_col());
	    return this.pos.equals(pos) || (isBig && upper.equals(pos));
	}
	
	public String getIcon() {
		if (!this.isAlive())
			return Messages.EMPTY;
		
		switch (dir) {
		case RIGHT: return Messages.MARIO_RIGHT;
		case LEFT: return Messages.MARIO_LEFT;
		case STOP: return Messages.MARIO_STOP;	
		case UP:
		case DOWN:
		{
			if (this.last_dir_h == Action.LEFT)
				return Messages.MARIO_LEFT;
			else if (this.last_dir_h == Action.RIGHT)
				return Messages.MARIO_RIGHT;
			else
				return Messages.MARIO_STOP;
		}
		default: return Messages.MARIO_STOP;
		}
	}
	
	public void addAction(Action act) {
		this.actList.add(act);
	}
	
	public void move(Action act, Action before) {
		switch (act) {
			case RIGHT:
				if (pos.get_col() < 29 && before != Action.LEFT) {
					dir = Action.RIGHT;
					this.last_dir_h = Action.RIGHT;
					
					this.isFalling = false;
					this.jumping = false;
					
					boolean right_cell = game.isSolidAt(pos.get_col() + 1, pos.get_row());
					boolean right_cell_up = game.isSolidAt(pos.get_col() + 1, pos.get_row() - 1);
					/*
					 * !right_cell && !isBig -> no land in right cell (small mario)
					 * !right_cell && !right_cell_up -> no land in right cell and its upper (big mario)
					 */
					if (!right_cell && (!isBig || !right_cell_up))
					    pos.moveRight();
					else
						dir = Action.LEFT;
				}
			break;
			case LEFT:
				if (pos.get_col() > 0 && before != Action.RIGHT) {
					dir = Action.LEFT;
					this.last_dir_h = Action.LEFT;
					
					this.isFalling = false;
					this.jumping = false;
					
					boolean left_cell = game.isSolidAt(pos.get_col() - 1, pos.get_row());
					boolean left_cell_up = game.isSolidAt(pos.get_col() - 1, pos.get_row() - 1);
					/*
					 * !left_cell && !isBig -> no land in right cell (small mario)
					 * !left_cell && !left_cell_up -> no land in right cell and its upper (big mario)
					 */
					if (!left_cell && (!isBig || !left_cell_up))
					    pos.moveLeft();
					else
						dir = Action.RIGHT;
				}
			break;
			case Action.UP:
				if (before != Action.DOWN) {
					dir = Action.UP;
					
					this.isFalling = false;
					this.jumping = true;
					
					boolean up_cell = game.isSolidAt(pos.get_col(), pos.get_row() - 1);
					boolean up_big = game.isSolidAt(pos.get_col(), pos.get_row() - 2);
					
					// look if mario interact with box full
					boolean box_cell = game.positionToString(pos.get_col(), pos.get_row() - 1).equals(Messages.BOX);
					boolean box_big_cell = game.positionToString(pos.get_col(), pos.get_row() - 2).equals(Messages.BOX);
					
					/*
					 * (small mario)                                 (big mario)
					 */
					if ((!isBig && pos.get_row() > 0 && !up_cell) || (isBig && pos.get_row() > 1 && !up_big))
						pos.moveUp();
					// determinate if Mario is interacting with full box
					else if ((!isBig && pos.get_row() > 0 && box_cell) || (isBig && pos.get_row() > 1 && box_big_cell))
						this.crash_box = true;
				}
			break;
			case Action.DOWN:
				if (before != Action.UP) {
					
					this.isFalling = true;
					this.jumping = false;
					
					if (game.isSolidAt(pos.get_col(), pos.get_row() + 1)) {
						dir = Action.STOP;
					}
					else {
						while (!game.isSolidAt(pos.get_col(), pos.get_row() + 1) && pos.get_row() < 15) {
							pos.moveDown();
							game.doInteraction(this);
						}
						if (pos.get_row() == 15)
							this.dead();
					}
				}
			break;
			// action stop
			case Action.STOP:
				dir = Action.STOP;
				this.last_dir_h = Action.STOP;
				this.jumping = false;
			break;
			default:
		}
	}
	
	@Override
	public void update() {
		if (dir == Action.STOP) {
	        this.isFalling = false;
	        this.jumping = false;
	    }

	    if (this.actList.isEmpty() && dir != Action.STOP && this.isAlive()) {
	        this.jumping = false;
	        this.automaticMovement();
	        game.doInteraction(this);
	    }
	    else {
	        Action before = Action.NONE;
	        for (int i = 0; i < actList.size() && this.isAlive(); i++) {
	            Action act = actList.get(i);
	            move(act, before);
	            before = act;
	            game.doInteraction(this);
	        }
	        this.actList.clear();
	    }
//	    if (!this.isAlive()) {
//	        game.marioDead();
//		}
	}
	
	// Mario receive interaction from Goomba: mario dies and goomba too
	@Override
	public boolean receiveInteraction(Goombas obj) {
		if (!this.isAlive() || !obj.isAlive())
			return false;
		
		obj.dead();
		game.addPoints(100);
		if (!this.isFalling) {
			if (this.isBig)
				this.isBig = false;
			else {
				this.dead();
			}
		}
		return true;
	}
	
	@Override
	public boolean receiveInteraction(MushRoom obj) {
		if (!this.isAlive() || !obj.isAlive())
			return false;
		// if mario's big, mushroom disappear
		obj.dead();
		if (!this.isBig)
			this.isBig = true;
		return true;
	}
	
	@Override
	public boolean receiveInteraction(Box obj) {
		if (!this.isAlive())
			return false;
		obj.receiveInteraction(this);
		return true;
	}
	
	// ExitDoor receive interaction from Mario: Mario exits and wins
	@Override
	public boolean receiveInteraction(ExitDoor obj) {
		game.marioExited();
		return true;
	}
	
	// Mario interact with Goomba (goomba dies) or ExitDoor (mario wins)
	@Override
	public boolean interactWith(GameItem other) {
		boolean canInteract = other.isInPosition(this.pos);
		if (canInteract)
			other.receiveInteraction(this);
		return canInteract;
	}
	
	// METHODS CREATE MOOD ----------------------------------------------------------------------------
	protected MovingObject copy(GameWorld game, Position pos) { return new Mario(); }
	
	@Override
	public GameObject parse(String[] objWords, GameWorld game) {
		if (!this.matchObjectName(objWords[1]) || objWords.length > 4)
			return null;
		
		String[] position = objWords[0].trim().substring(1, objWords[0].length() - 1).split(","); // return (row, col)
		
		if (!isNumeric(position[0]) || !isNumeric(position[1]))
			return null;
		
		int row = Integer.parseInt(position[0].trim());
		int col = Integer.parseInt(position[1].trim());
		
		if (!game.rowInLimits(row) || !game.colInLimits(col))
			return null;
		
		Mario mario = new Mario(game, new Position(row, col));
		game.isMario(mario);
		
		if (objWords.length >= 3) { // direction
			String string_dir = objWords[2];
			if (string_dir.equalsIgnoreCase("L") || string_dir.equalsIgnoreCase("LEFT"))
                mario.dir = Action.LEFT;
            else if (string_dir.equalsIgnoreCase("R") || string_dir.equalsIgnoreCase("RIGHT"))
                mario.dir = Action.RIGHT;
		}
		
		if (objWords.length == 4) {
			String string_size = objWords[3];
			if (string_size.equalsIgnoreCase("B") || string_size.equalsIgnoreCase("BIG"))
                mario.isBig = true;
            else if (string_size.equalsIgnoreCase("S") || string_size.equalsIgnoreCase("SMALL"))
                mario.isBig = false;
		}
		return mario;
	}
}
