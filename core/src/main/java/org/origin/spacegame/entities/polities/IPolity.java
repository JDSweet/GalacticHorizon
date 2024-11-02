package org.origin.spacegame.entities.polities;

import org.origin.spacegame.entities.ships.Ship;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.entities.ships.Station;

public interface IPolity
{
    void addStation(Station station);

    void addShip(Ship ship);

    void addSystem(StarSystem system);
}
