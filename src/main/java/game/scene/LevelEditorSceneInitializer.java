package game.scene;

import org.joml.Vector2f;

import game.components.EditorCamera;
import game.components.GridLines;
import game.components.KeyControls;
import game.components.MouseControls;
import game.components.Sprite;
import game.components.SpriteRenderer;
import game.components.Spritesheet;
import game.components.StateMachine;
import game.editor.JImGui;
import game.engine.GameObject;
import game.engine.Item;
import game.engine.Prefabs;
import game.engine.Window;
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
                .getSpritesheet("assets/spriteSheets/all.png");
        Spritesheet gizmos = AssetPool.getSpritesheet("assets/utils/gizmos.png");

        levelEditorStuff = scene.createGameObject("LevelEditor");

        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new KeyControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        scene.addGameObject(levelEditorStuff);
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

        sheetPath = "assets/spriteSheets/CharacterAnimation.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        32, 32, 32, 0));

        sheetPath = "assets/spriteSheets/Items.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        16, 16, 4, 0));

        sheetPath = "assets/spriteSheets/Mes_Apfel_bekommen.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        256, 128, 1, 0));

        sheetPath = "assets/spriteSheets/Eric.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        32, 32, 4, 0));

        sheetPath = "assets/spriteSheets/Amelia.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        32, 32, 4, 0));
        sheetPath = "assets/spriteSheets/Zauntor-Sheet.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        64, 64, 3, 0));

        sheetPath = "assets/spriteSheets/Message_sheet.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        255, 128, 6, 0));

        sheetPath = "assets/spriteSheets/Brief_sheet.png";
        AssetPool.addSpritesheet(sheetPath,
                new Spritesheet(
                        AssetPool.getTexture(sheetPath),
                        768, 768, 2, 0));

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
            imguiBlock();
            imguiPrefabs();
            imguiItems();
            imguiMessages();
            imguiBriefe();
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

    public void imguiBlock() {

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
    }

    public void imguiPrefabs() {
        if (ImGui.beginTabItem("PreFabs")) {
            int uid = 0;
            Spritesheet playerSprites = AssetPool.getSpritesheet("assets/spriteSheets/CharacterAnimation.png");
            Spritesheet streetSprites = AssetPool.getSpritesheet("assets/spriteSheets/all.png");
            Spritesheet eric_sheet = AssetPool.getSpritesheet("assets/spriteSheets/Eric.png");
            Spritesheet amelia_sheet = AssetPool.getSpritesheet("assets/spriteSheets/Amelia.png");
            Spritesheet zaun_sheet = AssetPool.getSpritesheet("assets/spriteSheets/Zauntor-Sheet.png");
            int[] fullRigid_all = new int[1];
            fullRigid_all[0] = 56;

            Sprite sprite = playerSprites.getSprite(0);
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;

            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(uid++);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                    texCoords[2].y)) {
                GameObject object = Prefabs.generatePlayer();
                // Attach to mouse Cursor to drop
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();
            ImGui.sameLine();

            sprite = playerSprites.getSprite(0);
            spriteWidth = sprite.getWidth() * 2;
            spriteHeight = sprite.getHeight() * 2;

            id = sprite.getTexId();
            texCoords = sprite.getTexCoords();

            ImGui.pushID(uid++);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                    texCoords[2].y)) {
                GameObject object = Prefabs.generatePlayer_Two();
                // Attach to mouse Cursor to drop
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();
            ImGui.sameLine();

            sprite = eric_sheet.getSprite(0);
            spriteWidth = sprite.getWidth() * 2;
            spriteHeight = sprite.getHeight() * 2;

            id = sprite.getTexId();
            texCoords = sprite.getTexCoords();

            ImGui.pushID(uid++);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                    texCoords[2].y)) {
                GameObject object = Prefabs.generateFigure(eric_sheet);
                // Attach to mouse Cursor to drop
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();
            ImGui.sameLine();

            sprite = amelia_sheet.getSprite(0);
            spriteWidth = sprite.getWidth() * 2;
            spriteHeight = sprite.getHeight() * 2;

            id = sprite.getTexId();
            texCoords = sprite.getTexCoords();

            ImGui.pushID(uid++);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                    texCoords[2].y)) {
                GameObject object = Prefabs.generateFigure(amelia_sheet);
                // Attach to mouse Cursor to drop
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();
            ImGui.sameLine();

            sprite = zaun_sheet.getSprite(0);
            spriteWidth = sprite.getWidth();
            spriteHeight = sprite.getHeight();

            id = sprite.getTexId();
            texCoords = sprite.getTexCoords();

            ImGui.pushID(uid++);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                    texCoords[2].y)) {
                GameObject object = Prefabs.generateZaun(zaun_sheet);
                // Attach to mouse Cursor to drop
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }
            ImGui.popID();
            ImGui.sameLine();

            for (int i : fullRigid_all) {
                sprite = streetSprites.getSprite(i);
                spriteWidth = sprite.getWidth() * 2;
                spriteHeight = sprite.getHeight() * 2;

                id = sprite.getTexId();
                texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                        texCoords[2].y)) {
                    GameObject object = Prefabs.generateFullRigid(sprite);
                    // Attach to mouse Cursor to drop
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();
            }

            ImGui.endTabItem();
        }
    }

    public void imguiItems() {
        if (ImGui.beginTabItem("Items")) {
            int uid = 0;
            Spritesheet itemSprites = AssetPool.getSpritesheet("assets/spriteSheets/Items.png");

            for (Sprite sprite : itemSprites.getSprites()) {
                float spriteWidth = sprite.getWidth() * 2;
                float spriteHeight = sprite.getHeight() * 2;

                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                        texCoords[2].y)) {
                    GameObject object = Prefabs.generateItem(sprite, "Itemm " + uid);
                    // Attach to mouse Cursor to drop
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();
            }

            ImGui.endTabItem();
        }
    }

    public void imguiMessages() {
        if (ImGui.beginTabItem("Messages")) {
            int uid = 0;
            Spritesheet messag_sheets = AssetPool.getSpritesheet("assets/spriteSheets/Message_sheet.png");

            for (Sprite sprite : messag_sheets.getSprites()) {
                float spriteWidth = sprite.getWidth();
                float spriteHeight = sprite.getHeight();

                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                        texCoords[2].y)) {
                    GameObject object = Prefabs.generateMessage(sprite, 2, 1);
                    // Attach to mouse Cursor to drop
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();

            }

            ImGui.endTabItem();
        }
    }

    public void imguiBriefe() {

        if (ImGui.beginTabItem("Briefe")) {
            int uid = 0;
            Spritesheet messag_sheets = AssetPool.getSpritesheet("assets/spriteSheets/Brief_sheet.png");

            for (Sprite sprite : messag_sheets.getSprites()) {
                float spriteWidth = sprite.getWidth() / 3;
                float spriteHeight = sprite.getHeight() / 3;

                int id = sprite.getTexId();
                Vector2f[] texCoords = sprite.getTexCoords();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,
                        texCoords[2].y)) {
                    GameObject object = Prefabs.generateSpriteObject(sprite, 2, 1);
                    // Attach to mouse Cursor to drop
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                ImGui.sameLine();
            }

            ImGui.endTabItem();
        }
    }

}
