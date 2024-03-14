package game.components;

import org.joml.Vector2f;

import game.components.Component;
import game.engine.Transform;
import game.engine.Window;

/**
 * PositionAbsolut
 * Setze die Position Abselut bzw relativ von der Kamera
 */
public class PositionAbsolut extends Component {
    public Vector2f absolutPos = new Vector2f(0, 0);

    @Override
    public void update(float dt) {
        Vector2f camPos = Window.getCurrenScene().camera().getPosition();

        this.gameObject.transform.position = new Vector2f(
                camPos.x + absolutPos.x,
                camPos.y + absolutPos.y);
    }

}
