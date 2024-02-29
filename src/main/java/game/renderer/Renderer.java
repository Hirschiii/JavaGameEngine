package game.renderer;

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.components.SpriteRenderer;
import game.engine.GameObject;

public class Renderer {
	private final int MAX_BATCH_SIZE = 1000;
	private List<RenderBatch> batches;
	private static Shader currentShader;

	public Renderer() {
		this.batches = new ArrayList<>();
	}

	public void add(GameObject go) {
		SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
		if (spr != null) {
			add(spr);
		}
	}

	private void add(SpriteRenderer sprite) {
		boolean added = false;
		for (RenderBatch batch : batches) {
			if (batch.hasRoom() && batch.zIndex() == sprite.gameObject.transform.getzIndex()) {
				Texture tex = sprite.getTexture();
				if (tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom())) {
					batch.addSprite(sprite);
					added = true;
					break;
				}
			}
		}

		if (!added) {
			RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.transform.getzIndex(), this);
			newBatch.start();
			batches.add(newBatch);
			newBatch.addSprite(sprite);
			Collections.sort(batches);
		}
	}

	public static void bindShader(Shader shader) {
		currentShader = shader;
	}

	public static Shader getBoundShader() {
		return currentShader;
	}

	public void render() {
		currentShader.use();
		for (int i=0; i < batches.size(); i++) {
			RenderBatch batch = batches.get(i);
			batch.render();
		}
	}

	public void destroyGameObject(GameObject go) {
		if (go.getComponent(SpriteRenderer.class) == null)
			return;

		for (int i=0; i < batches.size(); i++) {
			RenderBatch batch = batches.get(i);
			if (batch.destroyIfExists(go)) {
				return;
			}
		}
	}
}
