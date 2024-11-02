package org.origin.spacegame.generation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Constants;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.utilities.RandomNumberUtility;

import java.util.Random;

public class RandomStarSystemGenerator
{
    private static int totalGeneratedSystems = 0;
    private static Random random = new Random();

    public static Random getRandom()
    {
        return random;
    }

    private int minPlanets;
    private int maxPlanets;

    public RandomStarSystemGenerator(int minPlanets, int maxPlanets)
    {
        this.minPlanets = minPlanets;
        this.maxPlanets = maxPlanets;
    }

    public StarSystem generateSystem()
    {
        Array<String> starClassTags = GameInstance.getInstance().getStarClassTags();
        int starClassTagIndex = RandomNumberUtility.nextInt(0, starClassTags.size-1);

        float galacticX = RandomNumberUtility.nextFloat(0f, Constants.GALAXY_WIDTH);
        float galacticY = RandomNumberUtility.nextFloat(0f, Constants.GALAXY_HEIGHT);

        Vector2 galacticPos = new Vector2(galacticX, galacticY);
        Array<StarSystem> starSystems = GameInstance.getInstance().getState().getStarSystems();
        /*for(int i = 0; i < starSystems.size; i++)
        {
            //float distance = galacticPos.dst(starSystems.get(i).getGalacticPosition());
            while(galacticPos.dst(starSystems.get(i).getGalacticPosition()) < Constants.MIN_DISTANCE_BETWEEN_STARS)
            {
                galacticPos.x++;
                galacticPos.y++;
            }
        }*/

        //This chunk of code makes sure these coordinates are not too close to any other already existing star systems on the galactic map.
        /*for(int i = 0; i < GameInstance.getInstance().getState().getStarSystems().size; i++)
        {
            StarSystem alreadyCreatedSystem = GameInstance.getInstance().getState().getStarSystems().get(i);
            while(Math.abs(galacticX - alreadyCreatedSystem.getGalacticPosition().x) < Constants.MIN_DISTANCE_BETWEEN_STARS)
                galacticX = random.nextFloat(Constants.GALAXY_WIDTH);
            while(Math.abs(galacticY - alreadyCreatedSystem.getGalacticPosition().y) < Constants.MIN_DISTANCE_BETWEEN_STARS)
                galacticY = random.nextFloat(Constants.GALAXY_HEIGHT);
        }*/

        Gdx.app.log("SystemGenerationDebug", starClassTags.get(starClassTagIndex));
        StarSystem newSystem = new StarSystem(totalGeneratedSystems++,
                                                starClassTags.get(starClassTagIndex),
                                                galacticX,
                                                galacticY);
        return newSystem;
    }
}
