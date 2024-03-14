package game.util;

public class Settings {
    public static final String HOME_PATH = "/Users/niklas/projects/school/liferfix_game_projekt_2024_2_hj/";
    // public static float GRID_WIDTH = 0.25f;
    // public static float GRID_HEIGHT = 0.25f;
    public static final boolean RELEASE_BUILD = false;
    public static final float tileSize = 1;
    public static final float GRID_WIDTH = 1f * tileSize;
    public static final float GRID_HEIGHT = 1f * tileSize;
    public static final float PROJECTION_HEIGHT = 4 * tileSize;
    // 0: nothing 1: in Editor 2: in editor play 3: in Final Game
    public static final int DEBUG_DRAW = 2;
    public static final String LEVEL_JSON_PATH = HOME_PATH + "level.json";
    public static final String IMGUI_CONFIG_FILE_PATH = HOME_PATH + "imgui.ini";

}
