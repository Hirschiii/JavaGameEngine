package game.components;

/**
 * Item
 */
public class Item {
    public static int uid_counter = 0;
    public int uid;


    public Item() {
        this.uid = uid_counter++;
    }

}
