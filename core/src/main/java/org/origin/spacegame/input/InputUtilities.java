package org.origin.spacegame.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.origin.spacegame.game.CameraManager;

public class InputUtilities
{
    private static InputMultiplexer plexer = null;
    private static ZoomInputProcessor zoomProcessor = null;

    public static void detectCameraMovement(CameraManager manager)
    {
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP) || Gdx.input.isKeyPressed(Input.Keys.W))
            manager.getCurrentCamera().position.y += (0.4f * manager.getCurrentCamera().zoom);;
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
            manager.getCurrentCamera().position.y -= (0.4f * manager.getCurrentCamera().zoom);
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
            manager.getCurrentCamera().position.x -= (0.4f * manager.getCurrentCamera().zoom);
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
            manager.getCurrentCamera().position.x += (0.4f * manager.getCurrentCamera().zoom);
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.PLUS))
            manager.getCurrentCamera().zoom += (0.05f * manager.getCurrentCamera().zoom);
        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE) || Gdx.input.isKeyPressed(Input.Keys.MINUS))
            manager.getCurrentCamera().zoom -= (0.05f * manager.getCurrentCamera().zoom);
        if(manager.getCurrentCamera().zoom <= 0.01f)
            manager.getCurrentCamera().zoom = 0.01f;

        /*if(Gdx.input.isTouched())
            detectStarSystemMouseClicks(manager);*/
    }

    public static void initialize(OrthographicCamera defaultCamera)
    {
        //Initialize the input-multiplexer, add the default processor to it, and add our zoom processor.
        initializeInputMultiplexer(defaultCamera);

        //Initialize the tile map's input detection.
        //initializeTileMapInput();
    }

    public static InputMultiplexer getInputMultiplexer()
    {
        return plexer;
    }

    public static void setProjectionCamera(OrthographicCamera camera)
    {
        zoomProcessor.setCamera(camera);
    }

    private static void initializeInputMultiplexer(OrthographicCamera defaultCamera)
    {
        plexer = new InputMultiplexer();
        ZoomInputProcessor zoomInputProcessor = new ZoomInputProcessor(defaultCamera);
        zoomProcessor = zoomInputProcessor;
        plexer.addProcessor(zoomInputProcessor);
        Gdx.input.setInputProcessor(plexer);
    }

    //Sets the default value for each tile.
    /*private static void initializeTileMapInput()
    {
        tiles = new int[Math.round(Constants.GALAXY_WIDTH)][Math.round(Constants.GALAXY_HEIGHT)];
        Array<StarSystem> systems = GameInstance.getInstance().getState().getStarSystems();
        for(int x = 0; x < tiles.length; x++)
        {
            for(int y = 0; y < tiles[x].length; y++)
            {
                tiles[x][y] = Constants.InputConstants.TILE_MAP_NO_SYSTEM_VAL;
            }
        }
        for(StarSystem system : systems)
        {
            int roundedX = Math.round(system.getGalacticPosition().x);
            int roundedY = Math.round(system.getGalacticPosition().y);
            tiles[roundedX][roundedY] = system.id;
        }
    }*/
    /*public static StarSystem getSelectedStarSystem()
    {
        return selectedStarSystem;
    }*/

    //This detects mouse clicks and checks to see if the click is in bounds.
    //If it is, and the click matches a star system's tile coordinates, then
    //that system is selected.
    /*static Vector3 projectedCoords = new Vector3();
    public static void detectStarSystemMouseClicks(CameraManager manager)
    {
        String logText = "";

        int px = Gdx.input.getX();
        int py = Gdx.input.getY();
        projectedCoords.x = (float)px;
        projectedCoords.y = (float)py;
        Vector3 unprojectedCoords = manager.getCurrentCamera().unproject(projectedCoords);

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

        if(!outOfBounds && tiles[x][y] != Constants.InputConstants.TILE_MAP_NO_SYSTEM_VAL)
        {
            StarSystem thisSelectedStarSystem = GameInstance.getInstance().getState().getStarSystem(tiles[x][y]);
            if(thisSelectedStarSystem != selectedStarSystem)
            {
                selectedStarSystem = thisSelectedStarSystem;
                GameInstance.getInstance().selectStarSystem(selectedStarSystem);
                Gdx.app.log("InputUtilities", " Star System " + selectedStarSystem.id + " selected.");
                manager.setRenderView(CameraManager.RenderView.SYSTEM_VIEW);
            }
        }
    }*/
}
