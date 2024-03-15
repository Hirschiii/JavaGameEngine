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

        GameObject interactiveGizmo = Prefabs.generateSpriteObject(sprites.getSprite(69), 0.6f, 0.6f);
        interactiveGizmo.name = "InteraktiveGizmo";
        interactiveGizmo.transform.zIndex = 100;
        interactiveGizmo.addComponent(new InteraktiveGizmo());

        scene.addGameObject(interactiveGizmo);
        scene.addGameObject(cameraObject);
        scene.gameStart();
    }

    @Override
    public void loadResources(Scene scene) {
        // AssetPool.getShader("src/main/resources/assets/shaders/default.glsl");
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getShader("assets/shaders/vhs.glsl");


        String sheetPath = "assets/spriteSheets/all.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        32, 32, 75, 0));

        sheetPath ="assets/spriteSheets/CharacterAnimation.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        32, 32, 32, 0));

        sheetPath ="assets/spriteSheets/Items.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        16, 16, 4, 0));

        sheetPath ="assets/spriteSheets/Mes_Apfel_bekommen.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        256, 128, 1, 0));

        sheetPath ="assets/spriteSheets/Eric.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        32, 32, 4, 0));

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
