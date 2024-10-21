package org.origin.spacegame.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Constants;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.entities.Planet;
import org.origin.spacegame.entities.StarSystem;
import org.origin.spacegame.game.GameInstance;

import java.util.Random;

//This class generates planets for star systems.
public class PlanetGenerator
{
    private final Random random;
    private int planetCount;

    public PlanetGenerator()
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

        int minPlanetCount = Constants.MIN_PLANET_COUNT;
        int maxPlanetCount = Constants.MAX_PLANET_COUNT;
        int realPlanetCount = random.nextInt(minPlanetCount, maxPlanetCount);

        //We generate the minimum orbital radius, then we "step" out a random distance from the minimum for each planet
        //generated. The random distance is between Constants.StarSystemConstants.MIN_DISTANCE_BETWEEN_PLANET_ORBITS
        //and Constants.StarSystemConstants.MIN_DISTANCE_BETWEEN_PLANET_ORBITS.
        float minOrbitalRadius = Constants.StarSystemConstants.MINIMUM_ORBITAL_RADIUS; //(Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH + Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT)/2f * 1.1f;

        //The current orbital radius we are working with.
        float curOrbitalRadius = Constants.StarSystemConstants.MINIMUM_ORBITAL_RADIUS;
        float lastOrbitalRadius = 0f; //Constants.StarSystemConstants.MINIMUM_ORBITAL_RADIUS;
        //float curRadius = 0f;

        for(int i = 0; i < realPlanetCount; i++)
        {
            //float stepAmount = 0f;
            float distanceBetweenThisAndLastOrbit = random.nextFloat(Constants.StarSystemConstants.MIN_DISTANCE_BETWEEN_PLANET_ORBITS,
                                                                     Constants.StarSystemConstants.MAX_DISTANCE_BETWEEN_PLANET_ORBITS);
            if(i == 0)
                curOrbitalRadius = Constants.StarSystemConstants.MINIMUM_ORBITAL_RADIUS;
            else
                curOrbitalRadius = lastOrbitalRadius + distanceBetweenThisAndLastOrbit;
            /*if(i > 0)
                stepAmount = random.nextFloat(minOrbitalRadius, minOrbitalRadius + Constants.StarSystemConstants.MAX_DISTANCE_BETWEEN_PLANET_ORBITS);
            else
                stepAmount = random.nextFloat(minOrbitalRadius, minOrbitalRadius * 1.1f);*/

            Array<String> planetClassTags = GameInstance.getInstance().getPlanetClassTags();
            String planetClassTag = planetClassTags.get(random.nextInt(0, planetClassTags.size-1));
            PlanetClass planetClass = GameInstance.getInstance().getPlanetClass(planetClassTag);
            //Planet planet = generatePlanet(system, planetClass, curOrbitalRadius + stepAmount);

            Planet planet = generatePlanet(system, planetClass, curOrbitalRadius);
            Gdx.app.log("PlanetGeneratorDebug", "Planet " + planet.id
                + " generated at " + planet.getPosition().toString());
            lastOrbitalRadius = curOrbitalRadius;
            retval.add(planet);
        }
        return retval;
    }

    //This function takes a star system, a planet class, and an orbital radius, and returns a new planet
    //positioned at a random point on that orbital radius around the centerpoint of that system.
    private Planet generatePlanet(StarSystem system, PlanetClass planetClass, float orbitalRadius)
    {
        float angle = random.nextFloat(0f, 2f*3.1415f);
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        float planetX = system.getCenter().x + (orbitalRadius * cos);
        float planetY = system.getCenter().y + (orbitalRadius * sin);
        Vector2 position = new Vector2(planetX, planetY);
        return new Planet(planetCount++, position, orbitalRadius, planetClass);
    }
}
