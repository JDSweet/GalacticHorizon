package org.origin.spacegame.map.hex;

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
                cells[row][col] = new HexCell(row, col, "default", this);
            }
        }
    }
}
