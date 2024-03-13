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
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBlitFramebuffer;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.joml.Vector4f;

import static game.util.Settings.*;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import game.renderer.DebugDraw;
import game.observers.EventSystem;
import game.observers.Observer;
import game.observers.events.Event;
import game.renderer.*;
import game.renderer.Framebuffer;
import game.renderer.PickingTexture;
import game.renderer.Shader;
import game.scene.LevelEditorSceneInitializer;
import game.scene.LevelSceneInitializer;
import game.scene.Scene;
import game.scene.SceneInitializer;
import game.util.*;

public class Window implements Observer {
    private static Window window = null;

    // TODO Rename framebuffers
    private Framebuffer framebuffer_before;
    private Framebuffer framebuffer_ShaderApplied;
    private Framebuffer entityIdFramebuffer;
    private PickingTexture pickingTexture;

    private static float startingTime;

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;

    }

    /**
     * Aktiviere Scene Nr. n
     * windowja
     *
     * @param newScene
     */
    public static void changeScene(SceneInitializer sceneInitializer) {
        if (currenScene != null) {
            currenScene.destroy();
        }

        getImguiLayer().getPropertiesWindow().setActiveGameObject(null);
        currenScene = new Scene(sceneInitializer);
        currenScene.load();
        currenScene.init();
        currenScene.start();
    }

    public static int getWidth() {
        return 1920; //get().width[0];
    }

    public static int getHeight() {
        return 1080; //get().height[0];
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
    private boolean runntimePlaying = Settings.RELEASE_BUILD;

    public float r, g, b, a;

    private boolean fadeToBlack = false;

    private ImGuiLayer imguiLayer;

    private Window() {
        this.height[0] = 1080;
        this.width[0] = 1920;
        this.title = "The Last Package";
        EventSystem.addObserver(this);
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

        // make vars for x and y
        System.out.println("Height: " + height[0]);
        System.out.println("Width:  " + width[0]);

        // this.height[0] = 1080;
        // this.width[0] = 1920;
        // this.framebuffer_before = new Framebuffer(2560, 1600);
        this.framebuffer_before = new Framebuffer(width[0], height[0]);
        this.framebuffer_ShaderApplied = new Framebuffer(width[0], height[0]);
        this.pickingTexture = new PickingTexture(width[0], height[0]);

        this.imguiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imguiLayer.initImGui();

        // glViewport(0, 0, 2560, 1600);
        glViewport(0, 0, width[0], height[0]);

        Window.changeScene(new LevelEditorSceneInitializer());

        glfwGetWindowSize(glfwWindow, width, height);
    }

    public void loop() {
        getScene().camera().adjustProjection();
        float beginTime = (float) glfwGetTime();
        startingTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        // Shader vhsShader = AssetPool.getShader("assets/shaders/vhs.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll Events
            glfwPollEvents();

            // Render Pass 1 Render to pick id
            glDisable(GL_BLEND);

            // this.entityIdFramebuffer.bind();
            pickingTexture.enableWriting();

            Renderer.bindShader(pickingShader);
            currenScene.render();
            // this.entityIdFramebuffer.unbind();
            pickingTexture.disableWriting();

            glEnable(GL_BLEND);

            // Redner Pass 2 Actual Render
            DebugDraw.beginFrame();

            this.framebuffer_before.bind();
            Vector4f clearColor = currenScene.camera().clearColor;
            glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
            glClear(GL_COLOR_BUFFER_BIT);

            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                Renderer.bindShader(defaultShader);
                if (runntimePlaying) {
                    currenScene.update(dt);
                } else {
                    currenScene.editorUpdate(dt);
                }
                currenScene.render();

                DebugDraw.draw();
            }
            this.framebuffer_before.unbind();

            this.framebuffer_ShaderApplied.bind();
            glBindFramebuffer(GL_READ_FRAMEBUFFER, framebuffer_before.getFboID());
            glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
            glBlitFramebuffer(0, 0, framebuffer_before.width, framebuffer_before.height, 0, 0, this.width[0],
                    this.height[0],
                    GL_COLOR_BUFFER_BIT, GL_NEAREST);
            this.framebuffer_ShaderApplied.unbind();

            if (RELEASE_BUILD) {
                // NOTE: This is the most complicated piece for release builds. In release
                // builds
                // we want to just blit the framebuffer to the main window so we can see the
                // game
                //
                // In non-release builds, we usually draw the framebuffer to an ImGui component
                // as an image.
                glBindFramebuffer(GL_READ_FRAMEBUFFER, framebuffer_before.getFboID());
                glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
                glBlitFramebuffer(0, 0, framebuffer_before.width, framebuffer_before.height, 0, 0, this.width[0],
                        this.height[0],
                        GL_COLOR_BUFFER_BIT, GL_NEAREST);
            } else {
                this.imguiLayer.update(dt, currenScene);
            }

            // this.framebuffer_ShaderApplied.bind();
            //
            // glClearColor(1, 1, 1, 1);
            // glClear(GL_COLOR_BUFFER_BIT);
            // Renderer.bindShader(vhsShader);
            // // currenScene.render();
            // this.framebuffer_before.renderToScreen();
            //
            // this.framebuffer_ShaderApplied.unbind();
            //
            // this.imguiLayer.update(dt, currenScene);

            glfwSwapBuffers(glfwWindow);
            MouseListener.endFrame();
            KeyListener.endFrame();

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;

        }

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

    public static ImGuiLayer getImguiLayer() {
        return get().imguiLayer;
    }

    public void setImguiLayer(ImGuiLayer imguiLayer) {
        this.imguiLayer = imguiLayer;
    }

    public static Framebuffer getFramebuffer() {
        return get().framebuffer_before;
        // return get().entityIdFramebuffer;
        // return get().framebuffer_ShaderApplied;
    }

    public static Scene getCurrenScene() {
        return currenScene;
    }

    public static float getTargetAspectRatio() {
        return getCurrenScene().camera().getAspectRation();
    }

    public static float getCurrentTime() {
        float currenTime = (float) glfwGetTime();
        return currenTime - startingTime;
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type) {
            case GameEngineStartPlay:
                this.runntimePlaying = true;
                currenScene.save();
                Window.changeScene(new LevelSceneInitializer());
                break;
            case GameEngineStopPlay:
                this.runntimePlaying = false;
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case LoadLevel:
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case SaveLevel:
                Window.getScene().save();
                break;
            case UserEvent:
                break;
        }
    }
}
