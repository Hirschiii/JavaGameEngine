package game.components.interactives;

import org.joml.Vector2f;
import org.joml.Vector4f;

import game.components.Interaktive;
import game.components.SpriteRenderer;
import game.engine.GameObject;

/**
 * change_color
 */
public class grow_in_size extends Interaktive {

    @Override
    public void interact(GameObject go) {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {

            gameObject.transform.scale = new Vector2f(1.4f, 1.4f);
        }
    }

}
