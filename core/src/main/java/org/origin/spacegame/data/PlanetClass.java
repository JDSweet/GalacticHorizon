package org.origin.spacegame.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class PlanetClass implements Disposable
{
    private String tag;
    private Texture gfx;

    public PlanetClass(String tag, Texture gfx)
    {
        this.tag = tag;
        this.gfx = gfx;
    }

    @Override
    public void dispose()
    {
        gfx.dispose();
    }
}
