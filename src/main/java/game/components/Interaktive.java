package game.components;

import org.joml.Vector3f;

import game.engine.GameObject;
import game.renderer.DebugDraw;

/**
 * Interaktive
 */
public abstract class Interaktive extends Component {
    private boolean interactive = false;

    public abstract void interact(GameObject go);

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public boolean getInteractive() {
        return this.interactive;
    }
    @Override
    public void update(float dt) {
        if(interactive) {
        DebugDraw.addBox2D(gameObject.transform.position, gameObject.transform.scale, gameObject.transform.rotation,
                new Vector3f(1, 0, 0));
        }
    }

    @Override
    public void editorUpdate(float dt) {
        DebugDraw.addBox2D(gameObject.transform.position, gameObject.transform.scale, gameObject.transform.rotation,
                new Vector3f(1, 0, 0));
    }

}
