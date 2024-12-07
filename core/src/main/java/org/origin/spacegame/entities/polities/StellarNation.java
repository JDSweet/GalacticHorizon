package org.origin.spacegame.entities.polities;

import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.IPlayable;
import org.origin.spacegame.Identifiable;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.entities.ships.Ship;
import org.origin.spacegame.entities.ships.Station;
import org.origin.spacegame.entities.stellarobj.Planet;
import org.origin.spacegame.game.GameInstance;

public class StellarNation implements IPolity, Identifiable, IPlayable
{
    private int idx;
    private Array<Ship> ships;
    private Array<Planet> planets;
    private Array<StarSystem> systems;
    private Array<Station> stations;
    private boolean isHuman;

    public StellarNation(int idx)
    {
        this(idx, false);
    }

    public StellarNation(int idx, boolean isHuman)
    {
        this.idx = idx;
        this.isHuman = isHuman;
        this.ships = new Array<>();
        this.planets = new Array<>();
        this.systems = new Array<>();
        this.stations = new Array<>();
    }

    @Override
    public void addStation(Station station)
    {
        stations.add(station);
    }

    @Override
    public void addShip(Ship ship)
    {
        ships.add(ship);
    }

    @Override
    public void addSystem(StarSystem system)
    {
        systems.add(system);
    }

    @Override
    public boolean ownsShip(Ship ship)
    {
        return ships.contains(ship, true);
    }

    @Override
    public boolean ownsStation(Station station)
    {
        return stations.contains(station, true);
    }

    @Override
    public boolean ownsSystem(StarSystem system)
    {
        return systems.contains(system, true);
    }

    @Override
    public boolean ownsPlanet(Planet planet)
    {
        return planets.contains(planet, true);
    }

    @Override
    public void markAsFriendlyTowards(IPolity other)
    {

    }

    @Override
    public void markAsNeutralTowards(IPolity other)
    {

    }

    @Override
    public void markMeAsSubjugateOf(IPolity other)
    {

    }

    @Override
    public void markAsHostileTowards(IPolity other)
    {

    }

    @Override
    public boolean isFriendlyTowards(IPolity other)
    {
        return false;
    }

    @Override
    public boolean isNeutralTowards(IPolity other)
    {
        return false;
    }

    @Override
    public boolean isHostileTowards(IPolity other)
    {
        return false;
    }

    @Override
    public boolean isSubjectOf(IPolity other)
    {
        return false;
    }

    @Override
    public int getID()
    {
        return this.idx;
    }

    @Override
    public boolean isPlayer()
    {
        return isHuman;
    }

    @Override
    public void setAsPlayer()
    {
        this.isHuman = true;
        GameInstance.getInstance().getState().setPlayer(this.idx);
    }
}
