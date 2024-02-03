package game.engine;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import imgui.ImGui;

public abstract class Component {
	public transient GameObject gameObject = null;

	public void start() {
	}

	public void update(float dt) {

	}

	public void imgui() {
		try {
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field field : fields) {
				boolean isPrivat = Modifier.isPrivate(field.getModifiers());

				if (isPrivat) {
					field.setAccessible(true);
				}

				Class type = field.getType();
				Object value = field.get(this);
				String name = field.getName();

				if (type == int.class) {
					int val = (int) value;
					int[] imInt = { val };
					if (ImGui.dragInt(name + ": ", imInt)) {
						field.set(this, imInt[0]);
					}

				}
				if (isPrivat) {
					field.setAccessible(false);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
