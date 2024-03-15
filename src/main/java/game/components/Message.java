package game.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

/**
 * Message
 */
public class Message extends Component {
    private float showTime = 10;
    private float time = showTime;
    private boolean active = false;

    @Override
    public void start() {
        if (gameObject.getComponent(PositionAbsolut.class) == null) {
            gameObject.addComponent(new PositionAbsolut());

        }
        gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
    }

    @Override
    public void gameStart() {
        gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 0, 0, 0));
        gameObject.transform.zIndex = 100;
    }

    @Override
    public void update(float dt) {
        if (active) {
            time -= dt;
            if (time < 0) {
                setInactive();
            }
        } else {
            gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 0, 0, 0));
        }
    }

    private void setInactive() {
        gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 0, 0, 0));
        this.active = false;
    }

    public void interact() {
        gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
        this.active = true;
    }

}
