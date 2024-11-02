package org.origin.spacegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;
//import org.luaj.vm2.ast.Chunk;
//import org.luaj.vm2.script.LuaScriptEngine;
//import org.luaj.vm2.script.LuaScriptEngineFactory;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.data.StarClass;
import org.origin.spacegame.entities.stellarobj.Planet;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.generation.OrbitalZone;
import org.origin.spacegame.utilities.StringToType;

import java.util.Random;

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
    private Random random;
    private Skin guiSkin;
    private StarSystem selectedStarSystem;
    private Planet selectedPlanet;
    private ArrayMap<String, Skin> skins;


    public GameInstance()
    {
        state = new GameState();
        starClasses = new ArrayMap<String, StarClass>();
        planetClasses = new ArrayMap<String, PlanetClass>();
        xmlReader = new XmlReader();
        this.random = new Random();
        this.skins = new ArrayMap<String, Skin>();

        /*String script = "val = 'hello from lua'";
        LuaScriptEngine eng = (LuaScriptEngine) new LuaScriptEngineFactory().getScriptEngine();
        try {
            eng.eval(script);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        String ret = (String)eng.get("val");
        Gdx.app.log("ScriptEngine", ret);*/
    }

    public Random getRandom()
    {
        return random;
    }

    public GameState getState()
    {
        return state;
    }

    public Skin getSkin(String skinName)
    {
        return this.guiSkin;
    }

    public void loadData()
    {
        loadPlanetClasses();
        loadStarClasses();
        loadDefaultSkin();
    }

    public void selectPlanet(Planet planet)
    {
        this.selectedPlanet = planet;
    }

    public Planet getSelectedPlanet()
    {
        return selectedPlanet;
    }

    private void loadSkins()
    {
        Array<FileHandle> skinFileHandles = new Array<FileHandle>();
        FileHandle[] skinFolderFileHandles = Gdx.files.internal("assets/gfx/ui/skins/").list();

        //Get the skin JSON files.
        for(FileHandle skinFolderFileHandle : skinFolderFileHandles)
        {
            if(skinFolderFileHandle.extension().equals(".json"))
                skinFileHandles.add(skinFolderFileHandle);
        }

        for(FileHandle skinFileHandle : skinFileHandles)
        {
            this.skins.put(skinFileHandle.nameWithoutExtension(), new Skin(skinFileHandle));
        }
    }

    @Deprecated
    private void loadDefaultSkin()
    {
        this.guiSkin = new Skin(Gdx.files.internal("assets/gfx/ui/skins/uiskin.json"));
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

    /**
     * &#064;returns a list of all planet classes that are flagged as being able to spawn in the given orbital zone.
     */
    @SuppressWarnings("GDXJavaUnsafeIterator")
    public Array<PlanetClass> getPlanetClassesThatCanSpawnInZone(OrbitalZone zone, boolean includeStars, boolean terrestrialOnly)
    {
        Array<PlanetClass> retval = new Array<PlanetClass>();
        for(PlanetClass planetClass : this.planetClasses.values())
        {
            OrbitalZone l_zone = planetClass.getSpawningZone();
            if(l_zone == zone || l_zone == OrbitalZone.ANY)
            {
                if(!includeStars && !planetClass.isStar())
                    retval.add(planetClass);
            }
        }
        return retval;
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
        //boolean onlySpawnsInHabitableZone = yesOrNoToTrueOrFalse(root.getAttribute("only_spawns_in_habitable_zone"));
        boolean isStar = StringToType.yesOrNoToTrueOrFalse(root.getAttribute("star"));
        boolean canColonize = StringToType.yesOrNoToTrueOrFalse(root.getAttribute("can_colonize"));
        boolean isTerrestrial = true;

        float habZoneRadius = 0f;
        float meltZoneRadius = 0f;
        float freezeZoneRadius = 0f;

        if(isStar)
        {
            meltZoneRadius = Float.parseFloat(root.getAttribute("melting_zone_radius"));
            habZoneRadius = Float.parseFloat(root.getAttribute("habitable_zone_radius"));
            freezeZoneRadius = Float.parseFloat(root.getAttribute("freezing_zone_radius"));
            isTerrestrial = false;
        }

        if(root.hasAttribute("terrestrial"))
        {
            String terrestrial = root.getAttribute("terrestrial");
            isTerrestrial = StringToType.yesOrNoToTrueOrFalse("terrestrial");
        }

        String spawningZone = "HABITABLE";
        if(root.hasAttribute("spawning_zone"))
            spawningZone = root.getAttribute("spawning_zone");
        else
            Gdx.app.log("Spawning Zone Debug", "Planet Class " + root.getName() + " has no valid defined spawning zone.");
        PlanetClass pc = new PlanetClass(tag, gfx, minHabitability,
                maxHabitability, minSize, maxSize, spawningZone,
                isStar, canColonize, meltZoneRadius,
                habZoneRadius, freezeZoneRadius, isTerrestrial);
        return pc;
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

    private SpaceGame game;
    public void setGame(SpaceGame game)
    {
        this.game = game;
    }

    public CameraManager getCameraManager()
    {
        return game.getCameraManager();
    }

    public boolean isSystemSelected()
    {
        return this.selectedStarSystem == null;
    }

    public boolean isPlanetSelected()
    {
        return this.selectedPlanet == null;
    }

    @Override
    public void dispose()
    {
        disposeOfStarData();
        disposeOfPlanetData();
        disposeOfGUIData();
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

    private void disposeOfGUIData()
    {
        for(Skin skin : this.skins.values())
        {
            skin.dispose();
        }
    }
}
