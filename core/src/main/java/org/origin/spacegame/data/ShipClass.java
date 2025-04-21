package org.origin.spacegame.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/*
* Examples of ship class include (but aren't necessarily limited to):
* Fighter, frigate, bomber, cruiser, battleship,
* destroyer, carrier, troop transport, free merchant,
* colony ship, science ship, etc.
* */
public class ShipClass implements Disposable
{
    private String tag;
    private Texture gfx;
    private Vector2 maxVel;
    private ShipClassType type;
    private float maxAccel;
    private float maxHP;
    private float spriteOffset = 90f;

    public ShipClass(String tag, Texture gfx, Vector2 maxVel, float maxAccel, float maxHP, float spriteOffset, ShipClassType type)
    {
        this.tag = tag;
        this.gfx = gfx;
        this.maxVel = maxVel;
        this.type = type;
        this.maxAccel = maxAccel;
        this.maxHP = maxHP;
        this.spriteOffset = spriteOffset;
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

    public float getSpriteOffset()
    {
        return this.spriteOffset;
    }

    public float getMaxAcceleration()
    {
        return this.maxAccel;
    }

    public float getMaxHP()
    {
        return this.maxHP;
    }

    @Override
    public void dispose()
    {
        this.gfx.dispose();
    }

    public float getCombatRange()
    {
        return 10f;
    }

    // This essentially represents whether ships of this ship class are civilian or military.
    // Which can determine how the AI will react to, for example,
    // being fired on by a military vessel.
    public enum ShipClassType
    {
        MILITARY, CIVILIAN, UNDEFINED
    }
}
