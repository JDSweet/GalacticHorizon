package org.origin.spacegame.generation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Constants;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.entities.Planet;
import org.origin.spacegame.entities.Star;
import org.origin.spacegame.entities.StarSystem;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.utilities.RandomNumberUtility;

import java.util.Random;

//This class generates planets for star systems.
public class PlanetOrbitGenerator
{
    private final Random random;
    private int planetCount;

    public PlanetOrbitGenerator()
    {
        this.random = new Random();
        this.planetCount = 0;
    }

    /*
    * How we generate the planet's system position (assuming a circular orbit for the sake of simplicity).
    * Source: Google AI Overview
    * To pick a random point on a circle, generate a random angle (between 0 and 2π radians) and a random radius
    *   (between 0 and the circle's radius), then use trigonometry to calculate the corresponding x and y coordinates based on the center of the circle; essentially, you are selecting a random direction on the circle and a random distance from the center to find the point.
    *   Key steps:
    *       1. Choose a random angle: Generate a random number between 0 and 2π (representing a full circle in radians).
    *       2. Choose a random radius: Generate a random number between 0 and the radius of the circle.
    *       3. Calculate coordinates:
    *           x-coordinate: x = centerX + (radius * cos(angle))
    *           y-coordinate: y = centerY + (radius * sin(angle))
    *   Explanation:
    *       Why angle and radius:
    *           By randomly selecting an angle and radius, you effectively choose a random point on the circle, ensuring uniform distribution across the circumference.
    *           Trigonometry for coordinates:
    *           The cosine of the angle gives the x-coordinate component relative to the radius, and the sine of the angle gives the y-coordinate component, allowing you to calculate the exact position on the circle.
    *       Important considerations:
    *           Uniform distribution:
    *               This method ensures that all points on the circle have an equal chance of being selected.
    *           Center of the circle:
    *               Remember to adjust the calculations based on the center coordinates of your circle (centerX, centerY).
    */

