package org.origin.spacegame.utilities;

/*
* This class creates an imaginary tile map of [map width] * [map height]. A source
* image is loaded from memory (the image resolution must match the map size), where black
* tiles represent default empty tiles, and white tiles represent sector centers.
* Sectors represent [generation sector width] * [generation sector height]
* tile slices of the map where stars are more likely to generate (30% by default, compared
* to a default base 5% chance in a non-sector tile). If an empty tile borders a tile with a
* star system in it, the chance of it having a star system increases by 10% (to a minimum of 15%).
*
* Because star system generation happens sector-by-sector, with no regard for sector overlap,
* a surefire way to ensure greater star density in a region is to pack multiple sectors close
* to each-other.
* */
public class TileMapStarSystemGenerator
{

}
