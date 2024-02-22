package game.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_COMMA;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_DECIMAL;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

import org.joml.Vector2f;

import game.engine.Camera;
import game.engine.*;

public class EditorCamera extends Component {
	private Camera levelEditorCamera;
	private Vector2f clickOrigin;
	private boolean reset = false;

	private float dragDebounce = 0.016f;

	private float dragSensitivity = 30.0f;
	private float scrollSensitivity = 0.1f;
	private float lerpTime = 0f;

	public EditorCamera(Camera levelEditorCamera) {
		this.levelEditorCamera = levelEditorCamera;
		this.clickOrigin = new Vector2f();

	}

	@Override
	public void update(float dt) {
		if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0) {
			this.clickOrigin = MouseListener.getWorld();
			dragDebounce -= dt;
			return;
		} else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
			Vector2f mousePos = new Vector2f(MouseListener.getWorld());
			Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);

			levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitivity));
			this.clickOrigin.lerp(mousePos, dt);
		}

		if (dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
			this.dragDebounce = 0.32f;
		}

		if (MouseListener.getScrollY() != 0.0f) {
			float addValue = (float) Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity),
					1 / levelEditorCamera.getZoom());

			addValue *= -Math.signum(MouseListener.getScrollY());

			levelEditorCamera.addZoom(addValue);
		}

		if (KeyListener.isKeyPressed(GLFW_KEY_COMMA)) {
			reset = true;
		}

		if (reset) {
			levelEditorCamera.position.lerp(new Vector2f(), lerpTime);
			levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() 
					+ ((1.0f - levelEditorCamera.getZoom()) * lerpTime));
			this.lerpTime += 0.1f*dt;
			if (Math.abs(levelEditorCamera.position.x) <= 0.05f && Math.abs(levelEditorCamera.position.y) <= 0.05f) {
				this.lerpTime = 0;
				levelEditorCamera.position.set(0f, 0f);
				this.levelEditorCamera.setZoom(1.0f);
				reset = false;
			}
		}
	}

}
