package org.origin.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Constants;
import org.origin.spacegame.game.SpaceGame;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.input.InputUtilities;
import org.origin.spacegame.game.CameraManager;

public class GalaxyScreen implements Screen
{
    private SpaceGame game;
    private int[][] tiles;
    private StarSystem selectedStarSystem;

    public GalaxyScreen(SpaceGame game)
    {
        this.game = game;
        initializeTileMapInput();
    }

    @Override
    public void show()
    {
        Gdx.app.log("GalaxyScreenDebug", "Showing Galaxy View...");
    }

    /**
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta)
    {
        detectInput();
        InputUtilities.detectCameraMovement(game.getCameraManager());
        game.getCameraManager().update();
        game.getBatch().setProjectionMatrix(game.getCameraManager().getCurrentCamera().combined);
        GameInstance.getInstance().getState().renderGalacticMap(game.getBatch());
    }

    //Sets the default value for each tile.
    private void initializeTileMapInput()
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
    }

    private void detectInput()
    {
        if(Gdx.input.isTouched())
            detectStarSystemMouseClicks(game.getCameraManager());
    }

    //This detects mouse clicks and checks to see if the click is in bounds.
    //If it is, and the click matches a star system's tile coordinates, then
    //that system is selected.
    static Vector3 projectedCoords = new Vector3();
    public void detectStarSystemMouseClicks(CameraManager manager)
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
                Gdx.app.log("GalaxyScreen", "Star System " + selectedStarSystem.id + " selected.");
                Gdx.app.log("GalaxyScreen", "Star System " + selectedStarSystem.id + " has " + selectedStarSystem.planets.size + " planets.");
                manager.setRenderView(CameraManager.RenderView.SYSTEM_VIEW);
            }
        }
    }

    /**
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height)
    {

    }

    /**
     *
     */
    @Override
    public void pause()
    {

    }

    /**
     *
     */
    @Override
    public void resume()
    {

    }

    /**
     *
     */
    @Override
    public void hide()
    {

    }

    /**
     *
     */
    @Override
    public void dispose()
    {

    }
}
