package org.origin.spacegame.entities.polities;

import com.badlogic.gdx.graphics.Color;
import org.origin.spacegame.entities.ships.Ship;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.entities.ships.Station;
import org.origin.spacegame.entities.stellarobj.Planet;

public interface IPolity
{
    void addStation(Station station);

    void addShip(Ship ship);

    void addSystem(StarSystem system);

    boolean ownsShip(Ship ship);

    boolean ownsStation(Station station);

    boolean ownsSystem(StarSystem system);

    boolean ownsPlanet(Planet planet);

    void markAsFriendlyTowards(IPolity other);

    void markAsNeutralTowards(IPolity other);

    void markMeAsSubjugateOf(IPolity other);

    void markAsHostileTowards(IPolity other);

    boolean isFriendlyTowards(IPolity other);

    boolean isNeutralTowards(IPolity other);

    boolean isHostileTowards (IPolity other);

    boolean isSubjectOf(IPolity other);

    Color getColor();
    void setColor(Color color);
}
