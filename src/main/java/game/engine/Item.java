package  game.engine;

/**
 * Item
 */
public class Item {
	private static int ID_COUNTER = 0;
	private int uid = -1;

    public void use() {}
    public void generateID() {
        if (this.uid == -1) {
            this.uid = ID_COUNTER++;
        }
    }

}
