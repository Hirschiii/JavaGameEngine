package game.scene;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import game.components.AnimationState;
import game.components.EditorCamera;
import game.components.GameCamera;
import game.components.GizmoSystem;
import game.components.GridLines;
import game.components.KeyControls;
import game.components.MouseControls;
import game.components.Rigidbody;
import game.components.ScaleGizmo;
import game.components.Sprite;
import game.components.SpriteRenderer;
import game.components.Spritesheet;
import game.components.StateMachine;
import game.components.TranslateGizmo;
import game.editor.JImGui;
import game.engine.Camera;
import game.engine.GameObject;
import game.engine.MouseListener;
import game.engine.Prefabs;
import game.engine.Transform;
import game.engine.Window;
import game.renderer.DebugDraw;
import game.util.AssetPool;
import game.util.Settings;
import imgui.ImGui;
import imgui.ImVec2;

public class LevelSceneInitializer extends SceneInitializer {
    public LevelSceneInitializer() {
    }

    @Override
    public void init(Scene scene) {
        Spritesheet sprites = AssetPool
                // .getSpritesheet("src/main/resources/assets/images/spritesheets/decorationsAndBlocks.png");
                .getSpritesheet("assets/spriteSheets/all.png");

        GameObject cameraObject = scene.createGameObject("GameCamera");
        cameraObject.addComponent(new GameCamera(scene.camera()));
        cameraObject.start();

        scene.addGameObject(cameraObject);
    }

    @Override
    public void loadResources(Scene scene) {
        // AssetPool.getShader("src/main/resources/assets/shaders/default.glsl");
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getTexture("src/main/resources/assets/images/blendImage2.png");

        AssetPool.addSpritesheet("src/main/resources/assets/images/spritesheets/decorationsAndBlocks.png",
                new Spritesheet(
                        AssetPool.getTexture("src/main/resources/assets/images/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
        AssetPool.addSpritesheet("assets/spriteSheets/all.png",
                new Spritesheet(
                        AssetPool.getTexture("assets/spriteSheets/all.png"),
                        32, 32, 72, 0));

        AssetPool.addSpritesheet("assets/spriteSheets/CharacterAnimation.png",
                new Spritesheet(
                        AssetPool.getTexture("assets/spriteSheets/CharacterAnimation.png"),
                        32, 32, 32, 0));
        AssetPool.addSpritesheet("assets/spriteSheets/all.png",
                new Spritesheet(
                        AssetPool.getTexture("assets/spriteSheets/all.png"),
                        32, 32, 4, 0));

        AssetPool.addSpritesheet("assets/Character/Sheet/Sheet.png",
                new Spritesheet(
                        AssetPool.getTexture("assets/Character/Sheet/Sheet.png"),
                        32, 32, 32, 0));
        AssetPool.addSpritesheet("assets/utils/gizmos.png",
                new Spritesheet(
                        AssetPool.getTexture("assets/utils/gizmos.png"),
                        24, 48, 3, 0));

        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);

                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }

            if (g.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTexture();
            }
        }
    }

    @Override
    public void imgui() {
    }
}
