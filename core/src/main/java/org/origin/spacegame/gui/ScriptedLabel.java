package org.origin.spacegame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.XmlReader.Element;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.origin.spacegame.game.GameInstance;


public class ScriptedLabel extends Label implements ScriptableGUIComponent
{
    private String debugTag;
    private LuaValue onShowCallbackFunc;
    private LuaValue onHideCallbackFunc;
    private LuaValue onCreateCallbackFunc;

    private ScriptedGUIScene scene;

    public ScriptedLabel(Element self, LuaValue ctxt)
    {
        super(self.getAttribute("text"), GameInstance.getInstance().getSkin(self.getAttribute("skin")));
        this.debugTag = getClass().getTypeName() + " Debug";
        this.scene = scene;

        if(ctxt == null)
            Gdx.app.log(debugTag, "The Lua Context is null.");

        if(self.hasAttribute("on_create") && ctxt != null)
            this.onCreateCallbackFunc = ctxt.get(self.getAttribute("on_create"));
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
        if(self.hasAttribute("font_size"))
            setFontScale(Float.parseFloat(self.getAttribute("font_size")));
        if(self.hasAttribute("font_scale"))
            setFontScale(Float.parseFloat(self.getAttribute("font_scale")));
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
    }

    @Override
    public void readChild(Element child, ScriptedGUIScene scene, LuaValue ctxt)
    {

    }
}
