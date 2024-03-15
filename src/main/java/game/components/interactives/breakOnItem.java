package game.components.interactives;

import game.components.Interaktive;
import game.components.Rigidbody;
import game.engine.GameObject;
import game.engine.Window;

/**
 * breakOnItem
 */
public class breakOnItem extends Interaktive{
    private String itemName;

    @Override
    public void interact(GameObject go) {
        GameObject item = Window.getCurrenScene().getGameObject(itemName);
        if(item == null || go == null || gameObject.getComponent(Rigidbody.class) == null) return;

        if(go.useItem(item)) {
            gameObject.removeComponent(Rigidbody.class);
        }


    }


}
