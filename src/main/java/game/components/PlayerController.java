package game.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import game.engine.GameObject;
import game.engine.KeyListener;
import game.engine.Window;
import game.renderer.DebugDraw;
import game.util.Settings;
import net.sourceforge.plantuml.sequencediagram.Newpage;

/**
 * PlayerController
 */
public class PlayerController extends Component {
    public float walkSpeed = 0.14f;
    public float slowDownForc = 0.002f;
    public Vector2f terminalVelocity = new Vector2f(0.07f, 0.07f);

    private transient StateMachine stateMachine;

    private transient Vector2f acceleraton = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient float playerWidth = 1;
    private List<GameObject> interactiveGOs = new ArrayList<>();

    private GameObject interactiveGO = null;

    private boolean collided = false;
    private boolean interact = false;

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
    }

    @Override
    public void update(float dt) {
        this.collided = false;
        this.acceleraton.zero();

        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
            interactiveGO.getComponent(Interaktive.class).interact(this.gameObject);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            this.acceleraton.x = walkSpeed;
            this.stateMachine.trigger("SwitchDirection");
        } else if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            this.acceleraton.x = -walkSpeed;
            this.stateMachine.trigger("SwitchDirection");
        } else if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            this.acceleraton.y = walkSpeed;
            this.stateMachine.trigger("SwitchDirection");
        } else if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            this.acceleraton.y = -walkSpeed;
            this.stateMachine.trigger("SwitchDirection");
        }

        if (this.acceleraton.x > 0 && velocity.x > 0) {
            this.stateMachine.trigger("StartRunRight");
        } else if (this.acceleraton.x < 0 && velocity.x < 0) {
            this.stateMachine.trigger("StartRunLeft");
        } else if (this.acceleraton.y > 0 && velocity.y > 0) {
            this.stateMachine.trigger("StartRunUp");
        } else if (this.acceleraton.y < 0 && velocity.y < 0) {
            this.stateMachine.trigger("StartRunDown");
        }

        if (this.acceleraton.x == 0 || this.acceleraton.y == 0) {
            if (this.acceleraton.x == 0) {
                if (this.velocity.x > 0) {
                    this.velocity.x = Math.max(0, this.velocity.x - slowDownForc);
                } else if (this.velocity.x < 0) {
                    this.velocity.x = Math.min(0, this.velocity.x + slowDownForc);
                }
            }
            if (this.acceleraton.y == 0) {

                if (this.velocity.y > 0) {
                    this.velocity.y = Math.max(0, this.velocity.y - slowDownForc);
                } else if (this.velocity.y < 0) {
                    this.velocity.y = Math.min(0, this.velocity.y + slowDownForc);
                }
            }

            if (this.velocity.x == 0 && this.velocity.y == 0) {
                this.stateMachine.trigger("StopRun");
            }
        }

        this.velocity.x += this.acceleraton.x * dt;
        this.velocity.y += this.acceleraton.y * dt;

        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);

        this.gameObject.transform.position.add(this.velocity);

        interactiveGOs = getNearGOS(2);
        for (GameObject go : interactiveGOs) {
            if (go.getComponent(Rigidbody.class) != null) {
                this.gameObject.getComponent(Rigidbody.class).update(dt);
                collided = go.getComponent(Rigidbody.class).collisionBox(this.gameObject);
                if (collided) {
                    go.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 0, 0, 1));
                    break;
                }
            }
        }

        if (collided) {
            this.gameObject.transform.position.sub(this.velocity);
            this.acceleraton.zero();
            this.velocity.zero();
            this.stateMachine.trigger("StopRun");
        }
    }

    private void setInteraktiveGO() {
        // If null select the first, else select the next
    }

    private List<GameObject> getNearGOS(int range) {
        List<GameObject> nearGos = new ArrayList<>();
        for (GameObject go : Window.getScene().getGameObjects()) {
            if (go.getUid() != this.gameObject.getUid()) {
                if ((go.transform.position.x - this.gameObject.transform.position.x) < range &&
                        (this.gameObject.transform.position.x - go.transform.position.x) < range &&
                        (go.transform.position.y - this.gameObject.transform.position.y) < range &&
                        (this.gameObject.transform.position.y - go.transform.position.y) < range) {
                    if (go.getComponent(SpriteRenderer.class) != null) {
                        nearGos.add(go);
                        if (Settings.DEBUG_DRAW >= 2) {
                            go.getComponent(SpriteRenderer.class).setColor(new Vector4f(0, 1, 0, 1));
                        }
                    }
                } else {
                    if (Settings.DEBUG_DRAW >= 2) {
                        if (go.getComponent(SpriteRenderer.class) != null) {
                            go.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 1, 1, 1));
                        }
                    }
                }
            }
        }
        return nearGos;
    }

}
