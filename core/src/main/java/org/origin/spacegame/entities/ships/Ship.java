package org.origin.spacegame.entities.ships;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Identifiable;
import org.origin.spacegame.data.ShipClass;

public class Ship implements Identifiable
{
    private int id;
    private Vector2 position;
    private Vector2 velocity;
    private ShipClass shipClass;
    private Sprite sprite;

    private Vector2 facing;

    private float delta = 0f;
    private float accel = 0f;

    public Vector2 collisionCenter;
    public Array<Vector2> collisionPoints;

    public Ship(int id, Vector2 pos, Vector2 vel, Vector2 facing, ShipClass shipClass)
    {
        this.id = id;
        this.position = pos;
        this.velocity = vel;
        this.facing = new Vector2();
        this.facing.set(facing);
        this.shipClass = shipClass;
        this.sprite = new Sprite(shipClass.getGfx());
        this.accel = shipClass.getMaxAcceleration();
    }

    public void updateShip(float delta)
    {
        sprite.setPosition(position.x, position.y);
    }

    public void thrust(float amnt)
    {
        updateDelta();
        velocity.add(facing.x * accel * delta, facing.y * accel * amnt * delta);
    }

    // Turns the ship towards the given position in world space.
    // Source: https://gamefromscratch.com/gamedev-math-recipes-rotating-to-face-a-point/
    public void turnTowards(Vector2 direction)
    {
        this.facing.set(direction);
        float angle = MathUtils.atan2(facing.y, facing.x);
        angle = angle * (180f/MathUtils.PI);
        turn(angle);
    }

    // This rotates the ship sprite towards the specified angle by a given turn speed.
    // For more info on LibGdx's Sprite.rotate() read this:
    //      https://gamedev.stackexchange.com/questions/88317/sprite-rotation-libgdx
    public void turn(float angle)
    {
        updateDelta();
        sprite.rotate((sprite.getRotation() - angle) * delta);
    }

    public void renderShip(SpriteBatch batch, float delta)
    {
        this.sprite.setPosition(position.x, position.y);
        this.sprite.draw(batch);
    }

    private void updateDelta()
    {
        delta = Math.min(0.06f, Gdx.graphics.getDeltaTime());
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
