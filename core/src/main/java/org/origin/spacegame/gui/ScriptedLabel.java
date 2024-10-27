package org.origin.spacegame.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import org.luaj.vm2.LuaValue;
import org.origin.spacegame.game.GameInstance;

public class ScriptedLabel extends ScriptableWidgetContainer
{
    public ScriptedLabel(XmlReader.Element self, LuaValue ctxt, ScriptedGUIScene scene)
    {
        super(self, ctxt, new Label("Default Text", GameInstance.getInstance().getSkin("uiskin.json")), scene);
        Label label_widget = (Label)widget;
        for(ObjectMap.Entry<String, String> entry : self.getAttributes())
        {
            switch(entry.key)
            {
                case "text":
                    label_widget.setText(entry.value);
                    break;
            }
        }
    }
}
