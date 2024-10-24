package org.origin.spacegame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.origin.spacegame.game.GameInstance;

public class ScriptedVerticalGroup extends VerticalGroup implements ScriptableGUIComponent
{
    private String debugID;
    private String debugTag;
    private ScriptedGUIScene scene;
    private Array<ScriptableGUIComponent> children;

    private LuaValue onClickCallbackFunc;
    private LuaValue onShowCallbackFunc;
    private LuaValue onHideCallbackFunc;

    public ScriptedVerticalGroup(XmlReader.Element self, LuaValue ctxt, ScriptedGUIScene scene)
    {
        //super(self.getAttribute("title"), GameInstance.getInstance().getSkin(self.getAttribute("skin")));
        super();

        this.children = new Array<ScriptableGUIComponent>();
        this.debugTag = getClass().getSimpleName() + " Debug";
        this.scene = scene;
        this.debugID = self.getAttribute("id");
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

        if(ctxt == null)
            Gdx.app.log(debugTag, "The Lua Context is null.");

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
        if(self.hasAttribute("alignment"))
        {
            switch(self.getAttribute("alignment"))
            {
                case "top-left":
                    columnTop().columnLeft();
                    break;
                case "bottom-left":
                    columnBottom().columnLeft();
                    break;
                case "center-left":
                    columnCenter().columnLeft();
                    break;
                case "top-right":
                    columnTop().columnRight();
                    break;
                case "bottom-right":
                    columnBottom().columnRight();
                    break;
                case "center-right":
                    columnCenter().columnRight();
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
                    columnTop().columnLeft();
                    break;
                case "bottom-left":
                    columnBottom().columnLeft();
                    break;
                case "center-left":
                    columnCenter().columnLeft();
                    break;
                case "top-right":
                    columnTop().columnRight();
                    break;
                case "bottom-right":
                    columnBottom().columnRight();
                    break;
                case "center-right":
                    columnCenter().columnRight();
                    break;
                default:
                    Gdx.app.log(debugTag, "Unidentified alignment " + self.getAttribute("align"));
            }
        }
        for(int i = 0; i < self.getChildCount(); i++)
        {
            readChild(self.getChild(i), scene, ctxt);
        }
    }

    @Override
    public void show()
    {
        for(ScriptableGUIComponent child : children)
        {
            child.show();
        }
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
        for(ScriptableGUIComponent child : children)
        {
            child.hide();
        }
        if(this.onHideCallbackFunc != null)
            onHideCallbackFunc.invoke(CoerceJavaToLua.coerce(this),
                CoerceJavaToLua.coerce(GameInstance.getInstance()),
                CoerceJavaToLua.coerce(GameInstance.getInstance().getState()));
        else
            Gdx.app.log(debugTag, "No on_hide callback function defined for " + debugTag);
    }

    @Override
    public void readChild(XmlReader.Element child, ScriptedGUIScene scene, LuaValue ctxt)
    {
        if(child.getName().equals("TextButton"))
        {
            ScriptedTextButton button = new ScriptedTextButton(child, ctxt, scene);
            Gdx.app.log(debugTag, "Text button " + button.getClass().getTypeName() + " created at (" + button.getX() + ", " + button.getY() + ") ");
            //stage.addActor(button);
            this.addActor(button);
            children.add(button);
        }
        if(child.getName().equals("Label"))
        {
            ScriptedLabel label = new ScriptedLabel(child, ctxt, scene);
            Gdx.app.log(debugTag, "Label " + label.getClass().getTypeName() + " created at (" + label.getX() + ", " + label.getY() + ") ");
            //stage.addActor(label);
            this.addActor(label);
            children.add(label);
        }
        if(child.getName().equals("Window"))
        {
            ScriptedWindow window = new ScriptedWindow(child, ctxt, scene);
            //stage.addActor(window);
            this.addActor(window);
            children.add(window);
        }
        if(child.getName().equals("Table"))
        {

        }
    }

    @Override
    public String getDebugID() {
        return debugID;
    }
}
