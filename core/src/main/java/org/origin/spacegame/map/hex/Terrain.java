package org.origin.spacegame.map.hex;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.XmlReader;
import org.origin.spacegame.ITaggable;
import org.origin.spacegame.Identifiable;

public class Terrain implements ITaggable
{
    private String id;
    private Texture gfx;

    public Terrain(XmlReader.Element element)
    {
        this.id = element.getAttribute("id");
        this.gfx = new Texture(element.getAttribute("textureFile"));
    }

    @Override
    public String getTag()
    {
        return this.id;
    }
}
