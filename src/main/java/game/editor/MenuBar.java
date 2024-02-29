package game.editor;

import game.observers.EventSystem;
import game.observers.events.Event;
import game.observers.events.EventType;
import imgui.ImGui;

public class MenuBar {
	public void imgui() {
		ImGui.beginMenuBar();
		if (ImGui.beginMenu("File")) {
			if(ImGui.menuItem("Save", "Ctrl+s")) {
				EventSystem.notify(null, new Event(EventType.SaveLevel));
			}
			if(ImGui.menuItem("Load", "Ctrl+o")) {
				EventSystem.notify(null, new Event(EventType.LoadLevel));
			}
			ImGui.endMenu();
		}

		ImGui.endMenuBar();
	}
}
