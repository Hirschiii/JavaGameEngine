package game.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_I;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_J;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_K;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_L;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector4f;

import game.engine.GameObject;
import game.engine.KeyListener;
import game.engine.Window;
import game.util.Settings;

/**
 * PlayerController
 */
public class PlayerController_two extends Component {
    private static final int GLFW_KEY_UP = 0;
    private static final int GLFW_KEY_RIGHT = 0;
    private static final int GLFW_KEY_LEFT = 0;
    private static final int GLFW_KEY_DONW = 0;
    public float walkSpeed = 0.14f;
    public float slowDownForc = 0.002f;
    public Vector2f terminalVelocity = new Vector2f(0.07f, 0.07f);

    private transient StateMachine stateMachine;

    private transient Vector2f acceleraton = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private List<GameObject> interactiveGOs = new ArrayList<>();

    private GameObject interactiveGO = null;
    private InteraktiveGizmo interactiveGOGizmo;

    private boolean collided = false;

    private float debounceTime = 0.2f;
    private float debounce = debounceTime;

    @Override
    public void start() {
        if(!Settings.PLAYER_TWO) {
            gameObject.destroy();
            return;
        }
        System.out.println("RunPlayerTwo");
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        GameObject gizmo_Interakt = Window.getCurrenScene().getGameObjectWith(InteraktiveGizmo.class);
        if (gizmo_Interakt != null) {
            if (gizmo_Interakt.getComponent(InteraktiveGizmo.class) != null) {
                this.interactiveGOGizmo = gizmo_Interakt.getComponent(InteraktiveGizmo.class);
            }
        }

    }

    @Override
    public void update(float dt) {
        debounce -= dt;
        this.collided = false;
        this.acceleraton.zero();


        if (KeyListener.isKeyPressed(GLFW_KEY_L)) {
            this.acceleraton.x = walkSpeed;
            this.stateMachine.trigger("SwitchDirection");
        } else if (KeyListener.isKeyPressed(GLFW_KEY_J)) {
            this.acceleraton.x = -walkSpeed;
            this.stateMachine.trigger("SwitchDirection");
        } else if (KeyListener.isKeyPressed(GLFW_KEY_I)) {
            this.acceleraton.y = walkSpeed;
            this.stateMachine.trigger("SwitchDirection");
        } else if (KeyListener.isKeyPressed(GLFW_KEY_K)) {
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

        for (GameObject go : interactiveGOs) {
            go.getComponent(Interaktive.class).setInteractive(false);
        }

        interactiveGOs.clear();

        List<GameObject> nearGOS = getNearGOS(2);
        for (int i = 0; i < nearGOS.size(); i++) {
            GameObject go = nearGOS.get(i);
            if (go.getComponent(Interaktive.class) != null) {
                go.getComponent(Interaktive.class).setInteractive(true);
                interactiveGOs.add(go);
            }
            if (go.getComponent(Rigidbody.class) != null) {
                this.gameObject.getComponent(Rigidbody.class).update(dt);
                collided = go.getComponent(Rigidbody.class).collisionBox(this.gameObject);
                if (collided) {
                    break;
                }
            }
        }

        if (interactiveGO == null && interactiveGOs.size() > 0) {
            setInteraktiveGO();
        } else if (!interactiveGOs.contains(interactiveGO) || interactiveGOs.size() <= 0) {
            interactiveGO = null;
            interactiveGOGizmo.setInactive();
        }
        if (collided) {
            this.gameObject.transform.position.sub(this.velocity);
            this.acceleraton.zero();
            this.velocity.zero();
            this.stateMachine.trigger("StopRun");
        }
    }

    private void setInteraktiveGO() {
        if (this.interactiveGO == null) {
            this.interactiveGO = interactiveGOs.getFirst();
        } else {
            if (interactiveGO.getComponent(Interaktive.class) != null) {
                interactiveGO.getComponent(Interaktive.class).setInteractive(false);
            }
            if (interactiveGOs.contains(interactiveGO)) {
                int index = interactiveGOs.indexOf(interactiveGO) + 1;
                if (index >= interactiveGOs.size()) {
                    interactiveGO = interactiveGOs.getFirst();
                } else {
                    interactiveGO = interactiveGOs.get(index);
                }
            } else {
                interactiveGO = interactiveGOs.getFirst();
            }
        }
        if (interactiveGO.getComponent(Interaktive.class) != null) {
            interactiveGO.getComponent(Interaktive.class).setInteractive(true);
            interactiveGOGizmo.setActive(interactiveGO);
        }
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
                    }
                }
            }
        }
        return nearGos;
    }

}
