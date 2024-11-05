package org.origin.spacegame.entities.ships;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
    private float accel = 0.1f;

    public Vector2 collisionCenter;
    public Array<Vector2> collisionPoints;

    private static float spriteOffset = 90f;

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
        this.sprite.setRotation(0);
        this.sprite.setSize(1f, 2.5f);
        this.sprite.setOrigin(sprite.getWidth()/2f, sprite.getHeight()/2f);
    }

    public IPolity getOwner()
    {
        return GameInstance.getInstance().getState().getPolity(ownerIdx);
    }

    public void updateShip(float delta)
    {
        sprite.setPosition(position.x, position.y);
    }

    //Moves the ship towards the specific point using its speed rating.
    public void thrust(float amnt)
    {
        updateDelta();
        velocity.add(facing.x * accel * delta, facing.y * accel * amnt * delta);
        position.set(position.x + velocity.x, position.y + velocity.y);
        sprite.setPosition(position.x, position.y);
    }

    // Turns the ship towards the given position in world space.
    // Source: https://gamefromscratch.com/gamedev-math-recipes-rotating-to-face-a-point/
    public void turnTowards(Vector2 direction)
    {
        turnTowards(direction.x, direction.y);
    }

    public void turnTowards(float x, float y)
    {
        this.facing.set(x - position.x, y - position.y);
        float angle = MathUtils.atan2(facing.y, facing.x) * MathUtils.radiansToDegrees;
        if(angle == sprite.getRotation())
            Gdx.app.log("Test", "Hey");
        else
            turn(angle-spriteOffset);
    }

    //Turns the ship away from the given position in world space.
    public void turnAway(Vector2 direction)
    {
        turnAway(direction.x, direction.y);
    }

    //Turns the ship away from the given position.
    public void turnAway(float x, float y)
    {
        this.facing.set(-(x - position.x), -(y - position.y));
        float angle = MathUtils.atan2(facing.y, facing.x) * MathUtils.radiansToDegrees;
        if(angle == sprite.getRotation())
            Gdx.app.log("Test", "Hey");
        else
            turn(angle-spriteOffset);
    }

    // This rotates the ship sprite towards the specified angle by a given turn speed.
    // For more info on LibGdx's Sprite.rotate() read this:
    //      https://gamedev.stackexchange.com/questions/88317/sprite-rotation-libgdx
    public void turn(float angle)
    {
        updateDelta();
        //sprite.rotate(angle);
        Gdx.app.log("ShipTurning Debug", "Ship angle is " + angle);
        sprite.setOrigin(sprite.getWidth()/2f, sprite.getHeight()/2f);
        sprite.setRotation(angle);
    }

    public void renderShip(SpriteBatch batch, float delta)
    {
        this.sprite.setPosition(position.x, position.y);
        this.sprite.draw(batch);
    }

    public void renderBackgroundCircle(ShapeRenderer renderer)
    {
        renderer.circle(position.x + sprite.getWidth()/2f, position.y + sprite.getHeight()/2f, sprite.getHeight());
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
