package game.engine;

import java.util.ArrayList;
import java.util.List;

import game.components.Component;

public class GameObject {
	private static int ID_COUNTER = 0;
	public static void init(int maxID) {
		ID_COUNTER = maxID;
	}
	public static int getID_COUNTER() {
		return ID_COUNTER;
	}
	public static void setID_COUNTER(int iD_COUNTER) {
		ID_COUNTER = iD_COUNTER;
	}

	private String name;
	private List<Component> components;


	public Transform transform;

	private int zIndex;

	private int uid = -1;

	public GameObject(String name, Transform transform, int zIndex) {
		this.name = name;
		this.zIndex = zIndex;
		this.components = new ArrayList<>();
		this.transform = transform;

		this.uid = ID_COUNTER++;

	}

	public <T extends Component> T getComponent(Class<T> componentClass) {
		for (Component c : components) {
			if (componentClass.isAssignableFrom(c.getClass())) {
				try {
					return componentClass.cast(c);
				} catch (ClassCastException e) {
					e.printStackTrace();

					assert false : "Error: Casting component.";
				}
			}
		}

		return null;
	}

	public <T extends Component> void removeComponent(Class<T> componentClass) {
		for (int i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			if (componentClass.isAssignableFrom(c.getClass())) {
				components.remove(i);
				return;
			}
		}
	}

	public void addComponent(Component c) {
		c.generateID();
		this.components.add(c);
		c.gameObject = this;
	}

	public void update(float dt) {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).update(dt);
		}
	}

	public void start() {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).start();
		}
	}

	public void imgui() {
		for (Component c : components) {
			c.imgui();
		}
	}

	public int zIndex() {
		return this.zIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public int getzIndex() {
		return zIndex;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public List<Component> getAllComponents(){
		return this.components;
	}
}
