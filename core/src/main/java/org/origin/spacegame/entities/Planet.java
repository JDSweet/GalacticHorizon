package org.origin.spacegame.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.utilities.RandomNumberUtility;

public class Planet
{
    public int id;
    private Vector2 position;
    private float orbitalRadius;
    private PlanetClass planetClass;
    private Rectangle rect;

    private int size = 0;
    private float habitability = 0f;

    private StarSystem starSystem;

    /*
    *
    * To Do: In order to add zones (habitable zone, freezing zone, melting zone), we need to be able
    * to get a list of each planet class that can spawn in each zone, and then randomly pick a planet from said
    * list of planet classes.
    *
    * */
    public Planet(int id, Vector2 position, float orbitalRadius, StarSystem system)
    {
        this(id, position, orbitalRadius, GameInstance.getInstance().getPlanetClass(null), system);
    }

    public Planet(int id, Vector2 position, float orbitalRadius, String planetClassTag, StarSystem system)
    {
        this(id, position, orbitalRadius, GameInstance.getInstance().getPlanetClass(planetClassTag), system);
    }

    public Planet(int id, Vector2 position, float orbitalRadius, PlanetClass planetClass, StarSystem system)
    {
        this.id = id;
        this.position = position;
        this.orbitalRadius = orbitalRadius;
        this.planetClass = planetClass;
        /*
        *
        * if(orbitalRadius <= Constants.MELTINGZONE)
        *   planetClass = GameInstance.getInstance().getPlanetClass("pc_molten");
        *
        * */
        if(planetClass.getMaxHabitability() <= planetClass.getMinHabitability())
            this.habitability = planetClass.getMinHabitability();
        else
            this.habitability = RandomNumberUtility.nextFloat(planetClass.getMinHabitability(),
                planetClass.getMaxHabitability());
        if(planetClass.getMaxSize() <= planetClass.getMinSize())
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

    public float distanceToPlanet(Planet other)
    {
        return position.dst(other.position);
    }

    public StarSystem getSystem()
    {
        return this.starSystem;
    }

    public boolean isTouched(float x, float y)
    {
        if(rect.contains(x, y))
            return true;
        else
            return false;
    }

    public void setPlanetClass(String tag, boolean resize, boolean regenerateHabitability)
    {
        this.planetClass = GameInstance.getInstance().getPlanetClass(tag);
        if(regenerateHabitability)
            this.habitability = RandomNumberUtility.nextFloat(planetClass.getMinHabitability(), planetClass.getMaxHabitability());
        if(resize)
            this.size = RandomNumberUtility.nextInt(planetClass.getMinSize(), planetClass.getMaxSize());
    }

    public int getHabitabilityRounded()
    {
        return Math.round(getHabitability()*100);
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

    public float getHabitability()
    {
        return this.habitability;
    }

    public int getID()
    {
        return this.id;
    }
}
