package game.components;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import game.engine.GameObject;
import game.engine.MouseListener;

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
			holdingObject.transform.position.x = MouseListener.getOrthX() - 16;
			holdingObject.transform.position.y = MouseListener.getOrthY() - 16;

			if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
				System.out.println("Paced Obj");
				place();
			}
		}
	}
}
