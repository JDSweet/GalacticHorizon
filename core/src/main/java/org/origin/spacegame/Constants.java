package org.origin.spacegame;

public class Constants
{
    public static float GALAXY_WIDTH = 40f;
    public static float GALAXY_HEIGHT = 40f;
    public static float MIN_DISTANCE_BETWEEN_STARS = 15f;

    public static float GALACTIC_MAP_CAMERA_VIEWPORT_WIDTH = 50f;
    public static float GALACTIC_MAP_CAMERA_VIEWPORT_HEIGHT = 50f;

    public static float STAR_SYSTEM_VIEW_CAMERA_VIEWPORT_WIDTH = 25f;
    public static float STAR_SYSTEM_VIEW_CAMERA_VIEWPORT_HEIGHT = 25f;



    //The width and height of each individual star system when rendering on the galactic map.
    public static float STAR_SYSTEM_GALACTIC_MAP_RENDER_WIDTH = 0.5f;
    public static float STAR_SYSTEM_GALACTIC_MAP_RENDER_HEIGHT = 0.5F;

    public static int STAR_SYSTEM_COUNT = 100;

    public static int MIN_PLANET_COUNT = 1;
    public static int MAX_PLANET_COUNT = 12;

    public static class FileConstants
    {
        public static String DEFAULT_GUI_CALLBACK_FILE = "00_callbacks.lua";
        public static String GUI_SCRIPTS_DIR = "assets/scripts/gui/";
        public static String GAMEPLAY_SCRIPTS_DIR = "assets/scripts/gameplay/";
        public static String GUI_XML_DIR = "assets/gfx/ui/xml/";
    }

    public static class InputConstants
    {
        public static int TILE_MAP_NO_SYSTEM_VAL = -1;
    }

    public static class StarSystemConstants
    {
        public static float MIN_DISTANCE_BETWEEN_PLANET_ORBITS = 50f;
        public static float MAX_DISTANCE_BETWEEN_PLANET_ORBITS = 90f;
        //The max height and width of the star system. Used to determine camera bounds.
        //Also used to calculate the logical center of the system.

        public static float STAR_SYSTEM_INTERNAL_WIDTH = 150f;
        public static float STAR_SYSTEM_INTERNAL_HEIGHT = 150f;

        public static float STAR_SYSTEM_VIEWPORT_WIDTH = 100f;
        public static float STAR_SYSTEM_VIEWPORT_HEIGHT = 100f;

        public static float MINIMUM_ORBITAL_RADIUS = ((STAR_SYSTEM_INTERNAL_WIDTH + STAR_SYSTEM_INTERNAL_HEIGHT)/2f) * 0.35f;
        public static float STAR_RENDER_SIZE = 30f;
        public static float PLANET_RENDER_SIZE = 10f;

        public static int MAX_STARS_IN_SYSTEM = 3;

        //Planets have certain radii that they can spawn within. Scorch Zone, Habitable Zone, or the Freeze Zone.
        //Some planets can spawn in "any" zone (i.e. a binary star, or a gas giant).
        public static float SCORCH_RADIUS_INNER = MINIMUM_ORBITAL_RADIUS;
        public static float SCORCH_RADIUS_OUTER = MINIMUM_ORBITAL_RADIUS / 2f;
        public static float HABITABLE_RADIUS_INNER = SCORCH_RADIUS_OUTER;
        public static float HABITABLE_RADIUS_OUTER = HABITABLE_RADIUS_INNER / 2f;
        public static float FREEZE_RADIUS_INNER = HABITABLE_RADIUS_OUTER + 1;
    }
}
