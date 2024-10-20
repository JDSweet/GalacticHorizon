package org.origin.spacegame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import org.origin.spacegame.Constants;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.game.GameInstance;

public class Planet
{
    public int id;
    private Vector2 position;
    private float orbitalRadius;
    private PlanetClass planetClass;

    public Planet(int id, Vector2 position, float orbitalRadius, String planetClassTag)
    {
        this.id = id;
        this.position = position;
        this.orbitalRadius = orbitalRadius;
        this.planetClass = GameInstance.getInstance().getPlanetClass(planetClassTag);
    }

    public Planet(int id, Vector2 position, float orbitalRadius, PlanetClass planetClass)
    {
        this.id = id;
        this.position = position;
        this.orbitalRadius = orbitalRadius;
        this.planetClass = planetClass;
    }

    public void renderPlanet(SpriteBatch batch)
    {
        batch.draw(planetClass.getGfx(), position.x, position.y,
            Constants.StarSystemConstants.PLANET_RENDER_SIZE,
            Constants.StarSystemConstants.PLANET_RENDER_SIZE);
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public float getOrbitalRadius()
    {
        return orbitalRadius;
    }

    public PlanetClass getPlanetClass()
    {
        return planetClass;
    }
}
