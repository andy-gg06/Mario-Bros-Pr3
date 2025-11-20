package tp1.logic.gameobjects;

import tp1.logic.GameWorld;

import java.util.Arrays;
import java.util.List;

public class GameObjectFactory {

	private static final List<GameObject> availableObjects = Arrays.asList(
			new Land(),
			new ExitDoor(),
			new Goombas(),
			new Mario(),
			new MushRoom(),
			new Box()
	);
	
	public static GameObject parse(String objWords[], GameWorld game) {
		for (GameObject obj : availableObjects) {
			GameObject o = obj.parse(objWords, game);
			if (o != null)
				return o;
        }
		return null;
	}
}
