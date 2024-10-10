package org.origin.spacegame.utilities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Constants;
import org.origin.spacegame.entities.StarSystem;
import org.origin.spacegame.game.GameInstance;

import java.lang.Math;
import java.util.Random;

public class StarSystemGenerator
{
    private static int totalGeneratedSystems = 0;
    private static Random random = new Random();

    private int minPlanets;
    private int maxPlanets;

    public StarSystemGenerator(int minPlanets, int maxPlanets)
    {
        this.minPlanets = minPlanets;
        this.maxPlanets = maxPlanets;
    }

    public StarSystem generateSystem()
    {
        Array<String> starClassTags = GameInstance.getInstance().getStarClassTags();
        int starClassTagIndex = random.nextInt(0, starClassTags.size-1);

        float galacticX = random.nextFloat(Constants.GALAXY_WIDTH);
        float galacticY = random.nextFloat(Constants.GALAXY_HEIGHT);

        //This chunk of code makes sure these coordinates are not too close to any other already existing star systems on the galactic map.
        /*for(int i = 0; i < GameInstance.getInstance().getState().getStarSystems().size; i++)
        {
            StarSystem alreadyCreatedSystem = GameInstance.getInstance().getState().getStarSystems().get(i);
            while(Math.abs(galacticX - alreadyCreatedSystem.getGalacticPosition().x) < Constants.MIN_DISTANCE_BETWEEN_STARS)
                galacticX = random.nextFloat(Constants.GALAXY_WIDTH);
            while(Math.abs(galacticY - alreadyCreatedSystem.getGalacticPosition().y) < Constants.MIN_DISTANCE_BETWEEN_STARS)
                galacticY = random.nextFloat(Constants.GALAXY_HEIGHT);
        }*/

        StarSystem newSystem = new StarSystem(totalGeneratedSystems++,
                                                starClassTags.get(starClassTagIndex),
                                                galacticX,
                                                galacticY);
        return newSystem;
    }
}
