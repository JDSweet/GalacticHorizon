package org.origin.spacegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import org.origin.spacegame.Constants;
import org.origin.spacegame.entities.Planet;
import org.origin.spacegame.entities.StarSystem;
import org.origin.spacegame.utilities.StarSystemGenerator;

import java.util.HashMap;

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
        generateStarSystems(Constants.STAR_SYSTEM_COUNT);
    }

    private void generateStarSystems(int systemCount)
    {
        StarSystemGenerator generator = new StarSystemGenerator(Constants.STAR_SYSTEM_MIN_PLANET_COUNT,
                                                                Constants.STAR_SYSTEM_MAX_PLANET_COUNT);
        for(int i = 0; i < systemCount; i++)
        {
            StarSystem generatedSystem = generator.generateSystem();
            starSystems.put(generatedSystem.id, generatedSystem);

            Gdx.app.log("Star System Generated", "Star System " + generatedSystem.id + " has been generated at (" + generatedSystem.getGalacticPosition().x + ", " + generatedSystem.getGalacticPosition().y + ") with a star type of " + generatedSystem.getStarType());
        }
    }

    public Array<StarSystem> getStarSystems()
    {
        return starSystems.values().toArray();
    }
}
