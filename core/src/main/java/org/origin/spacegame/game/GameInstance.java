package org.origin.spacegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.data.StarClass;
import org.origin.spacegame.entities.StarSystem;

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
    private XmlReader xmlReader;

    private Skin guiSkin;


    public GameInstance()
    {
        state = new GameState();
        starClasses = new ArrayMap<String, StarClass>();
        planetClasses = new ArrayMap<String, PlanetClass>();
        xmlReader = new XmlReader();
    }

    public GameState getState()
    {
        return state;
    }

    public void loadData()
    {
        loadPlanetClasses();
        loadStarClasses();
        loadSkin();
    }

    private void loadSkin()
    {
        this.guiSkin = new Skin(Gdx.files.internal("assets/gfx/skins/uiskin.json"));
    }

    public Skin getGuiSkin()
    {
        return this.guiSkin;
    }

    public StarClass getStarClass(String tag)
    {
        return starClasses.get(tag);
    }

    public PlanetClass getPlanetClass(String tag)
    {
        return planetClasses.get(tag);
    }

    //Loads planet classes from XML files in the assets/common/planet_classes folder.
    public void loadPlanetClasses()
    {
        /*FileHandle[] planetTexturePaths = Gdx.files.internal("assets/gfx/planets/").list();
        Gdx.app.log("Loading Planet Textures...", "Testing. " + planetTexturePaths.length + " planets detected.");
        for(FileHandle planetTexturePath : planetTexturePaths)
        {
            String fileName = planetTexturePath.nameWithoutExtension();
            Gdx.app.log("Texture Loaded: ", fileName);
            planetClasses.put(fileName, new PlanetClass(fileName, new Texture(planetTexturePath)));
        }*/

        FileHandle[] planetXMLDefinePaths = Gdx.files.internal("assets/common/planet_classes/").list();
        Gdx.app.log("Loading Planet XML Files...", "Testing. " + planetXMLDefinePaths.length + " planet classes detected.");
        for(FileHandle planetDefinePath : planetXMLDefinePaths)
        {
            if(planetDefinePath.extension().equals("xml"))
            {
                PlanetClass pc = loadPlanetClass(xmlReader, planetDefinePath);
                planetClasses.put(pc.getTag(), pc);
            }
        }
    }

    private PlanetClass loadPlanetClass(XmlReader reader, FileHandle handle)
    {
        XmlReader.Element root = reader.parse(handle);
        String tag = root.getAttribute("tag");
        Texture gfx = new Texture(Gdx.files.internal(root.getAttribute("textureFile")));
        float minHabitability = Float.parseFloat(root.getAttribute("min_habitability"));
        float maxHabitability = Float.parseFloat(root.getAttribute("max_habitability"));
        int minSize = Integer.parseInt(root.getAttribute("min_size"));
        int maxSize = Integer.parseInt(root.getAttribute("max_size"));
        boolean onlySpawnsInHabitableZone = yesOrNoToTrueOrFalse(root.getAttribute("only_spawns_in_habitable_zone"));
        boolean isStar = yesOrNoToTrueOrFalse(root.getAttribute("star"));
        boolean canColonize = yesOrNoToTrueOrFalse(root.getAttribute("can_colonize"));
        PlanetClass pc = new PlanetClass(tag, gfx, minHabitability,
            maxHabitability, minSize, maxSize, onlySpawnsInHabitableZone,
            isStar, canColonize);
        return pc;
    }

    private boolean yesOrNoToTrueOrFalse(String str)
    {
        if(str.toLowerCase().equals("yes"))
            return true;
        else
            return false;
    }

    public void loadStarClasses()
    {

        /*FileHandle[] starTexturePaths = Gdx.files.internal("assets/gfx/stars/").list();
        Gdx.app.log("Loading Star Textures...", "Testing. " + starTexturePaths.length + " stars detected.");
        for(FileHandle starTexturePath : starTexturePaths)
        {
            String fileName = starTexturePath.nameWithoutExtension();
            Gdx.app.log("Texture Loaded: ", fileName);
            starClasses.put(fileName, new StarClass(fileName, new Texture(starTexturePath), 1.0f));
        }*/
        Gdx.app.log("GameInstance", "Loading Star Class XML Files...");
        FileHandle[] starClassDefinesPaths = Gdx.files.internal("assets/common/star_classes/").list();
        for(FileHandle starClassDefinePath : starClassDefinesPaths)
        {
            //starClasses.put(fileName, new StarClass(fileName, new Texture(starTexturePath), 1.0f));
            StarClass starClass = loadStarClass(this.xmlReader, starClassDefinePath);
            Gdx.app.log("GameInstance: ", "Star Class " + starClass.getTag() + " has been loaded.");
            this.starClasses.put(starClass.getTag(), starClass);
        }
    }

    private StarClass loadStarClass(XmlReader reader, FileHandle handle)
    {
        XmlReader.Element root = reader.parse(handle);
        String tag = root.getAttribute("tag");
        String starPlanetClassTag = root.getAttribute("star_planet_class");
        Texture gfx = new Texture(Gdx.files.internal(root.getAttribute("textureFile")));
        float habZoneMinRadius = Float.parseFloat(root.getAttribute("hab_min_radius"));
        float habZoneMaxRadius = Float.parseFloat(root.getAttribute("hab_max_radius"));
        float energyProdBonus = Float.parseFloat(root.getAttribute("energy_prod_bonus"));
        return new StarClass(tag, starPlanetClassTag, gfx, habZoneMinRadius, habZoneMaxRadius, energyProdBonus);
    }

    private StarSystem selectedStarSystem;

    public void selectStarSystem(StarSystem system)
    {
        this.selectedStarSystem = system;
    }

    public StarSystem getSelectedStarSystem()
    {
        return selectedStarSystem;
    }

    public Array<String> getPlanetClassTags()
    {
        Array<String> planetClassTags = planetClasses.keys().toArray();
        return new Array<String>(planetClassTags);
    }

    public Array<String> getStarClassTags()
    {
        Array<String> starClassTags = starClasses.keys().toArray();
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
