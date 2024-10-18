package org.origin.spacegame.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.data.StarClass;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.game.GameState;

public class StarSystem
{
    public int id;
    public String starType;
    public Array<Planet> planets;
    public Array<Fleet> fleets;

    private Vector2 galacticPosition;

    public StarSystem(int id, String starType, float galacticX, float galacticY)
    {
        this.id = id;
        this.planets = new Array<Planet>();
        this.fleets = new Array<Fleet>();
        this.starType = starType;
        this.galacticPosition = new Vector2(galacticX, galacticY);
    }

    public Vector2 getGalacticPosition()
    {
        return galacticPosition;
    }

    public String getStarTypeTag()
    {
        return starType;
    }

    public StarClass getStarClass()
    {
        return GameInstance.getInstance().getStarClass(this.getStarTypeTag());
        //d
    }
}
