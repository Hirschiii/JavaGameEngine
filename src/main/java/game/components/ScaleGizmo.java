package game.components;

import game.editor.PropertiesWindow;
import game.engine.MouseListener;

public class ScaleGizmo extends Gizmo {
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {
        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void update(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldDX();
            } else if (yAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldDY();
            }
        }

        super.update(dt);
    }
}
