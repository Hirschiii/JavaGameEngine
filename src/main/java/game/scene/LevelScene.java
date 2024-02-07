package game.scene;

import game.engine.Window;

public class LevelScene extends Scene{
	

	public LevelScene() {
		System.out.println("Inside LEvel Scene");
		Window.get().r = 1;
		Window.get().g = 1;
		Window.get().b = 1;
	}

	@Override
	public void update(float dt) {
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'init'");
	}
	
}
