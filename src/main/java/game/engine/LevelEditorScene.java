package game.engine;

import org.joml.Vector2f;

import game.components.Sprite;
import game.components.SpriteRenderer;
import game.components.Spritesheet;
import game.util.AssetPool;

public class LevelEditorScene extends Scene {

	private GameObject obj1;
	private Spritesheet sprites;

	public LevelEditorScene() {

	}

	@Override
	public void init() {
		loadResources();

		this.camera = new Camera(new Vector2f());

		// sprites = new
		// AssetPool().getSpritesheet("src/main/resources/assets/images/spritesheet.png");

		obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 4);
		obj1.addComponent(new SpriteRenderer(new Sprite(
				AssetPool.getTexture("src/main/resources/assets/images/blendImage1.png"))));
		this.addGameObject(obj1);

		GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 0);
		obj2.addComponent(new SpriteRenderer(new Sprite(
				AssetPool.getTexture("src/main/resources/assets/images/testImage.png"))));
		this.addGameObject(obj2);

	}

	private void loadResources() {
		AssetPool.getShader("src/main/resources/assets/shaders/default.glsl");

		AssetPool.addSpritesheet("src/main/resources/assets/images/spritesheet.png",
				new Spritesheet(AssetPool.getTexture("src/main/resources/assets/images/spritesheet.png"),
						16, 16, 26, 0));
	}

	@Override
	public void update(float dt) {
		// obj1.transform.position.x += 10 * dt;

		// System.out.println("FPS: " + (1.0f / dt));
		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}

		this.renderer.render();

	}

}
