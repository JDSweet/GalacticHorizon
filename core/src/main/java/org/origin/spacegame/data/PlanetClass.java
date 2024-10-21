package org.origin.spacegame.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class PlanetClass implements Disposable
{
    private String tag;
    private Texture gfx;
    private float minHabitability;
    private float maxHabitability;
    private int minSize;
    private int maxSize;
    boolean onlySpawnsInHabitableZone;
    boolean isStar;
    boolean canColonize;

    public PlanetClass(String tag, Texture gfx, float minHabitability, float maxHabitability, int minSize,
                       int maxSize, boolean onlySpawnsInHabitableZone, boolean isStar, boolean canColonize)
    {
        this.tag = tag;
        this.gfx = gfx;
        this.minHabitability = minHabitability;
        this.maxHabitability = maxHabitability;
        this.minSize = minSize;
        this.maxSize = maxSize;
        this. onlySpawnsInHabitableZone = onlySpawnsInHabitableZone;
        this.isStar = isStar;
        this.canColonize = canColonize;
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

    public boolean onlySpawnsInHabitableZone()
    {
        return this.onlySpawnsInHabitableZone;
    }

}
