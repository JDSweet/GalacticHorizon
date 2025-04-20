package org.origin.spacegame.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.origin.spacegame.game.GameInstance;

/**
 * Creates a scriptable select box. This supports up to 5 items in its underlying list.
 * Also note: onClick is called when a new value is selected from the list, not when the user clicks the dropdown.
 */
public class ScriptedSelectBox extends ScriptableWidgetContainer
{
    public StringSelectBox selectBox = null;

    public ScriptedSelectBox(XmlReader.Element self, LuaValue ctxt, ScriptedGUIScene scene)
    {
        super(self, ctxt, new StringSelectBox(GameInstance.getInstance().getSkin("uiskin.json")), scene);
        StringSelectBox selectBox = (StringSelectBox)widget;
        if(widget == null)
            Gdx.app.log("ScriptedSelectBox Widget Debug", "widget not set");
        LuaValue onClick = this.onClickCallbackFunc;
        ScriptedSelectBox box = this;
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                widget = actor;
                onClick.invoke(new LuaValue[]{
                    CoerceJavaToLua.coerce(box),
                    CoerceJavaToLua.coerce(GameInstance.getInstance()),
                    CoerceJavaToLua.coerce(GameInstance.getInstance().getState())
                });
            }
        });

        Array<String> items = new Array<>(true, 5);
        for(ObjectMap.Entry<String, String> entry : self.getAttributes())
        {
            switch(entry.key)
            {
                case "option1":
                case "option2":
                case "option3":
                case "option4":
                case "option5":
                    items.add(self.getAttribute(entry.key));
                    break;
            }
        }
        //items.sort();
        String[] rItems = new String[items.size];
        for(int i = 0; i < items.size; i++)
        {
            switch(items.get(i))
            {
                case "option1":
                    rItems[0] = items.get(i);
                    break;
                case "option2":
                    rItems[1] = items.get(i);
                    break;
                case "option3":
                    rItems[2] = items.get(i);
                    break;
                case "option4":
                    rItems[3] = items.get(i);
                    break;
                case "option5":
                    rItems[4] = items.get(i);
                    break;
            }
            rItems[i] = items.get(i);
        }
        selectBox.setItems(rItems);
        selectBox.setSelectedIndex(0);
    }

    public StringSelectBox getSelectBox()
    {
        return (StringSelectBox)widget;
    }

    public String getSelected()
    {
        return ((StringSelectBox)widget).getSelected();
    }
}
