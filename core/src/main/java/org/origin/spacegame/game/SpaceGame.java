package org.origin.spacegame.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import org.origin.spacegame.input.InputUtilities;
import org.origin.spacegame.screens.GalaxyScreen;
import org.origin.spacegame.screens.PlanetScreen;
import org.origin.spacegame.screens.StarSystemScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SpaceGame extends Game//ApplicationAdapter
{
    private CameraManager cameraManager;
    private SpriteBatch batch;

    private GalaxyScreen galaxyScreen;
    private StarSystemScreen starSystemScreen;
    private PlanetScreen planetScreen;

    @Override
    public void create()
    {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        GameInstance.getInstance().setGame(this);
        cameraManager = new CameraManager();
        loadGameData();
        initInput();

        //These classes rely on all the initial generation to be finished.
        this.galaxyScreen = new GalaxyScreen(this);
        this.starSystemScreen = new StarSystemScreen(this);
        this.planetScreen = new PlanetScreen(this);
    }

    private void initInput()
    {
        InputUtilities.initialize(cameraManager.getCurrentCamera());
    }

    private void loadGameData()
    {
        batch = new SpriteBatch();
        GameInstance.getInstance().loadData();
        GameInstance.getInstance().getState().initialize();
        GameInstance.getInstance().getState().debugSystemIDs();
    }

    @Override
    public void render()
    {
        ScreenUtils.clear(Color.BLACK);
        if(getCameraManager().hasRecentlyChangedCamera())
        {
            if(getCameraManager().getRenderView() == CameraManager.RenderView.SYSTEM_VIEW)
            {
                getCameraManager().toggleChangeCameraFlag();
                setScreen(starSystemScreen);
            }
            if(getCameraManager().getRenderView() == CameraManager.RenderView.GALACTIC_VIEW)
            {
                getCameraManager().toggleChangeCameraFlag();
                setScreen(galaxyScreen);
            }
            if(getCameraManager().getRenderView() == CameraManager.RenderView.PLANET_VIEW)
            {
                getCameraManager().toggleChangeCameraFlag();
                setScreen(planetScreen);
            }
        }
        GameInstance.getInstance().getState().update();
        super.render();
    }

    @Override
    public void dispose()
    {
        GameInstance.getInstance().dispose();
        batch.dispose();
        super.dispose();
    }

    public CameraManager getCameraManager()
    {
        return cameraManager;
    }

    public SpriteBatch getBatch()
    {
        return batch;
    }
}
