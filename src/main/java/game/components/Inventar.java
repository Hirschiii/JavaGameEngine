package game.components;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.jsondiagram.Arrow;

/**
 * Inventar
 */
public class Inventar extends Component{
    private List<Item> items;

    public Inventar() {
        this.items = new ArrayList<>();
    }


    public void addItem(Item item) {
        System.out.println("Item ID: " + item.uid);
        items.add(item);
    }
    public void removeItem(Item item) {
        items.remove(item);
    }
    public boolean doesHasItem(Item item) {
        return items.contains(item);
    }
}
