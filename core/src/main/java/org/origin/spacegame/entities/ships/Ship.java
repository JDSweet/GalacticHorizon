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
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.entities.polities.IPolity;
import org.origin.spacegame.game.GameInstance;

public class Ship implements Identifiable
{
    private int id;
    private int ownerIdx;
    private Vector2 position;
    private Vector2 velocity;
    private ShipClass shipClass;
    private Sprite sprite;

    private Vector2 facing;
    private StarSystem systemLocation;

    private float delta = 0f;
    private float accel = 0f;

    public Vector2 collisionCenter;
    public Array<Vector2> collisionPoints;

    public Ship(int id, int ownerIdx, StarSystem systemLocation, Vector2 pos, Vector2 vel, Vector2 facing, ShipClass shipClass)
    {
        this.id = id;
        this.ownerIdx = ownerIdx;
        this.position = new Vector2(pos);
        this.velocity = new Vector2(vel);
        this.facing = new Vector2(facing);
        this.facing.set(facing);
        this.shipClass = shipClass;
        this.sprite = new Sprite(shipClass.getGfx());
        this.accel = shipClass.getMaxAcceleration();
    }

    public IPolity getOwner()
    {
        return GameInstance.getInstance().getState().getPolity(ownerIdx);
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
        /*this.facing.set(direction);
        float angle = MathUtils.atan2(facing.y, facing.x);
        angle = angle * (180f/MathUtils.PI);
        turn(angle);*/
        turnTowards(direction.x, direction.y);
    }

    public void turnTowards(float x, float y)
    {
        this.facing.set(x, y);
        float angle = MathUtils.atan2(facing.y, facing.x);
        angle = angle * (180f/MathUtils.PI);
        turn(angle);
    }

    //Turns the ship away from the given position in world space.
    public void turnAway(Vector2 direction)
    {
        /*this.facing.set(-direction.x, -direction.y);
        float angle = MathUtils.atan2(facing.y, facing.x);
        angle = angle * (180f/MathUtils.PI);
        turn(angle);*/
        turnAway(direction.x, direction.y);
    }

    //Turns the ship away from the given position.
    public void turnAway(float x, float y)
    {
        this.facing.set(-x, -y);
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
        //Gdx.app.log("Test", "Testing...");
        this.sprite.setPosition(position.x, position.y);
        //this.sprite.setScale(0.5f);
        this.sprite.setSize(1f, 2.5f);
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
