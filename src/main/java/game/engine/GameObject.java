package game.engine;

import java.util.ArrayList;
import java.util.List;

import game.components.Component;
import imgui.ImGui;

public class GameObject {
	private boolean isActiveGameObject = false;
	private boolean doSerialization = true;
	private boolean isDead = false;

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

	public String name;
	private List<Component> components;


	public transient Transform transform;

	private int uid = -1;

	public GameObject(String name) {
		this.name = name;
		this.components = new ArrayList<>();

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
            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.imgui();
        }
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

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public List<Component> getAllComponents(){
		return this.components;
	}

	public void setActiveGameObject(boolean state) {
		this.isActiveGameObject = state;
	}

	public boolean getIsActiveGameObject() {
		return isActiveGameObject;
	}

	public void setNoSerialize() {
		this.doSerialization = false;
	}

	public boolean doSerialization() {
		return this.doSerialization;
	}

	public boolean isDead() {
		return this.isDead;
	}

    public void destroy() {
		this.isDead = true;
		for (int i=0; i < components.size(); i++) {
			components.get(i).destroy();
		}
    }
    public void editorUpdate(float dt) {
		for (int i=0; i < components.size(); i++){
			components.get(i).editorUpdate(dt);
		}
    }
}
