package game.components;

import org.joml.Vector4f;

/**
 * Item
 */
public class Item extends Component {

    @Override
    public void gameStart() {
        System.out.println("GameStart item" + gameObject.name);
        this.getGameObject().getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 0, 0, 0));
        this.gameObject.transform.zIndex = 100;
    }

    public void show() {
        System.out.println("show item " + gameObject.name);
        this.getGameObject().getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
    }

    public void hide() {
        System.out.println("Hide Imte" + gameObject.name);
        this.getGameObject().getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 0, 0, 0));
    }

}
