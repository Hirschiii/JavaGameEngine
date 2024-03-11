package game.components;

import java.util.ArrayList;
import java.util.List;

import game.util.AssetPool;

/**
 * AnimationState
 */
public class AnimationState extends Component {

    public String title;
    public List<Frame> animationFrames = new ArrayList<>();

    private static Sprite defaultSprite = new Sprite();
    private float timeTracker = 0.0f;
    private transient int currentSrpite = 0;

    public boolean doesLoop = false;

    public void addFrame(Sprite sprite, float frameTime) {
        animationFrames.add(new Frame(sprite, frameTime));
    }

    public void setLoop(boolean doesLoop) {
        this.doesLoop = doesLoop;
    }

    @Override
    public void update(float dt) {
        if (currentSrpite < animationFrames.size()) {
            timeTracker -= dt;
            if (timeTracker <= 0) {
                if (currentSrpite != animationFrames.size() - 1 || doesLoop) {
                    currentSrpite = (currentSrpite + 1) % animationFrames.size();
                }

                timeTracker = animationFrames.get(currentSrpite).frameTime;
            }
        }
    }

    public Sprite getCurrentSprite() {
        if (currentSrpite < animationFrames.size()) {
            return animationFrames.get(currentSrpite).sprite;
        }

        return defaultSprite;
    }

	public void refreshTextures() {
        for (Frame frame : animationFrames) {
            frame.sprite.setTexture(AssetPool.getTexture(frame.sprite.getTexture().getFilepath()));
        }
	}

}
