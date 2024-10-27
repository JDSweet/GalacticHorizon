package org.origin.spacegame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import org.luaj.vm2.LuaValue;
import org.origin.spacegame.game.GameInstance;

public class ScriptedWindow extends ScriptableWidgetContainer
{
    public ScriptedWindow(XmlReader.Element self, LuaValue ctxt, ScriptedGUIScene scene)
    {
        super(self, ctxt, new Window(self.getAttribute("title"), GameInstance.getInstance().getSkin("uiskin.json")), scene);
        this.children = new Array<ScriptableGUIComponent>();
        this.debugTag = getClass().getSimpleName() + " Debug";
        this.scene = scene;
        this.debugID = self.getAttribute("id");

        scene.registerWidgetByID(debugID, this);

        if(self.hasAttribute("padding_up"))
            ((Window)widget).padTop(Gdx.graphics.getHeight() * Float.parseFloat(self.getAttribute("padding_up")));
        if(self.hasAttribute("padding_down"))
            ((Window)widget).padBottom(Gdx.graphics.getHeight() * Float.parseFloat(self.getAttribute("padding_down")));
        if(self.hasAttribute("padding_left"))
            ((Window)widget).padLeft(Gdx.graphics.getWidth() * Float.parseFloat(self.getAttribute("padding_left")));
        if(self.hasAttribute("padding_right"))
            ((Window)widget).padRight(Gdx.graphics.getWidth() * Float.parseFloat(self.getAttribute("padding_right")));
        if(self.hasAttribute("alignment"))
        {
            switch(self.getAttribute("alignment"))
            {
                case "top-left":
                    ((Window)widget).top().left();
                    break;
                case "bottom-left":
                    ((Window)widget).bottom().left();
                    break;
                case "center-left":
                    ((Window)widget).center().left();
                    break;
                case "top-right":
                    ((Window)widget).top().right();
                    break;
                case "bottom-right":
                    ((Window)widget).bottom().right();
                    break;
                case "center-right":
                    ((Window)widget).center().right();
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
                    ((Window)widget).top().left();
                    break;
                case "bottom-left":
                    ((Window)widget).bottom().left();
                    break;
                case "center-left":
                    ((Window)widget).center().left();
                    break;
                case "top-right":
                    ((Window)widget).top().right();
                    break;
                case "bottom-right":
                    ((Window)widget).bottom().right();
                    break;
                case "center-right":
                    ((Window)widget).center().right();
                    break;
                default:
                    Gdx.app.log(debugTag, "Unidentified alignment " + self.getAttribute("alignment"));
            }
        }

        for(int i = 0; i < self.getChildCount(); i++)
        {
            readChild(self.getChild(i), scene, ctxt);
        }
    }

    @Override
    public void readChild(XmlReader.Element child, ScriptedGUIScene scene, LuaValue ctxt)
    {
        if(child.getName().equals("TextButton"))
        {
            ScriptedTextButton button = new ScriptedTextButton(child, ctxt, scene);
            Gdx.app.log(debugTag, "Text button " + button.getClass().getTypeName() + " created at (" + button.widget.getX() + ", " + button.widget.getY() + ") ");
            //stage.addActor(button);
            ((Window)widget).addActor(button.widget);
            children.add((ScriptableGUIComponent) button);
        }
        if(child.getName().equals("Label"))
        {
            ScriptedLabel label = new ScriptedLabel(child, ctxt, scene);
            Gdx.app.log(debugTag, "Label " + label.getClass().getTypeName() + " created at (" + label.widget.getX() + ", " + label.widget.getY() + ") ");
            //stage.addActor(label);
            ((Window)widget).add(label.widget);
            children.add(label);
        }
        if(child.getName().equals("Window"))
        {
            ScriptedWindow window = new ScriptedWindow(child, ctxt, scene);
            //stage.addActor(window);
            ((Window)widget).add(window.widget);
            children.add(window);
        }
        if(child.getName().equals("VerticalGroup"))
        {
            ScriptedVerticalGroup group = new ScriptedVerticalGroup(child, ctxt, scene);
            ((Window)widget).add(group.widget);
            children.add(group);
        }
    }
}
