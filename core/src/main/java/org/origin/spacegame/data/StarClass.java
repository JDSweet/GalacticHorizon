package org.origin.spacegame.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.Gdx;

public class StarClass implements Disposable
{
    private String tag;
    private Texture gfx;
    public float energyOutputMod;
    private String starPlanetClass;
    private float minHabRange, maxHabRange;

    public StarClass(String tag, String starPlanetClass, Texture gfx, float minHabRange, float maxHabRange, float energyOutputMod)
    {
        this.tag = tag;
        this.gfx = gfx;
        this.energyOutputMod = energyOutputMod;
        this.minHabRange = minHabRange;
        this.maxHabRange = maxHabRange;
        if(gfx == null)
        {
            Gdx.app.log("StarClass", "Star Class " + tag + " has a null texture.");
        }
    }

    public Texture getGfx()
    {
        return gfx;
    }

    @Override
    public void dispose()
    {
        gfx.dispose();
    }

    public float getMinHabRange() {
        return minHabRange;
    }

    public void setMinHabRange(float minHabRange) {
        this.minHabRange = minHabRange;
    }

    public float getMaxHabRange() {
        return maxHabRange;
    }

    public void setMaxHabRange(float maxHabRange) {
        this.maxHabRange = maxHabRange;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
