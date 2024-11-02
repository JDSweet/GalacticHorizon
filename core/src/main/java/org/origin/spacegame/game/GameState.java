package org.origin.spacegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import org.origin.spacegame.Constants;
import org.origin.spacegame.entities.stellarobj.Planet;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.generation.PlanetOrbitGenerator;
import org.origin.spacegame.generation.SystemGeneratorType;
import org.origin.spacegame.generation.TileMapStarSystemGenerator;

public class GameState
{
    private IntMap<StarSystem> starSystems;
    private IntMap<Planet> planets;
    private PlanetOrbitGenerator planetOrbitGenerator;

    public GameState()
    {
        this.starSystems = new IntMap<StarSystem>();
        this.planets = new IntMap<Planet>();
        planetOrbitGenerator = new PlanetOrbitGenerator();
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
        if (genType == SystemGeneratorType.TILE_MAP)
        {
            TileMapStarSystemGenerator generator = new TileMapStarSystemGenerator();
            Array<StarSystem> generatedSystems = generator.generateStarSystems(systemCount);

            for(StarSystem system : generatedSystems)
            {
                system.addAllPlanets(planetOrbitGenerator.generatePlanets(system));
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

    public StarSystem getStarSystem(int id)
    {
        if(this.starSystems.containsKey(id))
        {
            return starSystems.get(id);
        }
        else
        {
            Gdx.app.log("GameState", "Star System " + id + " does not exist. Returning null.");
        }
        return null;
    }

    public void renderSystemView(SpriteBatch batch, StarSystem system)
    {
        batch.begin();
        system.renderSystemToSystemView(batch);
        batch.end();
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

    public void debugSystemIDs()
    {
        for(StarSystem system : getStarSystems())
        {
            system.debugID();
        }
    }
}
