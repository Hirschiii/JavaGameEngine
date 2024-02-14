package game.components;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector2f;

import game.engine.GameObject;
import game.engine.MouseListener;
import game.util.Settings;

public class MouseControls extends Component {
	GameObject holdingObject = null;	

	public void pickupObject(GameObject go) {
		this.holdingObject = go;

		game.engine.Window.getScene().addGameObject(go);
	}

	public void place() {
		this.holdingObject = null;
	}

	@Override
	public void update(float dt) {
		if (holdingObject != null) {
			Vector2f WorldPos = MouseListener.getWorld();

            holdingObject.transform.position.x = (((int)Math.floor(WorldPos.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2.0f) - (holdingObject.getTransform().scale.x / 2);
            holdingObject.transform.position.y = (((int)Math.floor(WorldPos.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2.0f) - (holdingObject.getTransform().scale.y / 2);

			if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
				place();
			}
		}
	}
}
