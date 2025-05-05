package org.origin.spacegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.origin.spacegame.Constants;
import org.origin.spacegame.game.SpaceGame;
import org.origin.spacegame.entities.stellarobj.Planet;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.input.InputUtilities;
import org.origin.spacegame.gui.ScriptedGUIScene;

public class StarSystemScreen implements Screen, InputProcessor
{
    private SpaceGame game;
    boolean enterPositionSet = false;

    //This is used for the System GUI
    //A button to take you back to the galaxy view
    //is at the bottom center of the screen.
    private Stage stage;

    //Planet management window.
    private Window planetManagementWindow;

    private ScriptedGUIScene scene;

    private LuaValue gameplayCallbacks;
    private LuaValue onClickCallback;
    private LuaValue onPlanetClickedCallback;

    public StarSystemScreen(SpaceGame game)
    {
        this.game = game;
        this.scene = new ScriptedGUIScene("star_system_gui.xml");
        this.gameplayCallbacks = JsePlatform.standardGlobals();
        gameplayCallbacks.get("dofile").call(Gdx.files.internal(Constants.FileConstants.GAMEPLAY_SCRIPTS_DIR + "00_star_system_screen_gameplay_callbacks.lua").path());
        this.onClickCallback = gameplayCallbacks.get("on_click");
        this.onPlanetClickedCallback = gameplayCallbacks.get("on_planet_clicked");
    }

    @Override
    public void show()
    {
        Gdx.app.log("StarSystemScreenDebug", "Showing System View...");
        game.getCameraManager().getCurrentCamera().position.x = Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH/2f;
        game.getCameraManager().getCurrentCamera().position.y = Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH/2f;
        InputUtilities.getInputMultiplexer().addProcessor(scene.getStage());
        InputUtilities.getInputMultiplexer().addProcessor(this);
        this.scene.show();
    }

    /**
     * @param delta The time in seconds since the last render.
     */
    String debugTxt = "";
    @Override
    public void render(float delta)
    {
        StarSystem selectedSystem = GameInstance.getInstance().getSelectedStarSystem();
        InputUtilities.detectCameraMovement(game.getCameraManager());
        //renderGUI(selectedSystem);

        scene.update();
        scene.act();
        game.getCameraManager().update();
        game.getBatch().setProjectionMatrix(game.getCameraManager().getCurrentCamera().combined);
        GameInstance.getInstance().getShipCircleRenderer().setProjectionMatrix(game.getCameraManager().getCurrentCamera().combined);

        //Draw the star system.
        GameInstance.getInstance().getState().renderSystemMap(game.getBatch(), GameInstance.getInstance().getSelectedStarSystem());

        scene.draw();
    }

    public void renderGUI(StarSystem selectedSystem)
    {
        if(!enterPositionSet)
        {
            game.getCameraManager().getCurrentCamera().position.x = selectedSystem.getCenter().x;
            game.getCameraManager().getCurrentCamera().position.y = selectedSystem.getCenter().y;
            this.enterPositionSet = true;
        }

        //Detect GUI input before we detect planet input.
        stage.act();

        //Draw the GUI.
        stage.draw();
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
        Gdx.app.log("StarSystemScreenDebug ", "Hiding the Star System Screen!");
        InputUtilities.getInputMultiplexer().removeProcessor(scene.getStage());
        InputUtilities.getInputMultiplexer().removeProcessor(this);
        this.scene.hide();
    }

    /**
     *
     */
    @Override
    public void dispose()
    {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        Vector3 touchPos = game.getCameraManager().getCurrentCamera().unproject(new Vector3((float)screenX,
            (float)screenY, 0f));
        float tx = touchPos.x;
        float ty = touchPos.y;
        boolean isPlanetClicked = false;
        StarSystem selectedSystem = GameInstance.getInstance().getSelectedStarSystem();

        //Gdx.app.log("SystemScreen", "Click detected!");
        for(Planet planet : selectedSystem.getPlanets())
        {
            /*
            * Lua:
            * window = scene:getWidgetByID('planet_overview_window')
            * window:setVisible(false)
            * window:isVisible()
            *
            * */

            //If planet is not a star, and it has been touched, we set anyPlanetTouched to true.
            if(!planet.getPlanetClass().isStar() && planet.isTouched(tx, ty))
            {
                GameInstance.getInstance().selectPlanet(planet);
                onPlanetClicked(touchPos, planet);
                isPlanetClicked = true;
                break;
            }
        }
        if(!isPlanetClicked)
        {
            this.onClickCallback.invoke(new LuaValue[]
                {
                    CoerceJavaToLua.coerce(touchPos),
                    CoerceJavaToLua.coerce(selectedSystem),
                    CoerceJavaToLua.coerce(scene),
                    CoerceJavaToLua.coerce(GameInstance.getInstance()),
                    CoerceJavaToLua.coerce(GameInstance.getInstance().getState())
                }
            );
        }

        return true;
    }

    private void onPlanetClicked(Vector3 touchPos, Planet planet)
    {
        Gdx.app.log("StarSystemScreen", "Planet " + planet.id + " touched!");
        GameInstance.getInstance().selectPlanet(planet);
        this.onPlanetClickedCallback.invoke(new LuaValue[]
            {
                CoerceJavaToLua.coerce(touchPos),
                CoerceJavaToLua.coerce(planet),
                CoerceJavaToLua.coerce(scene),
                CoerceJavaToLua.coerce(GameInstance.getInstance()),
                CoerceJavaToLua.coerce(GameInstance.getInstance().getState())
            }
        );
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY)
    {
        OrthographicCamera cam = game.getCameraManager().getCurrentCamera();
        cam.zoom += (amountY);
        return true;
    }
}
