package org.origin.spacegame.map.hex;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HexMap
{
    private HexCell[][] cells;

    public HexMap(int width, int height)
    {
        this.cells = new HexCell[width][height];
        for(int row = 0; row < cells.length; row++)
        {
            for(int col = 0; col < cells[row].length; col++)
            {
                cells[row][col] = new HexCell(row, col, "00_grass_tile", this);
            }
        }
    }

    public void draw(SpriteBatch batch)
    {
        for(int x = 0; x < cells.length; x++)
        {
            for(int y = 0; y < cells[x].length; y++)
            {
                cells[x][y].draw(batch);
            }
        }
    }

    public HexCell[][] getCells()
    {
        return this.cells;
    }
}
