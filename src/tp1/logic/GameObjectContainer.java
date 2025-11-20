package tp1.logic;

import java.util.ArrayList;
import java.util.List;

import tp1.logic.gameobjects.GameObject;
import tp1.logic.gameobjects.GameItem;
import tp1.view.Messages;

public class GameObjectContainer {
	private List<GameObject> objects;
	private List<GameObject> new_objects;
	
	// CONSTRUCTOR -----------------------------------------------------------------------------------
	public GameObjectContainer() {
		this.objects = new ArrayList<>();
		this.new_objects = new ArrayList<>();
	}
	
	// METHODS ---------------------------------------------------------------------------------------
	// Only one add method (polymorphism)
	public void add(GameObject object) {
		this.objects.add(object);
	}
	
	public void add_new (GameObject object) {
		this.new_objects.add(object);
	}

	public String postitionToString(Position pos) {
		String icons = "";
		
		for (GameObject o : objects) {
			if (o.isInPosition(pos) && o.isAlive())
				icons = icons + o.getIcon();
		}
		if (icons.equals(""))
			return Messages.EMPTY;
		
		return icons;
	}
	
	public boolean isSolidAt(int col, int row) {
		for (GameObject o : this.objects) {
	        if (o.isInPosition(new Position(row, col)) && o.isSolid())
	            return true;
	    }
		return false;
	}
	
	public void update(GameWorld game) {
		for (int i = 0; i < this.objects.size() && !game.isReset(); i++) {
			GameObject o = this.objects.get(i);
			o.update();
			if (o.isAlive() && !game.isReset())
				doInteraction(o, game);
		}
		removeDead();
		objects.addAll(this.new_objects);
		this.new_objects.clear();
	}
	
	private void removeDead() {
		int i = 0;
		while (i < this.objects.size()) {
			if (!this.objects.get(i).isAlive())
				this.objects.remove(i);
			else
				i++;
		}
	}
	
	public void doInteraction(GameItem other, GameWorld game) {
		for (int i = 0; i < this.objects.size() && !game.isReset(); i++) {
			GameObject o = this.objects.get(i);
			if (o != other && other.isAlive() && !game.isReset()) // no interact with itself and be alive
				other.interactWith(o);
		}
	}
}
