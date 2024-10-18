package org.origin.spacegame;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.input.InputUtilities;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter
{
    //private SpriteBatch batch;
    private Texture image;

    private OrthographicCamera galacticCamera;
    private OrthographicCamera systemCamera;
    private OrthographicCamera currentCamera;

    enum RenderView
    {
        SYSTEM_VIEW,
        GALACTIC_VIEW
    }

    private RenderView renderView;
    private SpriteBatch batch;

    ExtendViewport extendViewport;

    @Override
    public void create()
    {
        renderView = RenderView.GALACTIC_VIEW;
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //Create cameras...
        galacticCamera = new OrthographicCamera(Constants.GALACTIC_MAP_CAMERA_VIEWPORT_WIDTH, Constants.GALACTIC_MAP_CAMERA_VIEWPORT_HEIGHT);
        systemCamera = new OrthographicCamera();

        loadGameData();
    }

    private void loadGameData()
    {
        batch = new SpriteBatch();
        image = new Texture("gfx/libgdx.png");
        GameInstance.getInstance().loadData();
        GameInstance.getInstance().getState().initialize();
        InputUtilities.initializeTileMapInput();

        GameInstance.getInstance().getState().debugSystemIDs();
    }

    @Override
    public void render()
    {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        if(renderView == RenderView.GALACTIC_VIEW)
        {
            this.currentCamera = galacticCamera;
        }
        else
        {
            this.currentCamera = systemCamera;
        }

        InputUtilities.detectCameraMovement(currentCamera);
        currentCamera.update();

        batch.setProjectionMatrix(currentCamera.combined);
        GameInstance.getInstance().getState().renderGalacticMap(batch);

    }

    @Override
    public void dispose()
    {
        GameInstance.getInstance().dispose();
        batch.dispose();
        //image.dispose();
    }
}
