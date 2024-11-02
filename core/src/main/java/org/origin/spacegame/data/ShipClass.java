package org.origin.spacegame.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/*
* Examples of ship class include (but aren't necessarily limited to):
* Fighter, frigate, bomber, cruiser, battleship,
* destroyer, carrier, troop transport, free merchant,
* colony ship, science ship, etc.
* */
public class ShipClass
{
    private String tag;
    private Texture gfx;
    private Vector2 maxVel;
    private ShipClassType type;

    public ShipClass(String tag, Texture gfx, Vector2 maxVel, ShipClassType type)
    {
        this.tag = tag;
        this.gfx = gfx;
        this.maxVel = maxVel;
        this.type = type;
    }

    public String getTag()
    {
        return this.tag;
    }

    public Texture getGfx()
    {
        return this.gfx;
    }

    public Vector2 getMaxVel()
    {
        return this.maxVel;
    }

    public ShipClassType getType()
    {
        return this.type;
    }

    // This essentially represents whether ships of this ship class are civilian or military.
    // Which can determine how the AI will react to, for example,
    // being fired on by a military vessel.
    public enum ShipClassType
    {
        MILITARY, CIVILIAN, UNDEFINED
    }
}
