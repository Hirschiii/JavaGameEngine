package game.scene;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import game.components.AnimationState;
import game.components.EditorCamera;
import game.components.GizmoSystem;
import game.components.GridLines;
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

public class LevelEditorSceneInitializer extends SceneInitializer {

    private Spritesheet sprites;

    private GameObject levelEditorStuff;

    public LevelEditorSceneInitializer() {
    }

    @Override
    public void init(Scene scene) {
        sprites = AssetPool
                // .getSpritesheet("src/main/resources/assets/images/spritesheets/decorationsAndBlocks.png");
                .getSpritesheet("assets/Character/Sheet/Sheet.png");
        Spritesheet gizmos = AssetPool.getSpritesheet("assets/utils/gizmos.png");

        levelEditorStuff = scene.createGameObject("LevelEditor");

        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObject(levelEditorStuff);
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

        AssetPool.addSpritesheet("assets/spriteSheets/CharacterAnimation.png",
                new Spritesheet(
                        AssetPool.getTexture("assets/spriteSheets/CharacterAnimation.png"),
                        32, 32, 32, 0));

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
        ImGui.begin("LevelEditor Stuff");
        levelEditorStuff.imgui();
        ImGui.end();

        ImGui.begin("Objects");
        if (ImGui.beginTabBar("WindowTabBar")) {
            if (ImGui.beginTabItem("Blocks")) {

                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                float windowY2 = windowPos.y + windowSize.y;

                for (int i = 0; i < sprites.size(); i++) {
                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 2;
                    float spriteHeight = sprite.getHeight() * 2;

                    int id = sprite.getTexId();
                    Vector2f[] texCoords = sprite.getTexCoords();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                            texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, Settings.GRID_WIDTH,
                                Settings.GRID_HEIGHT);
                        // Attach to mouse Cursor to drop
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);

                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("PreFabs")) {
                Spritesheet playerSprites = AssetPool.getSpritesheet("assets/spriteSheets/CharacterAnimation.png");

                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 2;
                float spriteHeight = sprite.getHeight() * 2;

                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                        texCoords[2].y)) {
                    GameObject object = Prefabs.generateMario();
                    // Attach to mouse Cursor to drop
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.sameLine();
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }

        ImGui.end();
    }

    public void ImGuiWordSet() {
        ImGui.begin("Set World");
        JImGui.drawVec2Control("CamPos", Window.getScene().camera().getPosition());
        JImGui.drawVec2Control("Window Size:", new Vector2f(Window.getWidth(), Window.getHeight()));
        Vector2f zoom = new Vector2f(Window.getScene().camera().getZoom(), 0);
        JImGui.drawVec2Control("Zoom", zoom);
        ImGui.end();

    }

}
