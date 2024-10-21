package org.origin.spacegame.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Constants;
import org.origin.spacegame.entities.StarSystem;
import org.origin.spacegame.game.GameInstance;

import java.util.Random;

/*
* This class creates an imaginary tile map of [map width] * [map height]. A source
* image is loaded from memory (the image resolution must match the map size), where black
* tiles represent default empty tiles, and white tiles represent sector centers.
* Sectors represent [generation sector width] * [generation sector height]
* tile slices of the map where stars are more likely to generate (30% by default, compared
* to a default base 5% chance in a non-sector tile). If an empty tile borders a tile with a
* star system in it, the chance of it having a star system increases by 10% (to a minimum of 15%).
*
* Because star system generation happens sector-by-sector, with no regard for sector overlap,
* a surefire way to ensure greater star density in a region is to pack multiple sectors close
* to each-other.
* */
public class TileMapStarSystemGenerator
{
    private Random random;
    private float percentChanceOfStarSpawningInTile;

    public TileMapStarSystemGenerator()
    {
        this.random = new Random();
        this.percentChanceOfStarSpawningInTile = 0.1f;
    }

    public Array<StarSystem> generateStarSystems (int numberOfSystemsToGenerate)
    {
        return generateStarSystems(0, numberOfSystemsToGenerate);
    }

    public Array<StarSystem> generateStarSystems(int currentSystemBeingGenerated, int numberOfSystemsToGenerate)
    {
        Array<StarSystem> retval = new Array<StarSystem>(numberOfSystemsToGenerate);
        //int currentSystemBeingGenerated = 0;

        //For each tile in the map.
        //  If current system being generated < numberOfSystemsToGenerate
        //      If tile is not occupied
        //          If the random component says we should generate a star system...
        //              Generate the star system.
        //If the number of star systems generated < the number we need to generate, call this function recursively.
        //For each tile in the map.
        for(int x = 0; x < Constants.GALAXY_WIDTH; x++)
        {
            for(int y = 0; y < Constants.GALAXY_HEIGHT; y++)
            {
                if(currentSystemBeingGenerated < numberOfSystemsToGenerate)
                {
                    //If this tile is not occupied...
                    if(!isSystemInThisTile(retval, 0.1f, x, y))
                    {
                        //And if the random component says we should generate a star system in this tile...
                        if(random.nextFloat(0f, 1f) < percentChanceOfStarSpawningInTile)
                        {
                            //Then generate the star system.
                            retval.add(generateSystem(currentSystemBeingGenerated++, x, y));
                        }
                    }
                }
            }
        }

        if(currentSystemBeingGenerated < numberOfSystemsToGenerate)
            retval.addAll(generateStarSystems(numberOfSystemsToGenerate - currentSystemBeingGenerated, numberOfSystemsToGenerate));

        //While we haven't yet generated the desired number of systems.
        /*while(currentSystemBeingGenerated < numberOfSystems)
        {
            //For each tile in the map.
            for(int x = 0; x < Constants.GALAXY_WIDTH; x++)
            {
                for(int y = 0; y < Constants.GALAXY_HEIGHT; y++)
                {
                    //If this tile is not occupied...
                    if(!isSystemInThisTile(retval, 0.1f, x, y))
                    {
                        //And if the random component says we should generate a star system in this tile...
                        if(random.nextFloat(0f, 1f) < percentChanceOfStarSpawningInTile)
                        {
                            //Then generate the star system.
                            retval.add(generateSystem(currentSystemBeingGenerated++, x, y));
                        }
                    }
                }
            }
        }*/
        Gdx.app.log("TileMapStarSystemGenerator Debug", "" + retval.size + " systems generated.");
        return retval;
    }

    private boolean isSystemInThisTile(Array<StarSystem> systems, float epsilon, int x, int y)
    {
        Vector2 position = new Vector2((float)x, (float)y);
        for(StarSystem system : systems)
        {
            if(system.getGalacticPosition().epsilonEquals(position, epsilon))
                return true;
        }
        return false;
    }

    private StarSystem generateSystem(int id, int x, int y)
    {
        Array<String> starClassTags = GameInstance.getInstance().getStarClassTags();
        Gdx.app.log("System Generation Debug", "Star Classes..." + starClassTags.size);
        int starClassTagIndex = random.nextInt(0, starClassTags.size);

        float galacticX = random.nextFloat(Constants.GALAXY_WIDTH);
        float galacticY = random.nextFloat(Constants.GALAXY_HEIGHT);

        //Vector2 galacticPos = new Vector2(galacticX, galacticY);
        Array<StarSystem> starSystems = GameInstance.getInstance().getState().getStarSystems();

        Gdx.app.log("SystemGenerationDebug", starClassTags.get(starClassTagIndex));
        StarSystem newSystem = new StarSystem(id,
            starClassTags.get(starClassTagIndex),
            x, y);
        return newSystem;
    }
}
