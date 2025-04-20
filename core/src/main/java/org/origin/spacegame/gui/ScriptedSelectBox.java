package org.origin.spacegame.gui;

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
 */
public class ScriptedSelectBox extends ScriptableWidgetContainer
{
    public ScriptedSelectBox(XmlReader.Element self, LuaValue ctxt, ScriptedGUIScene scene)
    {
        super(self, ctxt, new SelectBox<String>(GameInstance.getInstance().getSkin("uiskin.json")), scene);
        SelectBox<String> selectBox = (SelectBox<String>)widget;
        LuaValue onClick = this.onClickCallbackFunc;
        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onClick.invoke(new LuaValue[]{
                    CoerceJavaToLua.coerce(widget),
                    CoerceJavaToLua.coerce(GameInstance.getInstance()),
                    CoerceJavaToLua.coerce(GameInstance.getInstance().getState())
                });
            }
        });

        Array<String> items = new Array<>();
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
        String[] rItems = new String[items.size];
        selectBox.setItems(rItems);
    }
}
