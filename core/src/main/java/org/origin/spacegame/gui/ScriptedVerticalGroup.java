package org.origin.spacegame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.XmlReader;
import org.luaj.vm2.LuaValue;
//import org.origin.spacegame.gui.ScriptedWindow;

public class ScriptedVerticalGroup extends ScriptableWidgetContainer
{
    public ScriptedVerticalGroup(XmlReader.Element self, LuaValue ctxt, ScriptedGUIScene scene)
    {
        super(self, ctxt, new VerticalGroup(), scene);
        VerticalGroup verticalGroup = (VerticalGroup) widget;
        if(self.hasAttribute("alignment"))
        {
            switch(self.getAttribute("alignment"))
            {
                case "top-left":
                    verticalGroup.columnTop().columnLeft();
                    break;
                case "bottom-left":
                    verticalGroup.columnBottom().columnLeft();
                    break;
                case "center-left":
                    verticalGroup.columnCenter().columnLeft();
                    break;
                case "top-right":
                    verticalGroup.columnTop().columnRight();
                    break;
                case "bottom-right":
                    verticalGroup.columnBottom().columnRight();
                    break;
                case "center-right":
                    verticalGroup.columnCenter().columnRight();
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
                    verticalGroup.columnTop().columnLeft();
                    break;
                case "bottom-left":
                    verticalGroup.columnBottom().columnLeft();
                    break;
                case "center-left":
                    verticalGroup.columnCenter().columnLeft();
                    break;
                case "top-right":
                    verticalGroup.columnTop().columnRight();
                    break;
                case "bottom-right":
                    verticalGroup.columnBottom().columnRight();
                    break;
                case "center-right":
                    verticalGroup.columnCenter().columnRight();
                    break;
                default:
                    Gdx.app.log(debugTag, "Unidentified alignment " + self.getAttribute("align"));
            }
        }
        for(int i = 0; i < self.getChildCount(); i++)
        {
            readChild(self.getChild(i), scene, ctxt);
        }

        /*for(ObjectMap.Entry<String, String> entry : self.getAttributes())
        {
            switch(entry.key)
            {
                case "text":
                    verticalGroup.setText(entry.value);
                    break;
            }
        }*/
    }

    @Override
    public void readChild(XmlReader.Element child, ScriptedGUIScene scene, LuaValue ctxt)
    {
        if(child.getName().equals("TextButton"))
        {
            ScriptedTextButton button = new ScriptedTextButton(child, ctxt, scene);
            Gdx.app.log(debugTag, "Text button " + button.getClass().getTypeName() + " created at (" + button.widget.getX() + ", " + button.widget.getY() + ") ");
            //stage.addActor(button);
            ((VerticalGroup)widget).addActor(button.widget);
            children.add(button);
        }
        if(child.getName().equals("Label"))
        {
            ScriptedLabel label = new ScriptedLabel(child, ctxt, scene);
            Gdx.app.log(debugTag, "Label " + label.getClass().getTypeName() + " created at (" + label.widget.getX() + ", " + label.widget.getY() + ") ");
            //stage.addActor(label);
            ((VerticalGroup)widget).addActor(label.widget);
            children.add(label);
        }
        if(child.getName().equals("Window"))
        {
            ScriptedWindow window = new ScriptedWindow(child, ctxt, scene);
            //stage.addActor(window);
            ((VerticalGroup)widget).addActor(window.widget);
            children.add(window);
        }
        if(child.getName().equals("Table"))
        {

        }
    }
}
