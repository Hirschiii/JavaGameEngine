package game.util;

public class Settings {
    // public static float GRID_WIDTH = 0.25f;
    // public static float GRID_HEIGHT = 0.25f;
    public static final boolean RELEASE_BUILD = false;
    public static final float tileSize = 1;
    public static final float GRID_WIDTH = 1f * tileSize;
    public static final float GRID_HEIGHT = 1f * tileSize;
    public static final float PROJECTION_HEIGHT = 12 * tileSize;
    // 0: nothing 1: in Editor 2: in editor play 3: in Final Game
    public static final int DEBUG_DRAW = 2;
}
