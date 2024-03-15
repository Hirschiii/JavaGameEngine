package game.components.interactives;


import static org.lwjgl.glfw.GLFW.*;

import java.util.List;

import org.joml.Vector2f;

import game.components.Interaktive;
import game.components.PlayerController;
import game.engine.GameObject;
import game.engine.Item;
import game.engine.KeyListener;
import game.engine.Window;
import game.util.AssetPool;

public class getItem extends Interaktive {
    private List<Item> items;

    public getItem(Item item) {
        this.items.add(item);
    }

    @Override
    public void interact(GameObject go) {
        if(this.items.size() > 0) {
            go.inventar.add(items.getFirst());
            items.remove(0);
        }

    }
}
