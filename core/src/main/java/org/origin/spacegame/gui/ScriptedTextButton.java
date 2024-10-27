package org.origin.spacegame.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.origin.spacegame.game.GameInstance;

public class ScriptedTextButton extends ScriptableWidgetContainer
{
    public ScriptedTextButton(XmlReader.Element self, LuaValue ctxt, ScriptedGUIScene scene)
    {
        super(self, ctxt, new TextButton("Default Text", GameInstance.getInstance().getSkin("uiskin.json")), scene);
        TextButton btn_widget = (TextButton) widget;
        LuaValue onClick = this.onClickCallbackFunc;
        btn_widget.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onClick.invoke(new LuaValue[]{
                    CoerceJavaToLua.coerce(widget),
                    CoerceJavaToLua.coerce(GameInstance.getInstance()),
                    CoerceJavaToLua.coerce(GameInstance.getInstance().getState())
                });
            }
        });
        for(ObjectMap.Entry<String, String> entry : self.getAttributes())
        {
            switch(entry.key)
            {
                case "text":
                    btn_widget.setText(entry.value);
                    break;
            }
        }
    }
}
