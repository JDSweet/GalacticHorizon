package org.origin.spacegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import org.origin.spacegame.Constants;
import org.origin.spacegame.entities.Planet;
import org.origin.spacegame.entities.StarSystem;
import org.origin.spacegame.utilities.SystemGeneratorType;
import org.origin.spacegame.utilities.RandomStarSystemGenerator;
import org.origin.spacegame.utilities.TileMapStarSystemGenerator;

public class GameState
{
    private IntMap<StarSystem> starSystems;
    private IntMap<Planet> planets;

    public GameState()
    {
        this.starSystems = new IntMap<StarSystem>();
        this.planets = new IntMap<Planet>();
    }

    public void initialize()
    {
        generateGalaxy();
    }

    private void generateGalaxy()
    {
        Gdx.app.log("Galaxy being generated...", "The star system generator is running.");
        SystemGeneratorType genType = SystemGeneratorType.TILE_MAP;
        generateStarSystems(Constants.STAR_SYSTEM_COUNT, genType);
    }

    private void generateStarSystems(int systemCount, SystemGeneratorType genType)
    {
        if(genType == SystemGeneratorType.RANDOM)
        {
            RandomStarSystemGenerator generator = new RandomStarSystemGenerator(Constants.STAR_SYSTEM_MIN_PLANET_COUNT,
                Constants.STAR_SYSTEM_MAX_PLANET_COUNT);
            //This array will be sent to post-processing.
            Array<StarSystem> starSystemsArray = new Array<StarSystem>();
            for(int i = 0; i < systemCount; i++)
            {
                StarSystem generatedSystem = generator.generateSystem();
                starSystems.put(generatedSystem.id, generatedSystem);

                for(StarSystem s1 : starSystemsArray)
                {
                    while(isTooCloseToAnotherSystem(s1, starSystemsArray))
                    {
                        s1.getGalacticPosition().x = RandomStarSystemGenerator.getRandom().nextFloat(Constants.GALAXY_WIDTH);
                        s1.getGalacticPosition().y = RandomStarSystemGenerator.getRandom().nextFloat(Constants.GALAXY_HEIGHT);
                    }
                }
                Gdx.app.log("Star System Generated", "Star System " + generatedSystem.id + " has been generated at (" + generatedSystem.getGalacticPosition().x + ", " + generatedSystem.getGalacticPosition().y + ") with a star type of " + generatedSystem.getStarTypeTag());
            }

            for(StarSystem s1 : starSystemsArray)
            {
                while(isTooCloseToAnotherSystem(s1, starSystemsArray))
                {
                    s1.getGalacticPosition().x = RandomStarSystemGenerator.getRandom().nextFloat(Constants.GALAXY_WIDTH);
                    s1.getGalacticPosition().y = RandomStarSystemGenerator.getRandom().nextFloat(Constants.GALAXY_HEIGHT);
                }
            }
        }
        else if (genType == SystemGeneratorType.TILE_MAP)
        {
            TileMapStarSystemGenerator generator = new TileMapStarSystemGenerator();
            Array<StarSystem> generatedSystems = generator.generateStarSystems(systemCount);
            for(StarSystem system : generatedSystems)
            {
                this.starSystems.put(system.id, system);
            }
        }
    }

    //This checks whether the given origin point is too close to any of the star systems in the given list.
    private boolean isTooCloseToAnotherSystem(StarSystem origin, Array<StarSystem> starSystemArray)
    {
        for(StarSystem s1 : starSystemArray)
        {
            if(origin == s1)
                continue;
            if(systemsAreTooClose(origin, s1))
                return true;
        }
        return false;
    }

    private boolean systemsAreTooClose(StarSystem one, StarSystem two)
    {
        if(one.getGalacticPosition().dst(two.getGalacticPosition()) < Constants.MIN_DISTANCE_BETWEEN_STARS)
            return true;
        return false;
    }

    public void renderGalacticMap(SpriteBatch batch)
    {
        batch.begin();
        //String starSystemRenderDebugTxt = "";
        for(StarSystem system : this.starSystems.values())
        {
            batch.draw(GameInstance.getInstance().getStarClass(system.getStarTypeTag()).getGfx(),
                                                                system.getGalacticPosition().x,
                                                                system.getGalacticPosition().y, Constants.STAR_SYSTEM_GALACTIC_MAP_RENDER_WIDTH,
                                                                                                Constants.STAR_SYSTEM_GALACTIC_MAP_RENDER_HEIGHT);
            //starSystemRenderDebugTxt += "\n Rendered " + system.starType + " at " + system.getGalacticPosition().toString();
        }
        //Gdx.app.log("Galaxy Render Debug", starSystemRenderDebugTxt);
        batch.end();
    }

    public Array<StarSystem> getStarSystems()
    {
        return starSystems.values().toArray();
    }
}
