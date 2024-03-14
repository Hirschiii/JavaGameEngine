package game.engine;

import java.util.HashMap;
import java.util.List;

/**
 * Items
 */
public class Items {
    private List<Item> items;
    public HashMap<Item, GameObject> belongings;


    public void addItem(Item c) {
        c.generateID();
        items.add(c);
    }

    /**
     * @param item
     * @param from
     * @param to
     */
    public void changeBelong(Item item, GameObject from, GameObject to) {
        if(belongings.get(item).equals(from)) {
            belongings.put(item, to);
        }
    }

    public void setBelong(Item item, GameObject to) {
        if(belongings.get(item) != null) {
            belongings.put(item, to);
        }
    }
}
