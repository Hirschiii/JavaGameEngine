package game.editor;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import game.components.Component;
import game.components.Gizmo;
import game.components.NonPickable;
import game.components.Rigidbody;
import game.components.TranslateGizmo;
import game.engine.GameObject;
import game.engine.MouseListener;
import game.renderer.PickingTexture;
import game.scene.LevelEditorScene;
import game.scene.Scene;
import imgui.ImGui;

public class PropertiesWindow {
	private GameObject activeGameObject = null;
	private PickingTexture pickingTexture;

	private float debounce = 0.2f;

	public PropertiesWindow(PickingTexture pickingTexture) {
		this.pickingTexture = pickingTexture;
	}

	public void update(float dt, Scene currentScene) {
		debounce -= dt;

		if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
			int x = (int) MouseListener.getScreenX();
			int y = (int) MouseListener.getScreenY();

			int gameObjectId = pickingTexture.readPixel(x, y);
			GameObject go = currentScene.getGameObject(gameObjectId);

			if (go != null && go.getComponent(NonPickable.class) == null) {
				activeGameObject = go;
			} else if (pickingTexture == null && !MouseListener.isDragging()) {
				activeGameObject = null;
			}
			this.debounce = 0.2f;
		}

	}

	public void imgui() {

		if (activeGameObject != null) {
			ImGui.begin("Properties");

			if (ImGui.beginPopupContextWindow("ComponentAdder")) {
				// TODO Iterate through Components
				if (activeGameObject.getComponent(Rigidbody.class) == null) {
					if (ImGui.menuItem("Add Rigidbody")) {
						activeGameObject.addComponent(new Rigidbody());
					}
				}
				ImGui.endPopup();
			}

			activeGameObject.imgui();
			ImGui.end();
		}
	}

	public void setActiveGameObject(GameObject go) {
		this.activeGameObject = go;
	}

	public GameObject getActiveGameObject() {
		return this.activeGameObject;
	}

}
