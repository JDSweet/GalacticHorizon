package org.origin.spacegame.entities.galaxy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Constants;
import org.origin.spacegame.data.StarClass;
import org.origin.spacegame.entities.fleets.Fleet;
import org.origin.spacegame.entities.polities.IPolity;
import org.origin.spacegame.entities.polities.StellarNation;
import org.origin.spacegame.entities.ships.Ship;
import org.origin.spacegame.entities.stellarobj.Planet;
import org.origin.spacegame.game.GameInstance;

import java.util.Random;

public class StarSystem
{
    public int id;
    public String starType;
    public Array<Planet> planets;
    public Array<Fleet> fleets;
    private Vector2 centroid;
    private Array<Planet> stars;
    private Array<Ship> ships;

    private Vector2 galacticPosition;

    /* The screen/system is technically rectangular, for the sake of simplicity and because the camera is
    *       actually a rectangle, so this will hopefully look more natural.
    *  Source: https://stackoverflow.com/a/9735345/4326020*/
    /* The center of rectangle is the midpoint of the diagonal end points of rectangle.

    Here the midpoint is ( (x1 + x2) / 2, (y1 + y2) / 2 ).

    That means:
        xCenter = (x1 + x2) / 2
        yCenter = (y1 + y2) / 2 */
    public StarSystem(int id, String starType, float galacticX, float galacticY)
    {
        this.id = id;
        this.planets = new Array<Planet>();
        this.fleets = new Array<Fleet>();
        this.starType = starType;
        this.stars = new Array<Planet>();
        this.galacticPosition = new Vector2(galacticX, galacticY);
        this.ships = new Array<Ship>();

        float xCenter = (0f + Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH)/2f;
        float yCenter = (0f + Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT)/2f;
        this.centroid = new Vector2(xCenter, yCenter);
    }

    public void update()
    {
        updatePlanets();
        updateShips();
    }

    private void updatePlanets()
    {
        for(Planet p : this.planets)
        {
            p.update();
        }
    }

    private void updateShips()
    {
        for(Ship s : this.ships)
        {
            s.update();
        }
    }

    //Renders this star system, including its star and all constituent planets, to their positions.
    //System View must be enabled for this to work.
    public void renderSystemToSystemView(SpriteBatch batch, float delta)
    {
        /*batch.draw(getStarClass().getGfx(),
            Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH/2f,
            Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT/2f,
               Constants.StarSystemConstants.STAR_RENDER_SIZE,
               Constants.StarSystemConstants.STAR_RENDER_SIZE);*/

        renderPlanets(batch, delta);
        renderShipBackgroundCircles(GameInstance.getInstance().getShipCircleRenderer());
        renderShips(batch, delta);
    }

    private void renderPlanets(SpriteBatch batch, float delta)
    {
        batch.begin();
        for(Planet p : planets)
        {
            p.renderPlanetToSystemMap(batch);
        }
        batch.end();
    }

    private void renderShipBackgroundCircles(ShapeRenderer renderer)
    {
        renderer.begin();
        for(Ship s : ships)
        {
            s.renderBackgroundCircle(renderer);
        }
        renderer.end();
    }

    private void renderShips(SpriteBatch batch, float delta)
    {
        batch.begin();
        for(Ship s : ships)
        {
            s.renderShip(batch, delta);
        }
        batch.end();
    }

    public void addShip(Ship s)
    {
        this.ships.add(s);
    }

    public Array<Planet> getPlanets()
    {
        return this.planets;
    }

    //Adds the specific planet to the star list.
    public void addStar(Planet p)
    {
        stars.add(p);
    }

    public void addAllPlanets(Array<Planet> planets)
    {
        this.planets.addAll(planets);
        for(Planet p : planets)
        {
            if(p.getPlanetClass().isStar())
                this.addStar(p);
        }
    }

    public Array<Planet> getAllStars()
    {
        return this.stars;
    }


    public Vector2 getGalacticPosition()
    {
        return galacticPosition;
    }

    public String getStarTypeTag()
    {
        return starType;
    }

    public Vector2 getCenter()
    {
        return centroid;
    }

    public StarClass getStarClass()
    {
        return GameInstance.getInstance().getStarClass(this.getStarTypeTag());
    }

    public Array<Ship> getShips()
    {
        return this.ships;
    }

    public Array<Ship> getEnemyShipsFor(int factionID)
    {
        IPolity faction = GameInstance.getInstance().getState().getPolity(factionID);
        Array<Ship> output = new Array<>();
        Gdx.app.log("[StarSystem.getEnemyShipsFor]", "Ships list is sized at: " + ships.size);
        for(int i = 0; i < ships.size; i++)
        {
            Ship ship = ships.get(i);
            IPolity shipOwner = ship.getOwner();
            //Gdx.app.log("[StarSystem.getEnemyShipsFor]", "Faction ID: " + ((StellarNation)faction).getID());
            //Gdx.app.log("[StarSystem.getEnemyShipsFor]", "Ship Owner ID: " + ((StellarNation)shipOwner).getID());
            if(((StellarNation)shipOwner).getID() != ((StellarNation)faction).getID())
                output.add(ship);
        }
        return output;
    }

    public void debugID()
    {
        Gdx.app.log("DebugID", "ID " + id + " = " + GameInstance.getInstance().getState().getStarSystem(id).id);
    }

    Random random = new Random();
    int s = 0;
    public Vector2 getRandomPoint()
    {
        float systemWidth = Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH;
        float systemHeight = Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT;
        random.setSeed(s++);
        return new Vector2(random.nextFloat(0, systemWidth), random.nextFloat(0, systemHeight));
    }
}
