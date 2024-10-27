package org.origin.spacegame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.XmlReader;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import org.origin.spacegame.Constants;

public class ScriptedGUIScene  implements ScriptableGUIComponent
{
    protected LuaValue globals;
    protected XmlReader reader;
    protected String luaCallbackFile;
    protected Stage stage;
    protected String debugTag;
    Array<String> validGUIComponents;
    public Array<ScriptableGUIComponent> children;

    //Each scripted scene has a list of GUI components referred to by unique IDs stored within.
    protected ArrayMap<String, ScriptableGUIComponent> components;

    public ScriptedGUIScene(String xmlFile)
    {
        this.debugTag = getClass().getSimpleName() + " Debug";
        validGUIComponents = new Array<String>();
        validGUIComponents.addAll(
            "TextButton",
            "Table",
            "VerticalGroup",
            "Window",
            "Label",
            "Image"
        );
        children = new Array<ScriptableGUIComponent>();

        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(Gdx.files.internal(Constants.FileConstants.GUI_XML_DIR + xmlFile));
        if(root.hasAttribute("callback_file"))
            this.luaCallbackFile = root.getAttribute("callback_file");
        else
            this.luaCallbackFile = Constants.FileConstants.DEFAULT_GUI_CALLBACK_FILE;

        //Now we create our Lua environment for this GUI Scene.
        this.globals = JsePlatform.standardGlobals();
        this.components = new ArrayMap<String, ScriptableGUIComponent>();
        this.stage = new Stage();

        //We're going to compile our lua file and execute the code.
        //This ensures that components on this scene have a valid
        globals.get("dofile").call(Gdx.files.internal(Constants.FileConstants.GUI_SCRIPTS_DIR + luaCallbackFile).path());

        for(int i = 0; i < root.getChildCount(); i++)
        {
            XmlReader.Element child = root.getChild(i);
            if(validGUIComponents.contains(child.getName(), false))
                readChild(child, this, globals);
            else
                Gdx.app.log(debugTag, "Component " + child.getName() + " is not a valid component.");
        }

        registerAll();
    }

    public void enableWidget(String widgetID)
    {

    }

    public void disableWidget(String widgetID)
    {

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

    public ScriptableGUIComponent getWidgetByID(String id)
    {
        return components.get(id);
    }

    public void registerWidgetByID(String id, ScriptableGUIComponent component)
    {
        this.components.put(id, component);
    }

    private void registerAll()
    {
        for(ScriptableGUIComponent c : children)
        {
            if(!this.components.containsKey(c.getDebugID()))
            {
                registerWidgetByID(c.getDebugID(), c);
            }
        }
    }

    @Override
    public void readChild(XmlReader.Element child, ScriptedGUIScene scene, LuaValue ctxt)
    {
        if(child.getName().equals("TextButton"))
        {
            ScriptedTextButton button = new ScriptedTextButton(child, this.globals, this);
            Gdx.app.log(debugTag, "Text button " + button.getClass().getTypeName() + " created at (" + button.widget.getX() + ", " + button.widget.getY() + ") ");
            stage.addActor(button.widget);
            children.add(button);
        }
        if(child.getName().equals("Label"))
        {
            ScriptedLabel label = new ScriptedLabel(child, this.globals, scene);
            Gdx.app.log(debugTag, "Label " + label.getClass().getTypeName() + " created at (" + label.widget.getX() + ", " + label.widget.getY() + ") ");
            stage.addActor(label.widget);
            children.add(label);
        }
        if(child.getName().equals("Window"))
        {
            ScriptedWindow window = new ScriptedWindow(child, ctxt, this);
            stage.addActor(window.widget);
            children.add(window);
        }
        if(child.getName().equals("Table"))
        {

        }
    }

    public Stage getStage()
    {
        return this.stage;
    }

    @Override
    public String getDebugID()
    {
        return "root";
    }

    private boolean enabled = true;
    @Override
    public void enable()
    {
        enabled = true;
        show();
    }

    @Override
    public void disable()
    {
        enabled = false;
        hide();
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    public void act()
    {
        stage.act();
    }

    public void draw()
    {
        stage.draw();
    }
}
