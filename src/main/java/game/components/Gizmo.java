package game.components;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector2f;
import org.joml.Vector4f;

import game.editor.PropertiesWindow;
import game.engine.GameObject;
import game.engine.MouseListener;
import game.engine.Prefabs;
import game.engine.Window;

public class Gizmo extends Component {
    private boolean using = false;

    private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private Vector4f xAxisColorHover = new Vector4f(0.5f, 0, 0, 1);

    private Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
    private Vector4f yAxisColorHover = new Vector4f(0, 0.5f, 0, 1);

    private GameObject xAxisObject;
    private GameObject yAxisObject;

    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;

    protected GameObject activeGameObject = null;

    private Vector2f xAxisOffset = new Vector2f(0.75f, -0.75f);
    private Vector2f yAxisOffset = new Vector2f(-0.75f, 0.75f);

    private float gizmoWidth = 0.5f;
    private float gizmoHeight = ((48.0f / 16.0f) / 2);

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;

    private PropertiesWindow propertiesWindow;

    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.propertiesWindow = propertiesWindow;

        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);
        this.xAxisObject.addComponent(new NonPickable());
        this.yAxisObject.addComponent(new NonPickable());

        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);


        Window.getScene().addGameObject(this.xAxisObject);
        Window.getScene().addGameObject(this.yAxisObject);

    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
        this.yAxisObject.transform.rotation = 180;
        this.xAxisObject.transform.zIndex = 100;
        this.yAxisObject.transform.zIndex = 100;
        this.xAxisObject.setNoSerialize();
        this.yAxisObject.setNoSerialize();

    }

    @Override
    public void update(float dt) {
        if (using) {
            this.setInactive();
        }
        this.xAxisObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 0, 0, 0));
    }

    @Override
    public void editorUpdate(float dt) {
        if (!using) return;

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = true;
            yAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            yAxisActive = true;
            xAxisActive = false;
        } else {
            xAxisActive = false;
            yAxisActive = false;
        }

        if (this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisObject.transform.position.add(this.xAxisOffset);
            this.yAxisObject.transform.position.add(this.yAxisOffset);
        }
    }

    public void setActive() {
        this.xAxisSprite.setColor(this.xAxisColor);
        this.yAxisSprite.setColor(this.yAxisColor);
    }

    public void setInactive() {
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = MouseListener.getWorld();
        if (mousePos.x <= xAxisObject.transform.position.x + (gizmoHeight / 2) &&
                mousePos.x >= xAxisObject.transform.position.x - (gizmoHeight / 2) &&
                mousePos.y >= xAxisObject.transform.position.y - (gizmoWidth / 2) &&
                mousePos.y <= xAxisObject.transform.position.y + (gizmoWidth / 2)) {
            this.xAxisSprite.setColor(xAxisColorHover);
            return true;
        }
        this.xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState() {
        Vector2f mousePos = MouseListener.getWorld();
        if (mousePos.x <= yAxisObject.transform.position.x + (gizmoWidth / 2) &&
                mousePos.x >= yAxisObject.transform.position.x - (gizmoWidth / 2) &&
                mousePos.y <= yAxisObject.transform.position.y + (gizmoHeight / 2) &&
                mousePos.y >= yAxisObject.transform.position.y - (gizmoHeight / 2)) {
            this.yAxisSprite.setColor(yAxisColorHover);
            return true;
        }
        this.yAxisSprite.setColor(yAxisColor);
        return false;
    }

    public void setUsing() {
        this.using = true;
    }

    public void setNotUsing() {
        this.setInactive();
        this.using = false;
    }
}
