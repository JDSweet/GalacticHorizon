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

    public static class InputConstants
    {
        public static int TILE_MAP_NO_SYSTEM_VAL = -1;
    }

    public static class StarSystemConstants
    {
        public static float MIN_DISTANCE_BETWEEN_PLANET_ORBITS = 40f;
        public static float MAX_DISTANCE_BETWEEN_PLANET_ORBITS = 80f;
        //The max height and width of the star system. Used to determine camera bounds.
        //Also used to calculate the logical center of the system.

        public static float STAR_SYSTEM_INTERNAL_WIDTH = 150f;
        public static float STAR_SYSTEM_INTERNAL_HEIGHT = 150f;

        public static float STAR_SYSTEM_VIEWPORT_WIDTH = 100f;
        public static float STAR_SYSTEM_VIEWPORT_HEIGHT = 100f;

        public static float MINIMUM_ORBITAL_RADIUS = ((STAR_SYSTEM_INTERNAL_WIDTH + STAR_SYSTEM_INTERNAL_HEIGHT)/2f) * 0.25f;
        public static float STAR_RENDER_SIZE = 30f;
        public static float PLANET_RENDER_SIZE = 10f;

        public static int MAX_STARS_IN_SYSTEM = 3;
    }
}
