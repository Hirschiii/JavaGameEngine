package game.components.interactives;


import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

import game.components.Interaktive;
import game.components.PlayerController;
import game.engine.GameObject;
import game.engine.KeyListener;
import game.engine.Window;
import game.util.AssetPool;

public class Pipe extends Interaktive {
    private String connectingPipeName = "";
    private Vector2f offset = new Vector2f(0, 0);
    private transient GameObject connectingPipe = null;


    @Override
    public void start() {
        connectingPipe = Window.getScene().getGameObject(connectingPipeName);
    }

    @Override
    public void interact(GameObject go) {
        if(connectingPipe != null) {
            go.transform.position.set(new Vector2f(
                        connectingPipe.transform.position.x + offset.x,
                        connectingPipe.transform.position.y + offset.y
                        ));
        }
    }
}
