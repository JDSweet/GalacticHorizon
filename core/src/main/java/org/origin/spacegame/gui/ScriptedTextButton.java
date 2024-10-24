package org.origin.spacegame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.XmlReader;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.origin.spacegame.game.GameInstance;

//This button is defined in XML and receives a reference to the GUI script engine for callback referencing.
//It gets skins from the GameInstance's list of skins.
/*
*
* The ScriptedTextButton has several attributes in XML:
*   text - The default string of text that is going to be displayed.
*   on_click - The function that is going to be called by this button when it is pressed.
*   skin - The GUI skin this button will use.
*   x - The optional X coordinate of this button on the screen (as a fraction of the screen width).
*       This is overridden by any container this element is in that controls/manipulates its position.
*   y - The optional y coordinate of this button on the screen (as a fraction of the screen's width).
*       This is overridden by any container this element is in that controls/manipulates its position.
*   width - See above.
*   height - See above.
*   padding_up - If this widget is part of a layer or group, then this controls the up padding.
*   padding_down - If this widget is part of a layer or group, then this controls the down padding.
*   padding_left - If this widget is part of a layer or group, then this controls the left padding.
*   padding_right - If this widget is part of a layer or group, then this controls the right padding.
* */
public class ScriptedTextButton extends TextButton implements ScriptableGUIComponent
{
    private LuaValue onClickCallbackFunc;
    private LuaValue onShowCallbackFunc;
    private LuaValue onHideCallbackFunc;

    private ScriptedGUIScene scene;
    private String debugID;

    private String debugTag;

    public ScriptedTextButton(XmlReader.Element self, LuaValue ctxt, ScriptedGUIScene scene)
    {
        super(self.getAttribute("text"), GameInstance.getInstance().getSkin(self.getAttribute("skin")));
        this.debugID = self.getAttribute("id");
        this.debugTag = getClass().getSimpleName() + " " + debugID;
        this.scene = scene;
        if(this.scene != null)
            scene.registerWidgetByID(debugID, this);

        if(self.hasAttribute("visible"))
        {
            String attribVal = self.getAttribute("visible");
            if(attribVal.equalsIgnoreCase("yes"))
                attribVal = "true";
            else if(attribVal.equalsIgnoreCase("no"))
                attribVal = "false";
            boolean visibility = Boolean.parseBoolean(attribVal);
            setVisible(visibility);
        }

        if(self.hasAttribute("on_click") && ctxt != null)
            this.onClickCallbackFunc = ctxt.get(self.getAttribute("on_click"));
        else
            Gdx.app.log(debugTag, "Either the Lua context has not been set, or on_click has not been defined.");
        if(self.hasAttribute("on_show") && ctxt != null)
            this.onShowCallbackFunc = ctxt.get(self.getAttribute("on_show"));
        else
            Gdx.app.log(debugTag, "Either the Lua context has not been set, or on_show has not been defined.");
        if(self.hasAttribute("on_hide") && ctxt != null)
            this.onHideCallbackFunc = ctxt.get(self.getAttribute("on_hide"));
        else
            Gdx.app.log(debugTag, "Either the Lua context has not been set, or on_hide has not been defined.");
        if(self.hasAttribute("x"))
            setX(Gdx.graphics.getWidth() * Float.parseFloat(self.getAttribute("x")));
        if(self.hasAttribute("X"))
            setX(Gdx.graphics.getWidth() * Float.parseFloat(self.getAttribute("X")));
        if(self.hasAttribute("y"))
            setY(Gdx.graphics.getHeight() * Float.parseFloat(self.getAttribute("y")));
        if(self.hasAttribute("Y"))
            setY(Gdx.graphics.getHeight() * Float.parseFloat(self.getAttribute("y")));
        if(self.hasAttribute("width"))
            setWidth(Gdx.graphics.getWidth() * Float.parseFloat(self.getAttribute("width")));
        if(self.hasAttribute("height"))
            setHeight(Gdx.graphics.getHeight() * Float.parseFloat(self.getAttribute("height")));
        if(self.hasAttribute("padding_up"))
            padTop(Gdx.graphics.getHeight() * Float.parseFloat(self.getAttribute("padding_up")));
        if(self.hasAttribute("padding_down"))
            padBottom(Gdx.graphics.getHeight() * Float.parseFloat(self.getAttribute("padding_down")));
        if(self.hasAttribute("padding_left"))
            padLeft(Gdx.graphics.getWidth() * Float.parseFloat(self.getAttribute("padding_left")));
        if(self.hasAttribute("padding_right"))
            padRight(Gdx.graphics.getWidth() * Float.parseFloat(self.getAttribute("padding_right")));

        addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor)
            {
                //The widget, this GameInstance, and the GameState are passed to the callback.
                if(ctxt != null && onClickCallbackFunc != null)
                {
                    onClickCallbackFunc.invoke(
                        CoerceJavaToLua.coerce(this),
                        CoerceJavaToLua.coerce(GameInstance.getInstance()),
                        CoerceJavaToLua.coerce(GameInstance.getInstance().getState())
                    );
                }
            }
        });
    }

    /**
     *
     */
    @Override
    public void show()
    {
        if(this.onShowCallbackFunc != null)
            onShowCallbackFunc.invoke(CoerceJavaToLua.coerce(this),
                CoerceJavaToLua.coerce(GameInstance.getInstance()),
                CoerceJavaToLua.coerce(GameInstance.getInstance().getState()));
        else
            Gdx.app.log(debugTag, "No on_show callback function defined for " + debugTag);
        enable();
    }

    /**
     *
     */
    @Override
    public void hide()
    {
        if(this.onHideCallbackFunc != null)
            onHideCallbackFunc.invoke(CoerceJavaToLua.coerce(this),
                CoerceJavaToLua.coerce(GameInstance.getInstance()),
                CoerceJavaToLua.coerce(GameInstance.getInstance().getState()));
        else
            Gdx.app.log(debugTag, "No on_hide callback function defined for " + debugTag);
        disable();
    }

    @Override
    public void readChild(XmlReader.Element child, ScriptedGUIScene scene, LuaValue ctxt)
    {

    }

    @Override
    public String getDebugID() {
        return this.debugID;
    }

    boolean enabled = true;
    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
