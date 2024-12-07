package org.origin.spacegame.utilities;

import com.badlogic.gdx.Gdx;
import org.origin.spacegame.generation.OrbitalZone;

public class StringToType
{
    public static OrbitalZone readOrbitalZone(String val)
    {
        String comparator = val.toUpperCase();
        switch(comparator)
        {
            case "MELTING":
                return OrbitalZone.MELTING;
            case "HABITABLE":
                break;
            case "FREEZING":
                break;
            default:
                Gdx.app.log("StringToType", "The specified zone " + val + " is not a legitimate orbital zone.");
        }
        return OrbitalZone.ANY;
    }

    public static boolean yesOrNoToBoolean(String val)
    {
        return false;
    }

    public static boolean yesOrNoToTrueOrFalse(String str)
    {
        return str.equalsIgnoreCase("yes");
    }
}
