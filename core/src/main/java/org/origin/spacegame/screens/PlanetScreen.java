package org.origin.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.origin.spacegame.game.SpaceGame;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.input.InputUtilities;
import org.origin.spacegame.game.CameraManager;

public class PlanetScreen implements Screen
{
    private SpaceGame game;
    boolean enterPositionSet = false;

    //This is used for the System GUI
    //A button to take you back to the galaxy view
    //is at the bottom center of the screen.
    private Stage stage;
    private TextButton goToGalaxyScreenButton;

    public PlanetScreen(SpaceGame game)
    {
        this.game = game;
        stage = new Stage();
        InputUtilities.getInputMultiplexer().addProcessor(stage);
        goToGalaxyScreenButton = new TextButton("Galaxy Screen", GameInstance.getInstance().getGuiSkin());
        goToGalaxyScreenButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.getCameraManager().setRenderView(CameraManager.RenderView.GALACTIC_VIEW);
                Gdx.app.log("SystemScreenDebug", "Galaxy Button Clicked!");
            }
        });


        goToGalaxyScreenButton.setX(Gdx.graphics.getWidth()/2f);
        //goToGalaxyScreenButton.setY(Gdx.graphics.getHeight()/2f);
        stage.addActor(goToGalaxyScreenButton);
        //goToGalaxyScreenButton.
    }

    /**
     *
     */
    @Override
    public void show()
    {
        Gdx.app.log("SystemScreenDebug", "Showing Planet View...");
    }

    /**
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta)
    {
        //StarSystem selectedSystem = GameInstance.getInstance().getSelectedStarSystem();
        InputUtilities.detectCameraMovement(game.getCameraManager());
        if(!enterPositionSet)
        {
            game.getCameraManager().getCurrentCamera().position.x = 0f;
            game.getCameraManager().getCurrentCamera().position.y = 0f;
            this.enterPositionSet = true;
        }
        game.getCameraManager().update();
        game.getBatch().begin();
        game.getBatch().setProjectionMatrix(game.getCameraManager().getCurrentCamera().combined);
        GameInstance.getInstance().getState().renderPlanetMap(game.getBatch(), GameInstance.getInstance().getSelectedPlanet());
        game.getBatch().end();
        stage.draw();
        stage.act();
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
