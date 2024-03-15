package game.components.interactive;

import game.components.Interaktive;
import game.components.Item;
import game.components.Message;
import game.engine.GameObject;
import game.engine.Window;
import game.util.Settings;
import net.sourceforge.plantuml.tim.stdlib.IntVal;

/**
 * Eric
 */
public class Eric extends Interaktive {
    private String item_1, item_2;
    private String putItem;
    private String message_1;
    private String message_2;
    private boolean finishedQuest = false;

    @Override
    public void interact(GameObject go) {
        if (!finishedQuest) {
            GameObject item_one = Window.getCurrenScene().getGameObject(item_1);
            GameObject item_two = Window.getCurrenScene().getGameObject(item_2);
            GameObject putItem_go = Window.getCurrenScene().getGameObject(putItem);
            if (go.hasItem(item_one) && go.hasItem(item_two)) {
                go.useItem(item_one);
                go.useItem(item_two);
                finishedQuest = true;
                GameObject message_two = Window.getCurrenScene().getGameObject(message_2);
                if (message_two.getComponent(Message.class) != null) {
                    message_two.getComponent(Message.class).interact();
                }

                if (putItem_go != null) {
                    if (putItem_go.getComponent(Item.class) != null) {
                        go.addItem(putItem_go);
                        if (Settings.SAVE) {
                            Window.getCurrenScene().save();
                        }
                    }
                }

                return;
            }
        }

        GameObject message_one = Window.getCurrenScene().getGameObject(message_1);
        if (message_one.getComponent(Message.class) != null) {
            message_one.getComponent(Message.class).interact();
        }
    }

}
