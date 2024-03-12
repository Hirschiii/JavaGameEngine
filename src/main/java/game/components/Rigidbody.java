package game.components;

import java.util.Vector;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import game.renderer.DebugDraw;

public class Rigidbody extends Component {
    private transient Vector2f localCenter = new Vector2f(0f, 0f);
    private Vector2f globalCenter;
    private transient Vector2f dimension = new Vector2f(1f, 1);
    private Vector2f boxStart, boxEnd;

    public boolean collisionBox(Vector2f colStart, Vector2f colEnd) {

        return true;
    }

    @Override
    public void update(float dt) {
        this.globalCenter = new Vector2f(gameObject.transform.position.x + localCenter.x,
                gameObject.transform.position.y + localCenter.y);
        this.boxStart = new Vector2f(globalCenter.x - (dimension.x / 2), globalCenter.y - (dimension.y / 2));
        this.boxEnd = new Vector2f(globalCenter.x + (dimension.x / 2), globalCenter.y + (dimension.y / 2));
    }

    @Override
    public void editorUpdate(float dt) {
        this.globalCenter = new Vector2f(gameObject.transform.position.x + localCenter.x,
                gameObject.transform.position.y + localCenter.y);
        this.boxStart = new Vector2f(globalCenter.x - (dimension.x / 2), globalCenter.y - (dimension.y / 2));
        this.boxEnd = new Vector2f(globalCenter.x + (dimension.x / 2), globalCenter.y + (dimension.y / 2));
        DebugDraw.addLine2D(boxStart, boxEnd, new Vector3f(1, 0, 0));
        DebugDraw.addBox2D(globalCenter, dimension, 0, new Vector3f(1, 0, 0));
    }
}
