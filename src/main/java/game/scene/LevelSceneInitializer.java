package game.scene;

import game.components.GameCamera;
import game.components.SpriteRenderer;
import game.components.Spritesheet;
import game.components.StateMachine;
import game.components.InteraktiveGizmo;
import game.engine.GameObject;
import game.engine.Prefabs;
import game.util.AssetPool;

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

        GameObject interactiveGizmo = Prefabs.generateSpriteObject(sprites.getSprite(70), 1f, 1f);
        interactiveGizmo.name = "InteraktiveGizmo";
        interactiveGizmo.transform.zIndex = 100;
        interactiveGizmo.addComponent(new InteraktiveGizmo());

        scene.addGameObject(interactiveGizmo);
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
                        32, 32, 75, 0));

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
