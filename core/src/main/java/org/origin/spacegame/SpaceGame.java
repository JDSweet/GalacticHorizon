package org.origin.spacegame;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.input.InputUtilities;
import org.origin.spacegame.screens.GalaxyScreen;
import org.origin.spacegame.screens.SystemScreen;
import org.origin.spacegame.utilities.CameraManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SpaceGame extends Game//ApplicationAdapter
{
    private CameraManager cameraManager;
    private SpriteBatch batch;

    private GalaxyScreen galaxyScreen;
    private SystemScreen systemScreen;

    @Override
    public void create()
    {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        cameraManager = new CameraManager();
        loadGameData();
        initInput();

        //These classes rely on all the initial generation to be finished.
        this.galaxyScreen = new GalaxyScreen(this);
        this.systemScreen = new SystemScreen(this);
        //Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode(Gdx.graphics.getPrimaryMonitor()));
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
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        //This bit of code handles the logic of screen transitions.
        if(cameraManager.hasRecentlyChangedCamera() && cameraManager.getRenderView() == CameraManager.RenderView.GALACTIC_VIEW)
        {
            setScreen(galaxyScreen);
            cameraManager.toggleChangeCameraFlag();
        }
        else if(cameraManager.hasRecentlyChangedCamera() && cameraManager.getRenderView() == CameraManager.RenderView.SYSTEM_VIEW)
        {
            setScreen(systemScreen);
            cameraManager.toggleChangeCameraFlag();
        }
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
