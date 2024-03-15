package game.components.interactives;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joml.Vector2f;

import game.components.Interaktive;
import game.components.Item;
import game.components.PlayerController;
import game.engine.GameObject;
import game.engine.KeyListener;
import game.engine.Window;
import game.util.AssetPool;
import game.util.Settings;

public class getItem extends Interaktive {
    public String giveItem;

    @Override
    public void interact(GameObject go) {
        GameObject item = Window.getCurrenScene().getGameObject(giveItem);
        if (item != null) {
            if (item.getComponent(Item.class) != null) {
                go.addItem(item);
                if (Settings.SAVE) {
                    Window.getCurrenScene().save();
                }
            }
        }

    }
}
