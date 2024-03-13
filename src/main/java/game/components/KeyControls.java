package game.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import game.editor.PropertiesWindow;
import game.engine.GameObject;
import game.engine.KeyListener;
import game.engine.Window;
import game.util.Settings;

/**
 * KeyControls
 */
public class KeyControls extends Component {
    private float debounceTime = 0.2f;
    private float debounce = 0.0f;

    @Override
    public void editorUpdate(float dt) {
        debounce -= dt;

        PropertiesWindow propertiesWindow = Window.getImguiLayer().getPropertiesWindow();
        GameObject activeGameObject = propertiesWindow.getActiveGameObject();
        List<GameObject> activeGameObjects = propertiesWindow.getActiveGameObjects();
        float multiplier = KeyListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT) ? 0.1f : 1.0f;

        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                KeyListener.keyBeginPress(GLFW_KEY_D) && activeGameObject != null) {
            GameObject newObj = activeGameObject.copy();
            Window.getScene().addGameObject(newObj);
            newObj.transform.position.add(Settings.GRID_WIDTH, 0.0f);
            propertiesWindow.setActiveGameObject(newObj);
            if (newObj.getComponent(StateMachine.class) != null) {
                newObj.getComponent(StateMachine.class).refreshTexture();
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                KeyListener.keyBeginPress(GLFW_KEY_D) && activeGameObjects.size() > 1) {
            List<GameObject> gameObjects = new ArrayList<>(activeGameObjects);
            propertiesWindow.clearSelected();
            for (GameObject go : gameObjects) {
                GameObject copy = go.copy();
                Window.getScene().addGameObject(copy);
                propertiesWindow.addActiveGameObjet(copy);
                if (copy.getComponent(StateMachine.class) != null) {
                    copy.getComponent(StateMachine.class).refreshTexture();
                }
            }
        } else if (KeyListener.keyBeginPress(GLFW_KEY_R) && activeGameObject != null) {
            activeGameObject.transform.rotation += 90;
        } else if (KeyListener.keyBeginPress(GLFW_KEY_R) && activeGameObjects.size() > 1) {
            rotateGOs90Deg(activeGameObjects);
        } else if (KeyListener.keyBeginPress(GLFW_KEY_BACKSPACE)) {
            for (GameObject go : activeGameObjects) {
                go.destroy();
            }
            propertiesWindow.clearSelected();
        } else if (KeyListener.isKeyPressed(GLFW_KEY_PAGE_DOWN) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.zIndex--;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_PAGE_UP) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.zIndex++;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_UP) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.position.y += Settings.GRID_HEIGHT * multiplier;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.position.x -= Settings.GRID_HEIGHT * multiplier;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.position.x += Settings.GRID_HEIGHT * multiplier;
            }
        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN) && debounce < 0) {
            debounce = debounceTime;
            for (GameObject go : activeGameObjects) {
                go.transform.position.y -= Settings.GRID_HEIGHT * multiplier;
            }
        }
    }

    public static void rotateGOs90Deg(List<GameObject> gos) {
        // Berechne den Mittelpunkt der Form
        float centerX = 0;
        float centerY = 0;
        for (GameObject obj : gos) {
            centerX += obj.transform.position.x;
            centerY += obj.transform.position.y;
        }
        centerX /= gos.size();
        centerY /= gos.size();

        // Rotiere jedes GameObject um den Mittelpunkt
        for (GameObject obj : gos) {
            float oldX = obj.transform.position.x - centerX;
            float oldY = obj.transform.position.y - centerY;

            // Rotation um 90 Grad im Uhrzeigersinn
            float newX = oldY + centerX;
            float newY = -oldX + centerY;

            // Setze die neue Position
            obj.transform.position.x = newX;
            obj.transform.position.y = newY;

            obj.transform.rotation -= 90;
        }
    }
}
