package game.scene;

import org.joml.Vector2f;
import org.joml.Vector4f;

import game.components.MouseControls;
import game.components.Rigidbody;
import game.components.Sprite;
import game.components.SpriteRenderer;
import game.components.Spritesheet;
import game.engine.Camera;
import game.engine.GameObject;
import game.engine.MouseListener;
import game.engine.Prefabs;
import game.engine.Transform;
import game.util.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;

public class LevelEditorScene extends Scene {

	private GameObject obj1;
	private SpriteRenderer obj1Sprite;
	private Spritesheet sprites;

	MouseControls mouseControls = new MouseControls();

	public LevelEditorScene() {

	}

	@Override
	public void init() {
		loadResources();
		this.camera = new Camera(new Vector2f(0, 0));

		sprites = new AssetPool()
				.getSpritesheet("src/main/resources/assets/images/spritesheets/decorationsAndBlocks.png");

		if (loadedLevel) {
			this.activeGameObject = gameObjects.get(0);
			return;
		}

		obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
				new Vector2f(256, 256)), 1);

		obj1Sprite = new SpriteRenderer();
		obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
		obj1.addComponent(obj1Sprite);
		obj1.addComponent(new Rigidbody());

		GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(300, 100), new Vector2f(256, 256)), 2);
		SpriteRenderer obj2SpriteRender = new SpriteRenderer();
		Sprite obj2Sprite = new Sprite();
		obj2Sprite.setTexture(AssetPool.getTexture("src/main/resources/assets/images/blendImage2.png"));
		obj2SpriteRender.setSprite(obj2Sprite);

		obj2.addComponent(obj2SpriteRender);

		this.addGameObject(obj1);
		this.addGameObject(obj2);

	}

	@Override
	public void update(float dt) {

		mouseControls.update(dt);
		// obj1.transform.position.x += 10 * dt;

		// System.out.println("FPS: " + (1.0f / dt));
		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}

		this.renderer.render();

	}

	private void loadResources() {
		AssetPool.getShader("src/main/resources/assets/shaders/default.glsl");
		AssetPool.getTexture("src/main/resources/assets/images/blendImage2.png");

		AssetPool.addSpritesheet("src/main/resources/assets/images/spritesheets/decorationsAndBlocks.png",
				new Spritesheet(
						AssetPool.getTexture("src/main/resources/assets/images/spritesheets/decorationsAndBlocks.png"),
						16, 16, 81, 0));
	}

	@Override
	public void imgui() {
		ImGui.begin("Test Titel");

		ImVec2 windowPos = new ImVec2();
		ImGui.getWindowPos(windowPos);
		ImVec2 windowSize = new ImVec2();
		ImGui.getWindowSize(windowSize);
		ImVec2 itemSpacing = new ImVec2();
		ImGui.getStyle().getItemSpacing(itemSpacing);

		float windowX2 = windowPos.x + windowSize.x;
		float windowY2 = windowPos.y + windowSize.y;

		for (int i = 0; i < sprites.size(); i++) {
			Sprite sprite = sprites.getSprite(i);
			float spriteWidth = sprite.getWidth() * 4;
			float spriteHeight = sprite.getHeight() * 4;

			int id = sprite.getTexId();
			Vector2f[] texCoords = sprite.getTexCoords();

			ImGui.pushID(i);
			if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x,
					texCoords[2].y)) {
				GameObject object = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight);
				// Attach to mouse Cursor to drop
				mouseControls.pickupObject(object);
			}
			ImGui.popID();

			ImVec2 lastButtonPos = new ImVec2();
			ImGui.getItemRectMax(lastButtonPos);

			float lastButtonX2 = lastButtonPos.x;
			float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
			if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
				ImGui.sameLine();
			}
		}

		ImGui.end();
	}

}
