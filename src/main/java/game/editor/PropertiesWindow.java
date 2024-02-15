package game.editor;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import game.engine.GameObject;
import game.engine.MouseListener;
import game.renderer.PickingTexture;
import game.scene.Scene;
import gen.lib.cgraph.rec__c;
import imgui.ImGui;

public class PropertiesWindow {
	private GameObject activeGameObject = null;
	private PickingTexture pickingTexture;

	public PropertiesWindow(PickingTexture pickingTexture) {
		this.pickingTexture = pickingTexture;
	}

	public void update(float dt, Scene currentScene){

			if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
				int x = (int) MouseListener.getScreenX();
				int y = (int) MouseListener.getScreenY();

				int gameObjectId = pickingTexture.readPixel(x, y);
				activeGameObject = currentScene.getGameObject(gameObjectId);
			}

	}
	public void imgui() {

		if (activeGameObject != null) {
			ImGui.begin("Properties");
			activeGameObject.imgui();
			ImGui.end();
		}
	}

	public void setActiveGameObject(int gameObjectId) {
		// this.activeGameObject.setActiveGameObject(false);
		// this.activeGameObject.getComponent(SpriteRenderer.class).setDirty();
		// this.activeGameObject = getGameObject(gameObjectId);
		// this.activeGameObject.setActiveGameObject(true);
		// this.activeGameObject.getComponent(SpriteRenderer.class).setDirty();
	}
	public GameObject getActiveGameObject() {
		return this.activeGameObject;
	}
}
