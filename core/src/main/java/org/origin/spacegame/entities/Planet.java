package org.origin.spacegame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import org.origin.spacegame.Constants;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.utilities.RandomNumberUtility;

import java.util.Random;

public class Planet
{
    public int id;
    private Vector2 position;
    private float orbitalRadius;
    private PlanetClass planetClass;
    private Rectangle rect;

    private int size = 0;
    private float habitability = 0f;

    public Planet(int id, Vector2 position, float orbitalRadius, String planetClassTag)
    {
        this(id, position, orbitalRadius, GameInstance.getInstance().getPlanetClass(planetClassTag));
    }

    public Planet(int id, Vector2 position, float orbitalRadius, PlanetClass planetClass)
    {
        this.id = id;
        this.position = position;
        this.orbitalRadius = orbitalRadius;
        this.planetClass = planetClass;
        if(planetClass.getMaxHabitability() <= planetClass.getMinHabitability())
            this.habitability = planetClass.getMinHabitability();
        else
            this.habitability = RandomNumberUtility.nextFloat(planetClass.getMinHabitability(),
                planetClass.getMaxHabitability());
        if(planetClass.getMinSize() <= planetClass.getMaxSize())
            this.size = planetClass.getMinSize();
        else
            this.size = RandomNumberUtility.nextInt(planetClass.getMinSize(),
                planetClass.getMaxSize());
        this.rect = new Rectangle(position.x, position.y, size, size);
    }

    public void renderPlanet(SpriteBatch batch)
    {
        /*batch.draw(planetClass.getGfx(), position.x, position.y,
            Constants.StarSystemConstants.PLANET_RENDER_SIZE,
            Constants.StarSystemConstants.PLANET_RENDER_SIZE);*/
        batch.draw(planetClass.getGfx(), position.x, position.y,
            size,
            size);
    }

    public boolean isTouched(float x, float y)
    {
        if(rect.contains(x, y))
            return true;
        else
            return false;
    }

    public int getSize()
    {
        return size;
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
