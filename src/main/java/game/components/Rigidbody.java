package game.components;

import java.util.Vector;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import game.engine.GameObject;
import game.renderer.DebugDraw;

public class Rigidbody extends Component {
    private  Vector2f localCenter;
    public Vector2f globalCenter;
    public  Vector2f dimension;
    private Vector2f boxStart, boxEnd;

    public Rigidbody(Vector2f center, Vector2f dimension) {
        this.localCenter = center;
        this.dimension = dimension;
    }

    public boolean collisionBox(GameObject go) {
        if (go.getComponent(Rigidbody.class) != null) {
            Vector2f go_gCenter = go.getComponent(Rigidbody.class).globalCenter;
            Vector2f go_dimension = go.getComponent(Rigidbody.class).dimension;
            boolean x_overlap = (go_gCenter.x - (go_dimension.x / 2) < this.globalCenter.x + (this.dimension.x / 2) &&
                    go_gCenter.x + (go_dimension.x / 2) > this.globalCenter.x - (this.dimension.x / 2));

            boolean y_overlap = (go_gCenter.y - (go_dimension.y / 2) < this.globalCenter.y + (this.dimension.y / 2) &&
                    go_gCenter.y + (go_dimension.y / 2) > this.globalCenter.y - (this.dimension.y / 2));
            return x_overlap && y_overlap;
        } else {
            return false;
        }
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
