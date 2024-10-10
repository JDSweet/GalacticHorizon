package org.origin.spacegame.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class StarClass implements Disposable
{
    private String tag;
    private Texture gfx;
    public float energyOutputMod;

    public StarClass(String tag, Texture gfx, float energyOutputMod)
    {
        this.tag = tag;
        this.gfx = gfx;
        this.energyOutputMod = energyOutputMod;
    }

    @Override
    public void dispose()
    {
        gfx.dispose();
    }
}
