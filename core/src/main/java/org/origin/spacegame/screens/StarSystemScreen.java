package org.origin.spacegame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.origin.spacegame.SpaceGame;
import org.origin.spacegame.entities.Planet;
import org.origin.spacegame.entities.StarSystem;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.gui.ScriptedGUIScene;
import org.origin.spacegame.input.InputUtilities;
import org.origin.spacegame.utilities.CameraManager;

public class StarSystemScreen implements Screen, InputProcessor
{
    private SpaceGame game;
    boolean enterPositionSet = false;

    //This is used for the System GUI
    //A button to take you back to the galaxy view
    //is at the bottom center of the screen.
    private Stage stage;
    private Label systemNameLabel;
    private TextButton goToGalaxyScreenButton;

    //Planet management window.
    private Window planetManagementWindow;
    TextButton visitSurfaceBtn;
    Label habitabilityLabel;
    Label sizeLabel;
    Label planetClassLabel;

    private ScriptedGUIScene scene;

    public StarSystemScreen(SpaceGame game)
    {
        this.game = game;
        this.scene = new ScriptedGUIScene("star_system_gui.xml");
        //buildGUI();
    }

    private void buildGUI()
    {
        this.stage = new Stage();
        Table systemScreenTable = new Table();
        this.goToGalaxyScreenButton = new TextButton("Galaxy Screen", GameInstance.getInstance().getGuiSkin());
        this.systemNameLabel = new Label("System #-1", GameInstance.getInstance().getGuiSkin());
        goToGalaxyScreenButton.addListener(new ChangeListener()
        {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                game.getCameraManager().setRenderView(CameraManager.RenderView.GALACTIC_VIEW);
                Gdx.app.log("StarSystemScreenDebug", "Galaxy Button Clicked!");
            }
        });
        systemScreenTable.setX(Gdx.graphics.getWidth()/2f);
        systemScreenTable.setY(systemScreenTable.getHeight());
        systemScreenTable.add(systemNameLabel).padBottom(100).padRight(20);
        systemScreenTable.add(goToGalaxyScreenButton).padBottom(100);
        stage.addActor(systemScreenTable);
        stage.addActor(buildPlanetManagementWindow());
    }

    private Window buildPlanetManagementWindow()
    {
        Skin guiSkin = GameInstance.getInstance().getGuiSkin();
        TextButton closeBtn = new TextButton("X", guiSkin);
        closeBtn.setColor(Color.RED);
        closeBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                planetManagementWindow.setVisible(false);
                Gdx.app.log("StarSystemScreen","Planet Overview Window closed manually!");
            }
        });

        this.planetManagementWindow = new Window("Planet X Overview", guiSkin);
        this.planetManagementWindow.setWidth(400);
        this.planetManagementWindow.setHeight(600);
        this.planetManagementWindow.setVisible(false);

        visitSurfaceBtn = new TextButton("Visit Surface", guiSkin);
        habitabilityLabel = new Label("Habitability: X", guiSkin);
        sizeLabel = new Label("Size: X", guiSkin);
        planetClassLabel = new Label("Planet Class: ", guiSkin);

        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.columnLeft().addActor(visitSurfaceBtn);
        verticalGroup.addActor(habitabilityLabel);
        verticalGroup.addActor(sizeLabel);
        verticalGroup.addActor(planetClassLabel);
        planetManagementWindow.top().left().add(verticalGroup);
        //planetManagementWindow.top().left().add(visitSurfaceBtn);
        //planetManagementWindow.center().left().add(habitabilityLabel);
        //planetManagementWindow.add(sizeLabel);

        return planetManagementWindow;
    }

    @Override
    public void show()
    {
        Gdx.app.log("StarSystemScreenDebug", "Showing System View...");
        //updateText();
        //InputUtilities.getInputMultiplexer().addProcessor(stage);
        InputUtilities.getInputMultiplexer().addProcessor(scene.getStage());
        InputUtilities.getInputMultiplexer().addProcessor(this);
    }

    private void updateText()
    {
        systemNameLabel.setText("System #" + GameInstance.getInstance().getSelectedStarSystem().id);
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

        scene.act();
        game.getCameraManager().update();
        game.getBatch().setProjectionMatrix(game.getCameraManager().getCurrentCamera().combined);

        //Draw the star system.
        GameInstance.getInstance().getState().renderSystemView(game.getBatch(), GameInstance.getInstance().getSelectedStarSystem());

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
        InputUtilities.getInputMultiplexer().removeProcessor(stage);
        InputUtilities.getInputMultiplexer().removeProcessor(this);
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
        //Gdx.app.log("SystemScreen", "Click detected!");
        boolean anyPlanetTouched = false;
        for(Planet planet : GameInstance.getInstance().getSelectedStarSystem().getPlanets())
        {
            Vector3 touchPos = game.getCameraManager().getCurrentCamera().unproject(new Vector3((float)screenX,
                (float)screenY, 0f));
            float tx = touchPos.x;
            float ty = touchPos.y;

            //If planet is not touched, then we set anyPlanetTouched to false.
            if(!planet.getPlanetClass().isStar() && planet.isTouched(tx, ty))
            {
                anyPlanetTouched = true;
                onPlanetTouched(tx, ty, planet);
                if(this.planetManagementWindow.isVisible())
                    this.planetManagementWindow.setVisible(true);
                break;
            }
        }
        if(!anyPlanetTouched)
            this.planetManagementWindow.setVisible(false);
        if(planetManagementWindow.isVisible())
            updatePlanetManagementWindow();
        return true;
    }

    private void updatePlanetManagementWindow()
    {
        this.sizeLabel.setText("Size: " + GameInstance.getInstance().getSelectedPlanet().getSize());
        this.planetClassLabel.setText("Planet Class: " + GameInstance.getInstance().getSelectedPlanet().getPlanetClass().getTag());
        this.habitabilityLabel.setText("Habitability: " + Math.round(GameInstance.getInstance().getSelectedPlanet().getHabitability()*100));
    }

    private void onPlanetTouched(float iX, float iY, Planet planet)
    {
        Gdx.app.log("StarSystemScreen", "Planet " + planet.id + " touched!");
        GameInstance.getInstance().selectPlanet(planet);
        this.planetManagementWindow.getTitleLabel().setText("Planet " + planet.id + " Overview");

        if(!planet.getPlanetClass().isStar())
            this.planetManagementWindow.setVisible(true);
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
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
