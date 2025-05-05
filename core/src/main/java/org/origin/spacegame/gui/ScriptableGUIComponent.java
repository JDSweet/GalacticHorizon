package org.origin.spacegame.gui;

import com.badlogic.gdx.utils.XmlReader.Element;
import org.luaj.vm2.LuaValue;

public interface ScriptableGUIComponent
{
    public void show();
    public void hide();
    public void update();
    public void readChild(Element child, ScriptedGUIScene scene, LuaValue callbackCtxt);
    public String getDebugID();
    public void enable();
    public void disable();
    public boolean isEnabled();
}
