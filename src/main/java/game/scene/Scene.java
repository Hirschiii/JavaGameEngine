package game.scene;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import game.components.Component;
import game.components.ComponentDeserializer;
import game.engine.Camera;
import game.engine.GameObject;
import game.engine.GameObjectDeserializer;
import game.renderer.Renderer;
import imgui.ImGui;

public abstract class Scene {

	protected Renderer renderer = new Renderer();
	protected Camera camera;
	protected boolean loadedLevel = false;

	private boolean isRunning = false;

	protected List<GameObject> gameObjects = new ArrayList<>();
	protected GameObject activeGameObject = null;

	public Scene() {
	}

	public void init() {
	}

	public void start() {
		for (GameObject go : gameObjects) {
			go.start();
			this.renderer.add(go);
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
		}

	}

	public abstract void update(float dt);
	public abstract void render();

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

	public void sceneImgui() {
		if (activeGameObject != null) {
			ImGui.begin("Inspector");
			activeGameObject.imgui();
			ImGui.end();
		}

		imgui();

	}

	/**
	 * Expose Fields (Vars) to Dear Im GUI
	 * 
	 */
	public void imgui() {
	}

	public void saveExit() {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.registerTypeAdapter(Component.class, new ComponentDeserializer())
				.registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
				.create();

		try {
			FileWriter writer = new FileWriter("level.json");
			writer.write(gson.toJson(this.gameObjects));
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
				.create();
		String inFile = "";

		try {
			inFile = new String(Files.readAllBytes(Paths.get("level.json")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!inFile.equals("")) {
			int maxGoID = -1;
			int maxComID = -1;
			GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
			for (int i = 0; i < objs.length; i++) {
				addGameObject(objs[i]);
				for (Component c : objs[i].getAllComponents()){
					if(c.getUid() > maxComID) {
						maxComID = c.getUid();
					}
				}

				if(objs[i].getUid() > maxGoID) {
					maxGoID = objs[i].getUid();
				}
			}

			maxGoID++;
			maxComID++;

			System.out.println(maxGoID);
			System.out.println(maxComID);

			GameObject.init(maxGoID);
			Component.init(maxComID);
			this.loadedLevel = true;
		}
	}

	public void setActiveGameObject(int gameObjectId) {
		this.activeGameObject = getGameObject(gameObjectId);
	}

}
