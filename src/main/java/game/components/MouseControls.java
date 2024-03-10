package game.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector2f;
import org.joml.Vector4f;

import game.engine.GameObject;
import game.engine.KeyListener;
import game.engine.MouseListener;
import game.engine.Window;
import game.util.Settings;

public class MouseControls extends Component {
    GameObject holdingObject = null;
    private float debounceTime = 0.05f;
    private float debounce = debounceTime;
    private Vector2f hoverSquare = new Vector2f();

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        go.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.8f));

        game.engine.Window.getScene().addGameObject(go);
    }

    public void place() {
        System.out.println(hoverSquare);
        System.out.println(getHoverSquare());
        if (!hoverSquare.equals(getHoverSquare())) {
            System.out.println("Equal");
            GameObject newObj = this.holdingObject.copy();
            newObj.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
            Window.getScene().addGameObject(newObj);
            hoverSquare = getHoverSquare();
        } else {
            System.out.println("NOt");
        }

    }

    private Vector2f getHoverSquare() {
        float x = MouseListener.getWorldX();
        float y = MouseListener.getWorldY();
        x = ((int) Math.floor(x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2.0f;
        y = ((int) Math.floor(y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2.0f;

        return new Vector2f(x, y);
    }

    @Override
    public void editorUpdate(float dt) {
        debounce -= dt;
        if (holdingObject != null && debounce <= 0) {

            holdingObject.transform.position = getHoverSquare();

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
                debounce = debounceTime;
            }

            if (KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
                holdingObject.destroy();
                holdingObject = null;
            }

        }
    }
}
