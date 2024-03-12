package game.components;

import java.util.Vector;

import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import game.engine.KeyListener;

/**
 * PlayerController
 */
public class PlayerController extends Component {
    public float walkSpeed = 0.05f;
    public float slowDownForc = 0.002f;
    public Vector2f terminalVelocity = new Vector2f(0.07f, 0.07f);

    private transient StateMachine stateMachine;

    private transient Vector2f acceleraton = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient float playerWidth = 1;

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);

    }

    @Override
    public void update(float dt) {
        this.acceleraton.zero();

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

        if(this.acceleraton.x > 0 && velocity.x > 0) {
            this.stateMachine.trigger("StartRunRight");
        } else if(this.acceleraton.x < 0 && velocity.x < 0) {
            this.stateMachine.trigger("StartRunLeft");
        } else
        if(this.acceleraton.y > 0 && velocity.y > 0) {
            this.stateMachine.trigger("StartRunUp");
        } else if(this.acceleraton.y < 0 && velocity.y < 0) {
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
    }

}
