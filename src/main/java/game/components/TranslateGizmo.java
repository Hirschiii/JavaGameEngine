package game.components;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import javax.swing.text.html.BlockView;

import org.joml.Vector2f;
import org.joml.Vector4f;

import game.editor.PropertiesWindow;
import game.engine.GameObject;
import game.engine.MouseListener;
import game.engine.Prefabs;
import game.engine.Window;

public class TranslateGizmo extends Gizmo {

	public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
		super(arrowSprite, propertiesWindow);
	}

	@Override
	public void editorUpdate(float dt) {
		if (activeGameObject != null) {
			if (xAxisActive && !yAxisActive) {
				activeGameObject.transform.position.x -= MouseListener.getWorldDX();
			}
			if (yAxisActive) {
				activeGameObject.transform.position.y -= MouseListener.getWorldDY();
			}
		}
		super.editorUpdate(dt);
	}
}
