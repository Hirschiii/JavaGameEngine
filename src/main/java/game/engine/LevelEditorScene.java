package game.engine;

import org.joml.Vector2f;
import org.joml.Vector4f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import game.components.Sprite;
import game.components.SpriteRenderer;
import game.components.Spritesheet;
import game.util.AssetPool;
import imgui.ImGui;

public class LevelEditorScene extends Scene {

	private GameObject obj1;
	private SpriteRenderer obj1Sprite;
	private Spritesheet sprites;

	public LevelEditorScene() {

	}

	@Override
	public void init() {
		loadResources();

		this.camera = new Camera(new Vector2f());

		// sprites = new
		// AssetPool().getSpritesheet("src/main/resources/assets/images/spritesheet.png");

		obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
				new Vector2f(256, 256)), 1);

		obj1Sprite = new SpriteRenderer();
		obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
		obj1.addComponent(obj1Sprite);

		GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(300, 100), new Vector2f(256, 256)), 2);
		SpriteRenderer obj2SpriteRender = new SpriteRenderer();
		Sprite obj2Sprite = new Sprite();
		obj2Sprite.setTexture(AssetPool.getTexture("src/main/resources/assets/images/blendImage2.png"));
		obj2SpriteRender.setSprite(obj2Sprite);

		obj2.addComponent(obj2SpriteRender);

		this.addGameObject(obj1);
		this.addGameObject(obj2);

		this.activeGameObject = obj1;

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String serialized = gson.toJson(new Vector2f(1, 2));
		System.out.println(serialized);
		Vector2f one = gson.fromJson(serialized, Vector2f.class);
		System.out.println(one);
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

	@Override
	public void imgui() {
		ImGui.begin("Test Window");
		ImGui.text("Random Text");
		ImGui.end();

	}

}
