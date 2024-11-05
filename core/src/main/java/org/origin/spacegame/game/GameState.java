package org.origin.spacegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import org.origin.spacegame.Constants;
import org.origin.spacegame.data.ShipClass;
import org.origin.spacegame.entities.polities.IPolity;
import org.origin.spacegame.entities.polities.StellarNation;
import org.origin.spacegame.entities.ships.Ship;
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
    private Array<IPolity> polities;
    private Array<Ship> ships;
    private int playerPolityIndex;

    public GameState()
    {
        this.starSystems = new IntMap<StarSystem>();
        this.planets = new IntMap<Planet>();
        this.ships = new Array<Ship>();
        planetOrbitGenerator = new PlanetOrbitGenerator();
        this.polities = new Array<IPolity>(32);
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
        generatePolities(32, 0);
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

    private void generatePolities(int count, int playerPolityIndex)
    {
        this.playerPolityIndex = playerPolityIndex;
        if(playerPolityIndex < 0 || playerPolityIndex >= count)
        {
            Gdx.app.log("GameInstance.generatePolities Debug", "The player polity index " + playerPolityIndex + " is invalid. Player being set to 0.");
            this.playerPolityIndex = 0;
        }
        for(int i = 0; i < count; i++)
        {
            this.polities.add(new StellarNation(i));
        }
    }

    private static int shipMaxID = 0;

    public Ship spawnShip(String shipClassTag, Vector2 pos, Vector2 vel, Vector2 facing, int polityID)
    {
        StarSystem selectedStarSystem = GameInstance.getInstance().getSelectedStarSystem();
        return spawnShip(shipClassTag, selectedStarSystem, pos, vel, facing, polityID);
    }

    public Ship spawnShip(String shipClassTag, StarSystem system, Vector2 pos, Vector2 vel, Vector2 facing, int polityID)
    {
        ShipClass shipClass = GameInstance.getInstance().getShipClass(shipClassTag);
        IPolity polity = getPolity(polityID);

        //GameInstance.getInstance().log("Ship Spawn Debug", "System " + selectedStarSystem.id);
        Ship ship = new Ship(shipMaxID++, polityID, system, pos, vel, facing, shipClass);
        this.ships.add(ship);
        polity.addShip(ship);
        system.addShip(ship);
        Gdx.app.log("GameState.spawnShip() Debug", "Ship spawned at " + pos.toString());
        return ship;
    }

    public Ship getShip(int idx)
    {
        return ships.get(idx);
    }

    public Array<Ship> getShips()
    {
        return ships;
    }

    public int getPlayerID()
    {
        return this.playerPolityIndex;
    }

    public IPolity getPlayer()
    {
        return this.polities.get(playerPolityIndex);
    }

    public IPolity getPolity(int idx)
    {
        return polities.get(idx);
    }

    public void setPlayer(IPolity polity)
    {
        setPlayer(((StellarNation)polity).getID());
    }

    public void setPlayer(int idx)
    {
        this.playerPolityIndex = idx;
    }

    public StarSystem getRandomStarSystem()
    {
        Array<StarSystem> systems = this.starSystems.values().toArray();
        return systems.random();
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
        system.renderSystemToSystemView(batch, Gdx.graphics.getDeltaTime());
    }

    public void renderGalacticMap(SpriteBatch batch)
    {
        batch.begin();
        for(StarSystem system : this.starSystems.values())
        {
            batch.draw(GameInstance.getInstance().getStarClass(system.getStarTypeTag()).getGfx(),
                                                                system.getGalacticPosition().x,
                                                                system.getGalacticPosition().y, Constants.STAR_SYSTEM_GALACTIC_MAP_RENDER_WIDTH,
                                                                                                Constants.STAR_SYSTEM_GALACTIC_MAP_RENDER_HEIGHT);
        }
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
