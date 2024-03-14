package game.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
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
public class PlayerController extends Component {
    public float walkSpeed = 0.14f;
    public float slowDownForc = 0.002f;
    public Vector2f terminalVelocity = new Vector2f(0.07f, 0.07f);

    private transient StateMachine stateMachine;

    private transient Vector2f acceleraton = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private List<GameObject> interactiveGOs = new ArrayList<>();

    private GameObject interactiveGO = null;

    private boolean collided = false;

    private float debounceTime = 0.2f;
    private float debounce = debounceTime;

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
    }

    @Override
    public void update(float dt) {
        debounce -= dt;
        this.collided = false;
        this.acceleraton.zero();

        if (KeyListener.isKeyPressed(GLFW_KEY_E)) {
            if (interactiveGO != null) {
                interactiveGO.getComponent(Interaktive.class).interact(this.gameObject);
            }
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_TAB) && debounce < 0) {
            debounce = debounceTime;
            if (interactiveGO != null && interactiveGOs.size() > 1) {
                setInteraktiveGO();
            }
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

        List<GameObject> oldIntGos = interactiveGOs;

        interactiveGOs = getNearGOS(2);
        for (int i = 0; i < interactiveGOs.size(); i++) {
            GameObject go = interactiveGOs.get(i);
            if (go.getComponent(Interaktive.class) == null) {
                interactiveGOs.remove(i);
                i--;
            }
            if (go.getComponent(Rigidbody.class) != null) {
                this.gameObject.getComponent(Rigidbody.class).update(dt);
                collided = go.getComponent(Rigidbody.class).collisionBox(this.gameObject);
                if (collided) {
                    go.getComponent(SpriteRenderer.class).setColor(new Vector4f(1, 0, 0, 1));
                    break;
                }
            }
        }

        for (GameObject go : oldIntGos) {
            if (!interactiveGOs.contains(go)) {
                if (interactiveGO.getComponent(Interaktive.class) != null) {
                    System.out.println("Left The Chat");
                    interactiveGO.getComponent(Interaktive.class).setInteractive(false);
                    interactiveGO = null;
                }
            }
        }

        if (interactiveGO == null && interactiveGOs.size() > 0) {
            setInteraktiveGO();
        }
        if (collided) {
            this.gameObject.transform.position.sub(this.velocity);
            this.acceleraton.zero();
            this.velocity.zero();
            this.stateMachine.trigger("StopRun");
        }
    }

    private void setInteraktiveGO() {
        System.out.println("Set interakt");
        if (this.interactiveGO == null) {
            this.interactiveGO = interactiveGOs.getFirst();
        } else {
            if (interactiveGO.getComponent(Interaktive.class) != null) {
                System.out.println("Befor setting new set false");
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
                System.out.println("afet setting new set true");
            interactiveGO.getComponent(Interaktive.class).setInteractive(true);
        }
        System.out.println(interactiveGO.name);
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
