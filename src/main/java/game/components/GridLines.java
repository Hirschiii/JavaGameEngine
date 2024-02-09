package game.components;

import java.util.Set;

import org.joml.Vector2f;
import org.joml.Vector3f;

import game.engine.Camera;
import game.engine.Window;
import game.renderer.DebugDraw;
import game.util.Settings;


public class GridLines extends Component{

	@Override
	public void update(float dt) {
		Vector2f cameraPos = Window.getScene().camera().getPosition();
		Vector2f projectionSize = Window.getScene().camera().getProjectionSize();

		float firstX = ( cameraPos.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
		float firstY = ( cameraPos.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

		float height = projectionSize.y;
		float width = projectionSize.x;

		int numVtLines = (int)Math.ceil(width / Settings.GRID_WIDTH);
		int numHzLines = (int)Math.ceil(height / Settings.GRID_HEIGHT);

		int maxLine = Math.max(numHzLines, numVtLines);

		Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);

		for(int i=0; i < maxLine; i++) {
			float x = firstX + (Settings.GRID_WIDTH * i);
			float y = firstY + (Settings.GRID_HEIGHT * i);

			if (i < numVtLines) {
				DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, y + height), color, 1);
			}

			if (i < numHzLines) {
				DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color, 1);
			}

		}

	}
	
}
