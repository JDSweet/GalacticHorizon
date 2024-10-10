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
    private SpriteBatch batch;
    private Texture image;
    private OrthographicCamera camera;

    ExtendViewport extendViewport;

    @Override
    public void create()
    {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        loadGameData();
        initializeCamera();
    }

    private void loadGameData()
    {
        batch = new SpriteBatch();
        image = new Texture("gfx/libgdx.png");
        GameInstance.getInstance().loadData();
        GameInstance.getInstance().getState().initialize();
    }

    private void initializeCamera()
    {
        camera = new OrthographicCamera(250, 250);
        camera.zoom = 0.01f;
    }

    @Override
    public void render()
    {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(image, 0, 0, 30, 30);
        InputUtilities.detectCameraMovement(camera);
        camera.update();
        batch.end();
    }

    @Override
    public void dispose()
    {
        GameInstance.getInstance().dispose();
        batch.dispose();
        image.dispose();
    }
}
