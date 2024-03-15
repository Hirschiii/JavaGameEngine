package game.scene;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joml.Vector2f;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import game.components.Component;
import game.components.ComponentDeserializer;
import game.components.SpriteRenderer;
import game.engine.Camera;
import game.engine.GameObject;
import game.engine.GameObjectDeserializer;
import game.engine.Item;
import game.engine.Transform;
import game.renderer.Renderer;
import game.util.Settings;
import imgui.ImGui;

public class Scene {

    private Renderer renderer = new Renderer();
    private Camera camera;

    private boolean isRunning;

    protected List<GameObject> gameObjects;

    private SceneInitializer sceneInitializer;

    public Scene(SceneInitializer sceneInitializer) {
        this.sceneInitializer = sceneInitializer;
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
    }

    public void init() {
        this.camera = new Camera(new Vector2f(0, 0));
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    public void start() {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            // Add here to physics
        }
        isRunning = true;
    }

    public void addGameObject(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            // Add here to physics
        }

    }

    public void editorUpdate(float dt) {
        this.camera.adjustProjection();
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.editorUpdate(dt);

            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                // this.physics.destryGameObject(go);
                i--;
            }
        }
    }

    public void update(float dt) {
        this.camera.adjustProjection();
        // Update Physics...
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.update(dt);

            if (go.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                // this.physics.destryGameObject(go);
                i--;
            }
        }
    };

    public void render() {
        this.renderer.render();

    };

    public Camera camera() {
        return this.camera;
    }

    public <T extends Component> GameObject getGameObjectWith(Class<T> clazz) {
        for (GameObject go : gameObjects) {
            if (go.getComponent(clazz) != null) {
                return go;
            }
        }

        return null;
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }
    public GameObject getGameObject(String gameObjectName) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.name.equals(gameObjectName))
                .findFirst();
        return result.orElse(null);
    }

    /**
     * Expose Fields (Vars) to Dear Im GUI
     *
     */
    public void imgui() {
        this.sceneInitializer.imgui();
    }
    public Item createItem(String name) {
        Item item = new Item(name);
        item.addComponent(new Transform());
        item.transform = item.getComponent(Transform.class);
        return item;
    }

    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
    }

    // public void save(String filepath) {
    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();

        try {
            FileWriter writer = new FileWriter(Settings.LEVEL_JSON_PATH);
            List<GameObject> objsToSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects) {
                if (obj.doSerialization()) {
                    objsToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objsToSerialize));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String inFile = "";

        try {
            inFile = new String(Files.readAllBytes(Paths.get(Settings.LEVEL_JSON_PATH)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {
            int maxGoID = -1;
            int maxComID = -1;
            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objs.length; i++) {
                addGameObject(objs[i]);
                for (Component c : objs[i].getAllComponents()) {
                    if (c.getUid() > maxComID) {
                        maxComID = c.getUid();
                    }
                }

                if (objs[i].getUid() > maxGoID) {
                    maxGoID = objs[i].getUid();
                }
            }

            maxGoID++;
            maxComID++;

            // System.out.println(maxGoID);
            // System.out.println(maxComID);

            GameObject.init(maxGoID);
            Component.init(maxComID);
        }
    }

    public void destroy() {
        for (GameObject go : gameObjects) {
            go.destroy();
        }
    }

}
