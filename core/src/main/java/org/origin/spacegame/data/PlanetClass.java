package org.origin.spacegame.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import org.origin.spacegame.generation.OrbitalZone;
import org.origin.spacegame.utilities.StringToType;

public class PlanetClass implements Disposable
{
    private String tag;
    private Texture gfx;
    private float minHabitability;
    private float maxHabitability;
    private int minSize;
    private int maxSize;
    //boolean onlySpawnsInHabitableZone;
    boolean isStar;
    boolean canColonize;
    OrbitalZone spawningZone;
    float meltRadius, habRadius, freezeRadius;

    public PlanetClass(String tag, Texture gfx, float minHabitability, float maxHabitability, int minSize,
                       int maxSize, String spawningZone, boolean isStar, boolean canColonize, float meltRadius, float habRadius, float freezeRadius)
    {
        this.tag = tag;
        this.gfx = gfx;
        this.minHabitability = minHabitability;
        this.maxHabitability = maxHabitability;
        this.minSize = minSize;
        this.maxSize = maxSize;
        //this. onlySpawnsInHabitableZone = onlySpawnsInHabitableZone;
        this.spawningZone = StringToType.readOrbitalZone(spawningZone);
        this.isStar = isStar;
        this.canColonize = canColonize;
        this.meltRadius = meltRadius;
        this.habRadius = habRadius;
        this.freezeRadius = freezeRadius;
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

    public float getStarMeltingZoneRadius()
    {
        return meltRadius;
    }
    public float getStarHabitableZoneRadius()
    {
        return habRadius;
    }
    public float getStarFreezingZoneRadius()
    {
        return freezeRadius;
    }

    public float getMinHabitability() {
        return minHabitability;
    }

    public String getTag()
    {
        return tag;
    }

    public void setMinHabitability(float minHabitability) {
        this.minHabitability = minHabitability;
    }

    public float getMaxHabitability() {
        return maxHabitability;
    }

    public void setMaxHabitability(float maxHabitability) {
        this.maxHabitability = maxHabitability;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isStar()
    {
        return this.isStar;
    }

    public boolean canColonize()
    {
        return this.canColonize;
    }

    /*public boolean onlySpawnsInHabitableZone()
    {
        return this.onlySpawnsInHabitableZone;
    }*/

    public OrbitalZone getSpawningZone()
    {
        return this.spawningZone;
    }

}
