package game.components;

/**
 * KeyControls
 */
public class KeyControls extends Component {
    @Override
    public void update(float dt) {

        // TODO Move this into own keyEditorBinding class
        if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.keyBeginPress(GLFW_KEY_D)) {
            GameObject newObj = this.activeGameObject.copy();
            Window.getScene().addGameObject(newObj);
            newObj.transform.position.add(0.1f, 0.1f);
            this.propertiesWindow.setActiveGameObject(newObj);
            return;
        } else if (KeyListener.keyBeginPress(GLFW_KEY_BACKSPACE)) {
            this.activeGameObject.destroy();
            this.activeGameObject = null;
        }
    }
}
