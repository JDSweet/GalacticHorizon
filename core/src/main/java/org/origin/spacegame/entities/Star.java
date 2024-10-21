package org.origin.spacegame.entities;

import com.badlogic.gdx.math.Vector2;
import org.origin.spacegame.data.PlanetClass;

public class Star extends Planet
{
    public Star(int id, Vector2 position, float orbitalRadius, PlanetClass pc)
    {
        super(id, position, orbitalRadius, pc);
    }
}
