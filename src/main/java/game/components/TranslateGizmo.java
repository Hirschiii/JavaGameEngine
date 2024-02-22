package game.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import game.editor.PropertiesWindow;
import game.engine.GameObject;
import game.engine.MouseListener;
import game.engine.Prefabs;
import game.engine.Window;

public class TranslateGizmo extends Component {
	private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
	private Vector4f xAxisColorHover = new Vector4f();

	private Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
	private Vector4f yAxisColorHover = new Vector4f();

	private GameObject xAxisObject;
	private GameObject yAxisObject;

	private SpriteRenderer xAxisSprite;
	private SpriteRenderer yAxisSprite;

	private GameObject activeGameObject = null;
	private PropertiesWindow propertiesWindow;

	private Vector2f xAxisOffset = new Vector2f();
	private Vector2f yAxisOffset = new Vector2f();

	public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
		this.propertiesWindow = propertiesWindow;

		this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 0.5f, (float) (48 / 16) / 2, 2);
		this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 0.5f, (float) (48 / 16) / 2, 2);

		this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
		this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);

		this.xAxisOffset = new Vector2f(1.75f, -0.25f);
		this.yAxisOffset = new Vector2f(0.25f, 1.75f);

		Window.getScene().addGameObject(this.xAxisObject);
		Window.getScene().addGameObject(this.yAxisObject);
	}

	@Override
	public void start() {
		this.xAxisObject.transform.rotation = 90;
		this.yAxisObject.transform.rotation = 180;
		this.xAxisObject.setNoSerialize();
		this.yAxisObject.setNoSerialize();

	}

	@Override
	public void update(float dt) {
		this.activeGameObject = propertiesWindow.getActiveGameObject();

		boolean xAxisHot = checkXHoverState();	
		boolean yAxisHot = checkYHoverState();	

		if (this.activeGameObject != null) {
			this.setActive();
			this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
			this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);

			this.xAxisObject.transform.position.add(this.xAxisOffset);
			this.yAxisObject.transform.position.add(this.yAxisOffset);

		} else {
			this.setInactive();
			return;
		}


	}

	public void setActive() {
		this.xAxisSprite.setColor(this.xAxisColor);
		this.yAxisSprite.setColor(this.yAxisColor);
	}

	public void setInactive() {
		this.activeGameObject = null;
		this.xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
		this.yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
	}

	private boolean checkXHoverState(){
		Vector2f mousePos = new Vector2f(MouseListener.getWorldX(), MouseListener.getWorldY());
		int id = propertiesWindow.readPixel((int)mousePos.x, (int)mousePos.y);

		if(id == xAxisObject.getUid()) {
			xAxisSprite.setColor(yAxisColorHover);
			return true;
		}
		return false;
	}
	private boolean checkYHoverState(){
		Vector2f mousePos = new Vector2f(MouseListener.getWorldX(), MouseListener.getWorldY());
		int id = propertiesWindow.readPixel((int)mousePos.x, (int)mousePos.y);

		if(id == yAxisObject.getUid()) {
			xAxisSprite.setColor(yAxisColorHover);
			return true;
		}
		return false;
	}
}
