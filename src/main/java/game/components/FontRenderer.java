package game.components;

import game.engine.Component;

public class FontRenderer extends Component {
	@Override
	public void start() {
		if (gameObject.getComponent(SpriteRenderer.class) != null) {
			System.out.println("FOund FontRenderer");
		}
	}

	@Override
	public void update(float dt) {
	}

}
