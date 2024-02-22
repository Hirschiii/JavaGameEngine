package game.engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.util.Arrays;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MouseListener {
	private static MouseListener instance;
	private double scrollX, scrollY;
	private double xPos, yPos, lastY, lastX;
	private boolean mouseButtonPressed[] = new boolean[9];
	private boolean isDragging;

    private int mouseButtonDown = 0;

	private Vector2f gameViewportPos = new Vector2f();
	private Vector2f gameViewportSize = new Vector2f();

	private MouseListener() {
		this.scrollX = 0.0;
		this.scrollY = 0.0;
		this.xPos = 0.0;
		this.yPos = 0.0;
		this.lastX = 0.0;
		this.lastY = 0.0;

		// this.gameViewportSize = new Vector2f(Window.getWidth(), Window.getHeight());
		// this.gameViewportPos = new Vector2f(0, 0);
	}

    public static void clear() {
        get().scrollX = 0.0;
        get().scrollY = 0.0;
        get().xPos = 0.0;
        get().yPos = 0.0;
        get().lastX = 0.0;
        get().lastY = 0.0;
        get().mouseButtonDown = 0;
        get().isDragging = false;
        Arrays.fill(get().mouseButtonPressed, false);
    }

	public static MouseListener get() {
		if (MouseListener.instance == null) {
			MouseListener.instance = new MouseListener();

		}

		return MouseListener.instance;
	}

	public static void mousePosCallback(long window, double xPos, double yPos) {
		// System.out.println("set xPos: "+ xPos);
		// System.out.println("set yPos: "+ yPos);
		get().lastX = get().xPos;
		get().lastY = get().yPos;

		get().xPos = xPos;
		get().yPos = yPos;
		get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
	}

	public static void mouseButtonCallback(long window, int button, int action, int mod) {
		if (action == GLFW_PRESS) {
			if (button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = true;
			}
		} else if (action == GLFW_RELEASE) {
			if (button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = false;
				get().isDragging = false;
			}
		}
	}

	public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
		get().scrollX = xOffset;
		get().scrollY = yOffset;
	}

	public static void endFrame() {
		get().scrollX = 0;
		get().scrollY = 0;
	}

	public static float getX() {
		return (float) get().xPos;
	}

	public static float getY() {
		return (float) get().yPos;
	}

	public static float getDx() {
		return (float) (get().lastX - get().xPos);
	}

	public static MouseListener getInstance() {
		return instance;
	}

	public static void setInstance(MouseListener instance) {
		MouseListener.instance = instance;
	}

	public void setScrollX(double scrollX) {
		this.scrollX = scrollX;
	}

	public void setScrollY(double scrollY) {
		this.scrollY = scrollY;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public double getLastY() {
		return lastY;
	}

	public void setLastY(double lastY) {
		this.lastY = lastY;
	}

	public double getLastX() {
		return lastX;
	}

	public void setLastX(double lastX) {
		this.lastX = lastX;
	}

	public boolean[] getMouseButtonPressed() {
		return mouseButtonPressed;
	}

	public void setMouseButtonPressed(boolean[] mouseButtonPressed) {
		this.mouseButtonPressed = mouseButtonPressed;
	}

	public void setDragging(boolean isDragging) {
		this.isDragging = isDragging;
	}

	public static float getDy() {
		return (float) (get().lastY - get().yPos);
	}

	public static float getScrollX() {
		return (float) get().scrollX;
	}

	public static float getScrollY() {
		return (float) get().scrollY;
	}

	public static boolean isDragging() {
		return (boolean) get().isDragging;
	}

	public static boolean mouseButtonDown(int button) {
		if (button < get().mouseButtonPressed.length) {
			return get().mouseButtonPressed[button];
		} else {
			return false;
		}
	}

	public static void setGameViewportPos(Vector2f gameViewportPos) {
		// System.out.println("SetGameViewportPos: " + gameViewportPos);
		get().gameViewportPos.set(gameViewportPos);
	}

	public static void setGameViewportSize(Vector2f gameViewportSize) {
		get().gameViewportSize.set(gameViewportSize);
	}

	public static float getScreenX() {
		return getScreen().x;
	}

	public static float getScreenY() {
		return getScreen().y;
	}

	public static Vector2f getScreen() {
		float currentX = (getX() - get().gameViewportPos.x);
		currentX = (currentX / get().gameViewportSize.x) * 2560;

		float currentY = (get().gameViewportPos.y - getY());
		currentY = (currentY / get().gameViewportSize.y) * 1600;

		return new Vector2f(currentX, currentY);
	}


	public static float getWorldX() {
		return getWorld().x;
	}

	public static float getWorldY() {
		return getWorld().y;
	}

	public static Vector2f getWorld() {
		float currentX = getX() - get().gameViewportPos.x;
		currentX = (2.0f * (currentX / get().gameViewportSize.x)) - 1.0f;

		float currentY = (get().gameViewportPos.y - getY());
		currentY = (2.0f * (currentY / get().gameViewportSize.y)) - 1;

		Camera camera = Window.getScene().camera();
		Vector4f tmp = new Vector4f(currentX, currentY, 0, 1);
		Matrix4f inverseView = new Matrix4f(camera.getInverseView());
		Matrix4f inverseProjection = new Matrix4f(camera.getInverseProjection());
		tmp.mul(inverseView.mul(inverseProjection));

		return new Vector2f(tmp.x, tmp.y);
	}

}
