package org.origin.spacegame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.origin.spacegame.game.GameInstance;

//This class reads GUI elements into the game.
public class ScriptedGUIScene implements ScriptableGUIComponent
{
    protected LuaValue globals;
    protected XmlReader reader;
    protected String guiScriptFolder = "assets/gfx/gui/scripts/";
    protected String xmlDefinesFolder = "assets/gfx/gui/xml/";
    protected String defaultCallbackFile = "00_callbacks.lua";
    protected String luaCallbackFile;
    protected Stage stage;
    protected String debugTag;
    Array<String> validGUIComponents;
    public Array<ScriptableGUIComponent> children;

    //Each scripted scene has a list of GUI components referred to by unique IDs stored within.
    protected ArrayMap<String, Actor> components;

    public ScriptedGUIScene(String xmlFile)
    {
        this.debugTag = getClass().getSimpleName() + " Debug";
        validGUIComponents = new Array<String>();
        validGUIComponents.addAll(
            "TextButton",
            "Table",
            "VerticalGroup",
            "Window",
            "Label"
        );
        children = new Array<ScriptableGUIComponent>();

        XmlReader reader = new XmlReader();
        Element root = reader.parse(Gdx.files.internal(xmlDefinesFolder + xmlFile));
        if(root.hasAttribute("callback_file"))
            this.luaCallbackFile = root.getAttribute("callback_file");
        else
            this.luaCallbackFile = defaultCallbackFile;

        //Now we create our Lua environment for this GUI Scene.
        this.globals = JsePlatform.standardGlobals();
        this.components = new ArrayMap<String, Actor>();
        this.stage = new Stage();

        //We're going to compile our lua file and execute the code.
        //This ensures that components on this scene have a valid
        globals.get("dofile").call(Gdx.files.internal(guiScriptFolder + luaCallbackFile).path());

        for(int i = 0; i < root.getChildCount(); i++)
        {
            Element child = root.getChild(i);
            if(validGUIComponents.contains(child.getName(), false))
                readLegitimateChild(child);
            else
                Gdx.app.log(debugTag, "Component " + child.getName() + " is not a valid component.");
        }
    }

    public Stage getStage()
    {
        return stage;
    }

    //Called when the scene is shown.
    @Override
    public void show()
    {
        for(ScriptableGUIComponent child : children)
        {
            child.show();
        }
    }

    //Called when the scene is hidden.
    @Override
    public void hide()
    {
        for(ScriptableGUIComponent child : children)
        {
            child.hide();
        }
    }

    public void act()
    {
        stage.act();
    }

    public void draw()
    {
        stage.draw();
    }

    protected void readLegitimateChild(Element child)
    {
        if(child.getName().equals("TextButton"))
        {
            ScriptedTextButton button = new ScriptedTextButton(child, this.globals);
            Gdx.app.log(debugTag, "Text button " + button.getName() + " created at (" + button.getX() + ", " + button.getY() + ") ");
            stage.addActor(button);
        }
        if(child.getName().equals("Label"))
        {
            ScriptedLabel label = new ScriptedLabel(child, this.globals);
            Gdx.app.log(debugTag, "Label " + label.getName() + " created at (" + label.getX() + ", " + label.getY() + ") ");
            stage.addActor(label);
        }
    }
}
