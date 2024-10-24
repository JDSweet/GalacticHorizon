package org.origin.spacegame.gui;

import com.badlogic.gdx.utils.XmlReader;
import org.luaj.vm2.LuaValue;

public interface ScriptableGUIComponent
{
    public void show();
    public void hide();
    public void readChild(XmlReader.Element child, ScriptedGUIScene scene, LuaValue callbackCtxt);
}
