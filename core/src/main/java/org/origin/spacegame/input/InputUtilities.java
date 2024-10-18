package org.origin.spacegame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Constants;
import org.origin.spacegame.entities.StarSystem;
import org.origin.spacegame.game.GameInstance;

public class InputUtilities
{
    private static StarSystem selectedStarSystem = null;

    public static void detectCameraMovement(OrthographicCamera camera)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))
            camera.position.y += (0.4f * camera.zoom);;
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
            camera.position.y -= (0.4f * camera.zoom);
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT))
            camera.position.x -= (0.4f * camera.zoom);
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))
            camera.position.x += (0.4f * camera.zoom);
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.PLUS))
            camera.zoom += (0.05f * camera.zoom);
        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE) || Gdx.input.isKeyPressed(Input.Keys.MINUS))
            camera.zoom -= (0.05f * camera.zoom);
        if(camera.zoom <= 0.01f)
            camera.zoom = 0.01f;

        if(Gdx.input.isTouched())
            detectMouseClicks(camera);
    }

    private static int[][] tiles;

    //Sets the default value for each tile.
    public static void initializeTileMapInput()
    {
        tiles = new int[Math.round(Constants.GALAXY_WIDTH)][Math.round(Constants.GALAXY_HEIGHT)];
        Array<StarSystem> systems = GameInstance.getInstance().getState().getStarSystems();
        for(int x = 0; x < tiles.length; x++)
        {
            for(int y = 0; y < tiles[x].length; y++)
            {
                tiles[x][y] = Constants.InputConstants.TILEMAP_NO_SYSTEM_VAL;
            }
        }
        for(StarSystem system : systems)
        {
            int roundedX = Math.round(system.getGalacticPosition().x);
            int roundedY = Math.round(system.getGalacticPosition().y);
            tiles[roundedX][roundedY] = system.id;
        }
    }

    //This detects mouse clicks and checks to see if the click is in bounds.
    //If it is, and the click matches a star system's tile coordinates, then
    //that system is selected.
    static Vector3 projectedCoords = new Vector3();
    public static void detectMouseClicks(OrthographicCamera camera)
    {
        String logText = "";

        int px = Gdx.input.getX();
        int py = Gdx.input.getY();
        projectedCoords.x = (float)px;
        projectedCoords.y = (float)py;
        Vector3 unprojectedCoords = camera.unproject(projectedCoords);

        int x = Math.round(unprojectedCoords.x);
        int y = Math.round(unprojectedCoords.y);

        boolean outOfBounds = false;

        if(x < 0 || x >= tiles.length)
        {
            logText += "/n x = " + x + " is out of bounds.";
            outOfBounds = true;
        }
        if(y < 0 || y >= tiles.length)
        {
            outOfBounds = true;
            logText += "/n y = " + y + " is out of bounds.";
        }

        if(!outOfBounds && tiles[x][y] != Constants.InputConstants.TILEMAP_NO_SYSTEM_VAL)
        {
            StarSystem thisSelectedStarSystem = GameInstance.getInstance().getState().getStarSystem(tiles[x][y]);
            if(thisSelectedStarSystem != selectedStarSystem)
            {
                selectedStarSystem = thisSelectedStarSystem;
                Gdx.app.log("InputUtilities", " Star System " + selectedStarSystem.id + " selected.");
            }
        }
    }
}
