package org.origin.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import org.origin.spacegame.SpaceGame;
import org.origin.spacegame.entities.StarSystem;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.input.InputUtilities;

public class SystemScreen implements Screen
{
    private SpaceGame game;
    boolean enterPositionSet = false;

    public SystemScreen(SpaceGame game)
    {
        this.game = game;
    }

    /**
     *
     */
    @Override
    public void show()
    {
        Gdx.app.log("SystemScreen Debug", "Showing System View...");
    }

    /**
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta)
    {
        StarSystem selectedSystem = GameInstance.getInstance().getSelectedStarSystem();
        InputUtilities.detectCameraMovement(game.getCameraManager());
        if(!enterPositionSet)
        {
            game.getCameraManager().getCurrentCamera().position.x = selectedSystem.getCenter().x;
            game.getCameraManager().getCurrentCamera().position.y = selectedSystem.getCenter().y;
            this.enterPositionSet = true;
        }
        game.getCameraManager().update();

        game.getBatch().setProjectionMatrix(game.getCameraManager().getCurrentCamera().combined);
        GameInstance.getInstance().getState().renderSystemView(game.getBatch(), GameInstance.getInstance().getSelectedStarSystem());
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
