package game.components;

import game.engine.GameObject;

/**
 * Interaktive
 */
public abstract class Interaktive extends Component {
    public boolean canInterakt = false;

    public void interact(GameObject go) {
        if (canInterakt) {
            interaction(go);
            canInterakt = false;
        }
    }
    public abstract void interaction(GameObject go);

    public boolean isCanInterakt() {
        return canInterakt;
    }


    public void setCanInterakt(boolean canInterakt) {
        this.canInterakt = canInterakt;
    };



}