    //This function generates a list of planets for the given star system.
    //The list is randomly generated, and can be between Constants.
    public Array<Planet> generatePlanets(StarSystem system)
    {
        Array<Planet> retval = new Array<Planet>();
        Array<Planet> starsInSystem = new Array<Planet>();
        Array<Planet> nonStarPlanets = new Array<Planet>();

        int minPlanetCount = Constants.MIN_PLANET_COUNT;
        int maxPlanetCount = Constants.MAX_PLANET_COUNT;
        int realPlanetCount = RandomNumberUtility.nextInt(minPlanetCount, maxPlanetCount+1); //+1 because we are now treating stars like planets for the purpose of system generation.

        //We generate the minimum orbital radius, then we "step" out a random distance from the minimum for each planet
        //generated. The random distance is between Constants.StarSystemConstants.MIN_DISTANCE_BETWEEN_PLANET_ORBITS
        //and Constants.StarSystemConstants.MIN_DISTANCE_BETWEEN_PLANET_ORBITS.
        float minOrbitalRadius = Constants.StarSystemConstants.MINIMUM_ORBITAL_RADIUS; //(Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH + Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT)/2f * 1.1f;

        //The current orbital radius we are working with.
        float curOrbitalRadius = Constants.StarSystemConstants.MINIMUM_ORBITAL_RADIUS;
        float lastOrbitalRadius = 0f; //Constants.StarSystemConstants.MINIMUM_ORBITAL_RADIUS;
        //float curRadius = 0f;

        int numStarsInSystem = 0;
        for(int i = 0; i < realPlanetCount; i++)
        {
            //float stepAmount = 0f;
            float distanceBetweenThisAndLastOrbit = RandomNumberUtility.nextFloat(Constants.StarSystemConstants.MIN_DISTANCE_BETWEEN_PLANET_ORBITS,
                                                                     Constants.StarSystemConstants.MAX_DISTANCE_BETWEEN_PLANET_ORBITS);
            if(i == 0)
                curOrbitalRadius = 0f;
            else if(i == 1)
                curOrbitalRadius = Constants.StarSystemConstants.MINIMUM_ORBITAL_RADIUS;
            else
                curOrbitalRadius = lastOrbitalRadius + distanceBetweenThisAndLastOrbit;
            /*if(i > 0)
                stepAmount = random.nextFloat(minOrbitalRadius, minOrbitalRadius + Constants.StarSystemConstants.MAX_DISTANCE_BETWEEN_PLANET_ORBITS);
            else
                stepAmount = random.nextFloat(minOrbitalRadius, minOrbitalRadius * 1.1f);*/

            Array<String> planetClassTags = GameInstance.getInstance().getPlanetClassTags();
            String planetClassTag = planetClassTags.get(RandomNumberUtility.nextInt(0, planetClassTags.size));
            PlanetClass planetClass = null;
            if(i == 0) //If this is the first "planet" in the system, then is the system's main star.
            {
                planetClass = system.getStarClass().getStarPlanetClass();
                numStarsInSystem++;
            }
            else
            {
                planetClass = GameInstance.getInstance().getPlanetClass(planetClassTag);
                if(planetClass.isStar())
                {
                    numStarsInSystem++;
                }
            }

            if(numStarsInSystem >= Constants.StarSystemConstants.MAX_STARS_IN_SYSTEM)
            {
                //Now, we're going to remove all star planet classes from the list of possible planet classes to
                //avoid going over the limit.
                for(String pcTag : planetClassTags)
                {
                    if(GameInstance.getInstance().getPlanetClass(pcTag).isStar())
                        planetClassTags.removeValue(pcTag, false);
                }
                //Regenerate the list of valid planet-classes to exclude stars, now that we are out of stars.
                planetClassTag = planetClassTags.get(RandomNumberUtility.nextInt(0, planetClassTags.size));
                planetClass = GameInstance.getInstance().getPlanetClass(planetClassTag);
            }


            Planet planet = generatePlanet(system, planetClass, curOrbitalRadius);
            if(planetClass.isStar())
                starsInSystem.add(planet);
            else
                nonStarPlanets.add(planet);
            //if(planetClass.isGasGiant())
            Gdx.app.log("PlanetGeneratorDebug", "Planet " + planet.id
                + " generated at " + planet.getPosition().toString());
            lastOrbitalRadius = curOrbitalRadius;
            retval.add(planet);
        }

        // Now we are going to cycle through the list of generated planets.
        // We are going to get a list of all stars in the star system.
        // Then we are going to get a list of orbital minimum and
        // maximum radii for each "zone" (melting, habitable, freezing).
        // These radii will be defined in the star's planet-class.

        // For each planet that isn't a star
        //      For each star
        //          If planet.orbitalZone is colder than star.getOrbitalZoneOf(planet) then
        //              planet.orbitalZone = star.getOrbitalZoneOf(planet)
        //
        //

        for(Planet planet : nonStarPlanets)
        {
            OrbitalZone zone = OrbitalZone.ANY;
            for(Planet star : starsInSystem)
            {
                OrbitalZone newOrbitalZone = ((Star)star).getOrbitalZoneOf(planet);
                if(zone == OrbitalZone.ANY)
                    zone = OrbitalZone.FREEZING;
                if(zone == OrbitalZone.FREEZING && newOrbitalZone != OrbitalZone.FREEZING)
                    zone = newOrbitalZone;
                if(zone == OrbitalZone.HABITABLE && newOrbitalZone != OrbitalZone.MELTING)
                    zone = newOrbitalZone;
                if(zone == OrbitalZone.ANY)
                    Gdx.app.log("PlanetOrbitGenerator Debug", "This state should never be reached.");
            }

            switch(zone)
            {
                case FREEZING:
                    Gdx.app.log("PlanetOrbitGenerator Debug", "The planet " + planet.getID() + " is in the FREEZING zone with an orbital radius of " + planet.getOrbitalRadius());
                    break;
                case HABITABLE:
                    Gdx.app.log("PlanetOrbitGenerator Debug", "The planet " + planet.getID() + " is in the HABITABLE zone.");
                    break;
                case MELTING:
                    Gdx.app.log("PlanetOrbitGenerator Debug", "The planet " + planet.getID() + " is in the MELTING zone.");
                    break;
                case ANY:
                    Gdx.app.log("PlanetOrbitGenerator Debug", "This state should never be reached.");
                    break;
            }
        }

        return retval;
    }

    //This function takes a star system, a planet class, and an orbital radius, and returns a new planet
    //positioned at a random point on that orbital radius around the center-point of that system.
    private Planet generatePlanet(StarSystem system, PlanetClass planetClass, float orbitalRadius)
    {
        float angle = RandomNumberUtility.nextFloat(0f, 2f*3.1415f);
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        float planetX = system.getCenter().x + (orbitalRadius * cos);
        float planetY = system.getCenter().y + (orbitalRadius * sin);
        Vector2 position = new Vector2(planetX, planetY);
        Planet retval = null;
        if(planetClass.isStar())
        {
            retval = new Star(planetCount++, position, orbitalRadius, planetClass, system);
        }
        else
        {
            retval = new Planet(planetCount++, position, orbitalRadius, planetClass, system);
        }
        return retval;
    }
}
