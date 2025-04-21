package org.origin.spacegame.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader.Element;
import org.luaj.vm2.LuaValue;
import org.origin.spacegame.ITaggable;
import org.origin.spacegame.entities.ships.Ship;

public class ScriptedShipState extends ScriptedState<Ship>
{
    protected String tag = "notag";

    public ScriptedShipState(Element xml, LuaValue ctxt)
    {
        this(xml.getAttribute("tag"), ctxt);
    }

    public ScriptedShipState(String tag, LuaValue ctxt)
    {
        super(tag, ctxt);
        this.tag = tag;
        Gdx.app.log("ScriptedShipState", "Ship state " + getTag() + " loaded.");
    }

    public String getTag()
    {
        return tag;
    }
}
