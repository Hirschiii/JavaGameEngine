package game.engine;

import java.util.HashMap;
import java.util.List;

/**
 * Items
 */
public class Items {
    private List<GameObject> items;
    public HashMap<GameObject, GameObject> belongings;


    public void addItem(GameObject c) {
        items.add(c);
    }

    /**
     * @param item
     * @param from
     * @param to
     */
    public void changeBelong(GameObject item, GameObject from, GameObject to) {
        if(belongings.get(item).equals(from)) {
            belongings.put(item, to);
        }
    }

    public void setBelong(GameObject item, GameObject to) {
        if(belongings.get(item) != null) {
            belongings.put(item, to);
        }
    }
}
