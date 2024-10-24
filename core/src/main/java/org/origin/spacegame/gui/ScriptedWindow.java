package org.origin.spacegame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.origin.spacegame.game.GameInstance;

public class ScriptedWindow extends Window implements ScriptableGUIComponent
{
    private LuaValue onClickCallbackFunc;
    private LuaValue onShowCallbackFunc;
    private LuaValue onHideCallbackFunc;
    private String debugTag;

    private ScriptedGUIScene scene;

    public ScriptedWindow(XmlReader.Element self, LuaValue ctxt, ScriptedGUIScene scene)
    {
        super(self.getAttribute("text"), GameInstance.getInstance().getSkin(self.getAttribute("skin")));
        this.debugTag = getClass().getTypeName() + " Debug";
        this.scene = scene;

        if(ctxt == null)
            Gdx.app.log(debugTag, "The Lua Context is null.");

        if(self.hasAttribute("on_click") && ctxt != null)
            this.onClickCallbackFunc = ctxt.get(self.getAttribute("on_click"));
        else
            Gdx.app.log(debugTag, "Either the Lua context has not been set, or on_click has not been defined.");
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
        if(self.hasAttribute("alignment"))
        {
            switch(self.getAttribute("alignment"))
            {
                case "top-left":
                    top().left();
                    break;
                case "bottom-left":
                    bottom().left();
                    break;
                case "center-left":
                    center().left();
                    break;
                case "top-right":
                    top().right();
                    break;
                case "bottom-right":
                    bottom().right();
                    break;
                case "center-right":
                    center().right();
                    break;
                default:
                    Gdx.app.log(debugTag, "Unidentified alignment " + self.getAttribute("alignment"));
            }
        }

        if(self.hasAttribute("align"))
        {
            switch(self.getAttribute("align"))
            {
                case "top-left":
                    top().left();
                    break;
                case "bottom-left":
                    bottom().left();
                    break;
                case "center-left":
                    center().left();
                    break;
                case "top-right":
                    top().right();
                    break;
                case "bottom-right":
                    bottom().right();
                    break;
                case "center-right":
                    center().right();
                    break;
                default:
                    Gdx.app.log(debugTag, "Unidentified alignment " + self.getAttribute("alignment"));
            }
        }

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
