package org.origin.spacegame.map.hex;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.origin.spacegame.game.GameInstance;

public class HexCell
{
    private HexMap map; //This is the map this cell is a part of.
    private Vector2 position;
    private int row, col;
    private String terrainTag;
    private Terrain terrain;

    public HexCell(int row, int col, String terrain, HexMap map) {
        this.position = new Vector2();
        this.row = row;
        this.col = col;
        this.map = map;
        this.terrainTag = terrain;
        this.terrain = GameInstance.getInstance().getTerrain(terrainTag);
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(terrain.getGfx(), getPosition().x, getPosition().y, hexWidth, hexHeight);
    }

    float hexWidth = 1f;
    float hexHeight = 1f;

    //Every other row is offset.
    public Vector2 getPosition()
    {
        position.x = col * hexWidth * 1.5f + row * hexWidth / 2f;
        position.y = row * hexHeight * 0.866f; // 0.866f is sin(60°)
        return position;
    }

    public Terrain getTerrain()
    {
        return null;
    }
}
