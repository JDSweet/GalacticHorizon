package org.origin.spacegame.entities.ships;

import com.badlogic.gdx.math.Vector2;
import org.origin.spacegame.Identifiable;
import org.origin.spacegame.data.ShipClass;

public class Ship implements Identifiable
{
    private int id;
    private Vector2 position;
    private Vector2 velocity;
    private ShipClass shipClass;

    public Ship(int id, Vector2 pos, Vector2 vel, ShipClass shipClass)
    {
        this.id = id;
        this.position = pos;
        this.velocity = vel;
        this.shipClass = shipClass;
    }

    public void renderShip(float delta)
    {

    }

    public Vector2 getPosition()
    {
        return position;
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public ShipClass getShipClass()
    {
        return this.shipClass;
    }

    @Override
    public int getID()
    {
        return this.id;
    }
}
