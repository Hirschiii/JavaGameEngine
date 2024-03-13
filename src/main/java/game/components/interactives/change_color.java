package game.components.interactives;

import org.joml.Vector4f;

import game.components.Interaktive;
import game.components.SpriteRenderer;
import game.engine.GameObject;

/**
 * change_color
 */
public class change_color extends Interaktive {

    @Override
    public void interaction(GameObject go) {
        if (gameObject.getComponent(SpriteRenderer.class) != null) {

            gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 1, 1, 1));
        }
    }

}
