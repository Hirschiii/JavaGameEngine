package game.components.interactives;

import game.components.Interaktive;
import game.components.Rigidbody;
import game.components.StateMachine;
import game.engine.GameObject;
import game.engine.Window;

/**
 * breakOnItem
 */
public class breakOnItem extends Interaktive {
    private String itemName;
    private boolean borken = false;
    private boolean useItem = true;
    private transient StateMachine stateMachine;

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
    }

    @Override
    public void interact(GameObject go) {
        if (borken) {
                if(this.stateMachine != null) {
                this.stateMachine.trigger("Break");
                }
            System.out.println("Break");
            return;
        }
        GameObject item = Window.getCurrenScene().getGameObject(itemName);
        if (item == null || go == null || gameObject.getComponent(Rigidbody.class) == null)
            return;

        if (useItem) {
            if (go.useItem(item)) {
                gameObject.removeComponent(Rigidbody.class);
                if(this.stateMachine != null) {
                this.stateMachine.trigger("Break");
                }
                borken = true;
            }
        } else {
            if (go.hasItem(item)) {
                gameObject.removeComponent(Rigidbody.class);
                if(this.stateMachine != null) {
                this.stateMachine.trigger("Break");
                }
                borken = true;
            }
        }

    }

}
