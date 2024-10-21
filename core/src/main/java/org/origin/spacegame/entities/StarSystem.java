package org.origin.spacegame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.origin.spacegame.Constants;
import org.origin.spacegame.data.StarClass;
import org.origin.spacegame.game.GameInstance;
import org.origin.spacegame.game.GameState;

public class StarSystem
{
    public int id;
    public String starType;
    public Array<Planet> planets;
    public Array<Fleet> fleets;
    private Vector2 centroid;
    private Array<Planet> stars;

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

        float xCenter = (0f + Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH)/2f;
        float yCenter = (0f + Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT)/2f;
        this.centroid = new Vector2(xCenter, yCenter);
    }

    //Renders this star system, including its star and all constituent planets, to their positions.
    //System View must be enabled for this to work.
    public void renderSystemToSystemView(SpriteBatch batch)
    {
        /*batch.draw(getStarClass().getGfx(),
            Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_WIDTH/2f,
            Constants.StarSystemConstants.STAR_SYSTEM_INTERNAL_HEIGHT/2f,
               Constants.StarSystemConstants.STAR_RENDER_SIZE,
               Constants.StarSystemConstants.STAR_RENDER_SIZE);*/
        for(Planet p : planets)
        {
            p.renderPlanet(batch);
        }
    }

    //Adds the specific planet to the star list.
    public void addStar(Planet p)
    {
        stars.add(p);
    }

    public void addAllPlanets(Array<Planet> planets)
    {
        this.planets.addAll(planets);
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

    public void debugID()
    {
        Gdx.app.log("DebugID", "ID " + id + " = " + GameInstance.getInstance().getState().getStarSystem(id).id);
    }
}
