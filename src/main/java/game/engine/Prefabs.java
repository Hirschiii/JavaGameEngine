package game.engine;

import org.joml.Vector2f;

import game.components.Sprite;
import game.components.SpriteRenderer;

public class Prefabs {
	public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
		GameObject block = Window.getScene().createGameObject("Sprite_Object_Gen");
		block.transform.scale = new Vector2f(sizeX, sizeY);

		SpriteRenderer renderer = new SpriteRenderer();
		renderer.setSprite(sprite);
		block.addComponent(renderer);

		return block;
	}
}
