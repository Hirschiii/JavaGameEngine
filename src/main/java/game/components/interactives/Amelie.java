package game.components.interactives;

import game.components.Component;
import game.components.GameCamera;
import game.components.Interaktive;
import game.engine.GameObject;
import game.engine.Window;

/**
 * Amelie
 */
public class Amelie extends Interaktive {
    private String readItem;

    @Override
    public void interact(GameObject go) {
        GameObject gameCam = Window.getCurrenScene().getGameObjectWith(GameCamera.class);
        if (gameCam != null) {
            GameObject readGO = Window.getCurrenScene().getGameObject(readItem);
            if (readGO != null) {
                gameCam.getComponent(GameCamera.class).read(readItem, 20);
            }
        }

    }

}
