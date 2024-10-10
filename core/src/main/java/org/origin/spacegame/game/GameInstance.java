package org.origin.spacegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import org.origin.spacegame.Constants;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.data.StarClass;

import java.util.HashMap;

public class GameInstance implements Disposable
{
    private static GameInstance instance;

    public static GameInstance getInstance()
    {
        if(instance == null)
            instance = new GameInstance();
        return instance;
    }

    private GameState state;
    private ArrayMap<String, StarClass> starClasses;
    private ArrayMap<String, PlanetClass> planetClasses;

    public GameInstance()
    {
        state = new GameState();
        starClasses = new ArrayMap<String, StarClass>();
        planetClasses = new ArrayMap<String, PlanetClass>();
    }

    public GameState getState()
    {
        return state;

    }

    public void loadData()
    {
        loadStarClasses();
        loadPlanetClasses();
    }

    public StarClass getStarClass(String tag)
    {
        return starClasses.get(tag);
    }

    public PlanetClass getPlanetClass(String tag)
    {
        return planetClasses.get(tag);
    }

    public void loadStarClasses()
    {

        FileHandle[] starTexturePaths = Gdx.files.internal("assets/gfx/stars/").list();
        Gdx.app.log("Loading Star Textures...", "Testing. " + starTexturePaths.length + " stars detected.");
        for(FileHandle starTexturePath : starTexturePaths)
        {
            String fileName = starTexturePath.nameWithoutExtension();
            Gdx.app.log("Texture Loaded: ", fileName);
            starClasses.put(fileName, new StarClass(fileName, new Texture(starTexturePath), 1.0f));
        }
    }

    public void loadPlanetClasses()
    {
        FileHandle[] planetTexturePaths = Gdx.files.internal("assets/gfx/planets/").list();
        Gdx.app.log("Loading Planet Textures...", "Testing. " + planetTexturePaths.length + " planets detected.");
        for(FileHandle planetTexturePath : planetTexturePaths)
        {
            String fileName = planetTexturePath.nameWithoutExtension();
            Gdx.app.log("Texture Loaded: ", fileName);
            planetClasses.put(fileName, new PlanetClass(fileName, new Texture(planetTexturePath)));
        }
    }

    public Array<String> getPlanetClassTags()
    {
        String[] planetClassTags = planetClasses.keys().toArray().toArray();
        return new Array<String>(planetClassTags);
    }

    public Array<String> getStarClassTags()
    {
        String[] starClassTags = starClasses.keys().toArray().toArray();
        return new Array<String>(starClassTags);
    }

    @Override
    public void dispose()
    {
        disposeOfStarData();
        disposeOfPlanetData();
    }

    private void disposeOfStarData()
    {
        for(StarClass starType : starClasses.values())
        {
            starType.dispose();
        }
    }

    private void disposeOfPlanetData()
    {
        for(PlanetClass planetType : planetClasses.values())
        {
            planetType.dispose();
        }
    }
}
