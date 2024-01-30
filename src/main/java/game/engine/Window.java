package game.engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class Window {
	private int width, height;
	private String title;

	private long glfwWindow;

	private static Window window = null;

	private static Scene currenScene;

	public float r, g, b, a;
	private boolean fadeToBlack = false;

	private ImGuiLayer imguiLayer;

	private Window() {
		this.width = 1920;
		this.height = 1080;
		this.title = "Hello World";

		this.r = 0;
		this.g = 0;
		this.b = 0;
		this.a = 1;

	}

	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window();
		}

		return Window.window;

	}

	public static Scene getScene() {
		return get().currenScene;
	}

	public static void changeScene(int newScene) {
		switch (newScene) {
			case 0:
				currenScene = new LevelEditorScene();
				currenScene.init();
				currenScene.start();
				break;
			case 1:
				currenScene = new LevelScene();
				currenScene.start();
				currenScene.init();
				break;
			case 2:
				currenScene = new InitMenuScene();
				currenScene.start();
				currenScene.init();
				break;
			default:
				assert false : "Unknown Scenen '" + newScene + "'";
				break;
		}
	}

	public void run() {
		System.out.println("Hello LWJGL: " + Version.getVersion() + "!");

		init();
		loop();

		// Free Memory

		glfwFreeCallbacks(glfwWindow);
		glfwDestroyWindow(glfwWindow);

		// Terminate GLFW and free the memory

		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();

		if (!glfwInit()) {
			throw new IllegalStateException("Unable to init GLFW");
		}

		// Configure GLFW

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		// Create Window

		glfwWindow = glfwCreateWindow(this.height, this.width, this.title, NULL, NULL);
		if (glfwWindow == NULL) {
			throw new IllegalStateException("Failed to create GLFW Window");
		}

		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
		glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
			Window.setWidth(newWidth);
			Window.setHeight(newHeight);
		});

		// Make OpenGL Context Current
		glfwMakeContextCurrent(glfwWindow);

		// Enable vsync
		glfwSwapInterval(1);

		// Make The window Visible
		glfwShowWindow(glfwWindow);

		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

		this.imguiLayer = new ImGuiLayer(glfwWindow);
		this.imguiLayer.initImGui();

		Window.changeScene(0);
	}

	public void loop() {
		float beginTime = (float) glfwGetTime();
		float endTime;
		float dt = -1.0f;

		while (!glfwWindowShouldClose(glfwWindow)) {
			// Poll Events
			glfwPollEvents();

			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT);


			if (dt >= 0) {
				currenScene.update(dt);
			}

			this.imguiLayer.update(dt, currenScene);

			glfwSwapBuffers(glfwWindow);

			endTime = (float) glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;

		}
	}

	public static int getWidth() {
		return get().width;
	}

	public static int getHeight() {
		return get().height;
	}

	public static void setWidth(int newWidth) {
		get().width = newWidth;
	}

	public static void setHeight(int newHeight) {
		get().width = newHeight;
	}
}
