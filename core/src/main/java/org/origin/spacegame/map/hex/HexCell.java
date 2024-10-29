package org.origin.spacegame.map.hex;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class HexCell
{
    private HexMap map; //This is the map this cell is a part of.
    private Vector2 position;
    private int row, col;
    private String terrain;

    public HexCell(int row, int col, String terrain, HexMap map)
    {
        this.position = new Vector2();
        this.row = row;
        this.col = col;
        this.map = map;
        this.terrain = terrain;
    }

    //Every other row is offset.
    public Vector2 getPosition()
    {
        if(row % 2 == 0) //Even numbered row. Offset the position.
            position.x = (float)row+1;
        else
            position.x = (float)row;
        position.y = (float)col;

        return position;
    }

    public Terrain getTerrain()
    {
        return null;
    }
}
