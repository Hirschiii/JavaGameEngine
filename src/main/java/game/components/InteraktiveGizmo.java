package game.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import game.engine.GameObject;
import game.engine.Prefabs;
import game.engine.Window;

/**
 * InteraktiveGizmo
 */
public class InteraktiveGizmo extends Component {
    private boolean active = false;
    private GameObject gizmo;
    private SpriteRenderer gizmoSprite;
    private Vector2f offset = new Vector2f(0.5f, 0.5f);

    public InteraktiveGizmo(Sprite sprite) {
        this.gizmo = Prefabs.generateSpriteObject(sprite, 0.5f, 0.5f);
        this.gizmo.setNoSerialize();
        this.gizmoSprite = this.gizmo.getComponent(SpriteRenderer.class);
        Window.getScene().addGameObject(this.gizmo);
    }

    @Override
    public void update(float dt) {
        if (gameObject.getComponent(Interaktive.class) != null) {
            if (gameObject.getComponent(Interaktive.class).getInteractive() && !active) {
                System.out.println("SetActive");
                setActive();
            } else if (!gameObject.getComponent(Interaktive.class).getInteractive() && active){
                System.out.println("SetInakice");
                setInactive();
            }
        }
    }

    private void setInactive() {
        this.active = false;
        this.gizmoSprite.setColor(new Vector4f(0f, 0f, 0f, 0f));
    }

    private void setActive() {
        this.active = true;
        this.gameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 0, 1, 1));
        this.gizmoSprite.setColor(new Vector4f(1f, 1f, 1f, 1f));
        this.gizmo.transform.position = new Vector2f(
                gameObject.transform.position.x + offset.x,
                gameObject.transform.position.y + offset.y);
    }

}
