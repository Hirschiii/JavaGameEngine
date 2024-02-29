package game.observers;

import game.engine.GameObject;
import game.observers.events.Event;

public interface Observer {
	void onNotify(GameObject object, Event event);
}
