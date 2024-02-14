package game.engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
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
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
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
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import game.renderer.DebugDraw;
import game.renderer.*;
import game.renderer.Framebuffer;
import game.renderer.PickingTexture;
import game.renderer.Shader;
import game.scene.LevelEditorScene;
import game.scene.LevelScene;
import game.scene.Scene;

import game.util.*;

public class Window {
	private static Window window = null;

	private Framebuffer framebuffer;
	private Framebuffer entityIdFramebuffer;
	private PickingTexture pickingTexture;

	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window();
		}

		return Window.window;

	}

	/**
	 * Aktiviere Scene Nr. n
	 * 
	 * @param newScene
	 */
	public static void changeScene(int newScene) {
		switch (newScene) {
			case 0:
				currenScene = new LevelEditorScene();
				break;
			case 1:
				currenScene = new LevelScene();
				break;
			default:
				assert false : "Unknown Scenen '" + newScene + "'";
				break;
		}

		currenScene.load();
		currenScene.init();
		currenScene.start();

	}

	public static int getWidth() {
		return get().width[0];
	}

	public static int getHeight() {
		return get().height[0];
	}

	public static void setWidth(int newWidth) {
		get().width[0] = newWidth;
	}

	public static void setHeight(int newHeight) {
		get().height[0] = newHeight;
	}

	public static Window getWindow() {
		return window;
	}

	public static void setWindow(Window window) {
		Window.window = window;
	}

	public static Scene getScene() {
		return currenScene;
	}

	public static void setCurrenScene(Scene currenScene) {
		Window.currenScene = currenScene;
	}

	private int[] width = new int[1];
	private int[] height = new int[1];

	private String title;

	private long glfwWindow;

	private static Scene currenScene;

	public float r, g, b, a;

	private boolean fadeToBlack = false;

	private ImGuiLayer imguiLayer;

	private Window() {
		this.height[0] = 1080;
		this.width[0] = 1920;
		this.title = "Hello World";

		this.r = 1;
		this.g = 1;
		this.b = 1;
		this.a = 1;

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
		// glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		// Create Window

		glfwWindow = glfwCreateWindow(this.width[0], this.height[0], this.title, NULL, NULL);
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
			getScene().camera().adjustProjection();

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

		// make vars for x and y
		this.entityIdFramebuffer = new Framebuffer(2560, 1600);
		this.framebuffer = new Framebuffer(2560, 1600);
		this.pickingTexture = new PickingTexture(2560, 1660);

		glViewport(0, 0, 2560, 1600);

		Window.changeScene(0);

		glfwGetWindowSize(glfwWindow, width, height);
	}


	public void loop() {
		getScene().camera().adjustProjection();
		float beginTime = (float) glfwGetTime();
		float endTime;
		float dt = -1.0f;

		Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
		Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

		while (!glfwWindowShouldClose(glfwWindow)) {
			// Poll Events
			glfwPollEvents();

			// Render Pass 1 Render to pick id
			glDisable(GL_BLEND);

			// this.entityIdFramebuffer.bind();
			pickingTexture.enableWriting();

			glViewport(0, 0, 2560, 1600);
			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			Renderer.bindShader(pickingShader);
			currenScene.render();
			// this.entityIdFramebuffer.unbind();
			pickingTexture.disableWriting();

			if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
				int x = (int)MouseListener.getScreenX();
				int y = (int)MouseListener.getScreenY();

				System.out.println("X: " + x);
				System.out.println("Y: " + y);
				int pixel = pickingTexture.readPixel(x, y);
				System.out.println(pixel);
			}

			glEnable(GL_BLEND);

			// Redner Pass 2 Actual Render
			DebugDraw.beginFrame();


			this.framebuffer.bind();

			glClearColor(r, g, b, a);
			glClear(GL_COLOR_BUFFER_BIT);

			if (dt >= 0) {
                Renderer.bindShader(defaultShader);
				currenScene.update(dt);
				currenScene.render();

				DebugDraw.draw();
			}
			this.framebuffer.unbind();

			this.imguiLayer.update(dt, currenScene);

			glfwSwapBuffers(glfwWindow);

			endTime = (float) glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;

		}

		currenScene.saveExit();
	}

	public String getTitle() {
		return title;
	}

	public long getGlfwWindow() {
		return glfwWindow;
	}

	public boolean isFadeToBlack() {
		return fadeToBlack;
	}

	public void setFadeToBlack(boolean fadeToBlack) {
		this.fadeToBlack = fadeToBlack;
	}

	public ImGuiLayer getImguiLayer() {
		return imguiLayer;
	}

	public void setImguiLayer(ImGuiLayer imguiLayer) {
		this.imguiLayer = imguiLayer;
	}

	public static Framebuffer getFramebuffer() {
		return get().framebuffer;
		// return get().entityIdFramebuffer;
	}

	public static Scene getCurrenScene() {
		return currenScene;
	}

    public static float getTargetAspectRatio() {
        return getCurrenScene().camera().getAspectRation();
    }
}
