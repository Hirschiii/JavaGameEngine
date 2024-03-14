package game.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import game.engine.GameObject;
import game.engine.Prefabs;
import game.engine.Window;
import game.renderer.DebugDraw;

/**
 * InteraktiveGizmo
 */
public class InteraktiveGizmo extends Component {
    private GameObject activeGO;
    private boolean active = false;
    private SpriteRenderer gizmoSprite;
    private Vector2f offset = new Vector2f(0.5f, 0.5f);

    public InteraktiveGizmo() {
    }

    @Override
    public void update(float dt) {
        if (activeGO == null && active) {
            System.out.println("SetInakice");
            setInactive();
        }
    }

    public void setInactive() {
        this.gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0f, 0f, 0f, 0f));
        this.activeGO = null;
        this.active = false;
    }

    public void setActive(GameObject go) {
        this.active = true;
        this.activeGO = go;
        this.gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(1f, 1f, 1f, 1f));
        this.gameObject.transform.position = new Vector2f(
                this.activeGO.transform.position.x + offset.x,
                this.activeGO.transform.position.y + offset.y);
        DebugDraw.addBox2D(this.gameObject.transform.position, new Vector2f(0.5f, 0.5f), 0);
    }

}
