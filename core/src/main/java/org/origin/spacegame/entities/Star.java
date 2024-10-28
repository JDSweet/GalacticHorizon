package org.origin.spacegame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.origin.spacegame.Constants;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.generation.OrbitalZone;

public class Star extends Planet
{
    public Star(int id, Vector2 position, float orbitalRadius, PlanetClass pc, StarSystem system)
    {
        super(id, position, orbitalRadius, pc, system);
    }

    //If the distance of the given planet from me is the radius.
    public OrbitalZone getOrbitalZoneOf(Planet planet)
    {
        OrbitalZone retval = OrbitalZone.ANY;
        float radius = distanceToPlanet(planet);
        float meltZoneRadius = ((Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH +
                                Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT)/2f) *
                                (getPlanetClass().getStarMeltingZoneRadius()/100);
        float habZoneRadius = ((Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH +
            Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT)/2f) *
            (getPlanetClass().getStarHabitableZoneRadius()/100);
        float freezeZoneRadius = ((Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH +
            Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT)/2f) *
            (getPlanetClass().getStarFreezingZoneRadius()/100);

        Gdx.app.log("Star.getOrbitalZoneOf", "Radius is " + radius);
        Gdx.app.log("Star.getOrbitalZoneOf", "Melt Zone Radius is " + meltZoneRadius);
        Gdx.app.log("Star.getOrbitalZoneOf", "Hab Zone Radius is " + habZoneRadius);
        Gdx.app.log("Star.getOrbitalZoneOf", "Freeze Zone Radius is " + freezeZoneRadius);

        //If we are greater than or equal to the freeze zone radius, then we are in the freeze zone.
        //If we are greater than or equal to the hab zone radius and less than the freeze zone radius,
        //  then we are in the habitable zone.
        //If we are less than the habitable zone radius, then we are in the melting zone.
        if(radius >= freezeZoneRadius)
        {
            retval = OrbitalZone.FREEZING;
        }
        else if(radius >= habZoneRadius && radius < freezeZoneRadius)
        {
            retval = OrbitalZone.HABITABLE;
        }
        else if(radius <= habZoneRadius)
            retval = OrbitalZone.MELTING;
        else
            Gdx.app.log("[Star.getOrbitalZoneOf Debug]", "This state should never be reached.");
        Gdx.app.log("Star.getOrbitalZoneOf Debug", "The return value is " + retval.name());
        return retval;
    }

    /*public float getMeltingZoneRadius()
    {
        return 0;
    }

    public float getHabitableZoneRadius()
    {
        return 0;
    }

    public float getFreezingZoneRadius()
    {
        return 0;
    }*/
}
