package tp1.logic.gameobjects;

import tp1.logic.Action;
import tp1.logic.GameWorld;
import tp1.logic.Position;

public abstract class MovingObject extends GameObject{
	
	protected boolean isFalling;
	protected Action dir;

	// CONSTRUCTOR -----------------------------------------------------------------------------------
	public MovingObject(GameWorld game, Position pos, String name, String shorcut) {
		super(game, pos, name, shorcut);
		this.isFalling = false;
	}

	// METHODS ---------------------------------------------------------------------------------------
	protected void automaticMovement() {
		if (pos.get_row() != 15 && game.isSolidAt(pos.get_col(), pos.get_row() + 1)) { // it's on land
			this.isFalling = false; // not falling
			
			if (dir == Action.LEFT) {
				if (pos.get_col() == 0 || game.isSolidAt(pos.get_col() - 1, pos.get_row()))
					dir = Action.RIGHT;
				else
					pos.moveLeft();
			}
			else {
				dir = Action.RIGHT; //if mario was falling before, he stops falling and starts walking
				if (pos.get_col() == 29 || game.isSolidAt(pos.get_col() + 1, pos.get_row()))
					dir = Action.LEFT;
				else
					pos.moveRight();
			}
		}
		else {
			pos.moveDown();
			this.isFalling = true; // is falling
			if (pos.get_row() == 15)
				this.dead();
		}
	}
	
	// METHODS CREATE MOOD ---------------------------------------------------------------------------
	protected abstract MovingObject copy(GameWorld game, Position pos);
	
	@Override
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
		
		MovingObject o = this.copy(game, new Position(row,col));
		o.pos = new Position(row, col);
		
		if (objWords.length == 3) { // direction
			String string_dir = objWords[2];
			if (string_dir.equalsIgnoreCase("L") || string_dir.equalsIgnoreCase("LEFT"))
                o.dir = Action.LEFT;
            else if (string_dir.equalsIgnoreCase("R") || string_dir.equalsIgnoreCase("RIGHT"))
                o.dir = Action.RIGHT;
		}
		
		return o;
	}
}
