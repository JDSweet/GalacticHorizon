package org.origin.spacegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.*;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.origin.spacegame.ai.ScriptedShipState;
import org.origin.spacegame.data.PlanetClass;
import org.origin.spacegame.data.ShipClass;
import org.origin.spacegame.data.ShipClass.ShipClassType;
import org.origin.spacegame.data.StarClass;
import org.origin.spacegame.entities.stellarobj.Planet;
import org.origin.spacegame.entities.galaxy.StarSystem;
import org.origin.spacegame.generation.OrbitalZone;
import org.origin.spacegame.gui.ScriptedGUIScene;
import org.origin.spacegame.map.hex.Terrain;
import org.origin.spacegame.screens.StarSystemScreen;
import org.origin.spacegame.utilities.StringToType;

import java.util.Dictionary;
import java.util.Random;

public class GameInstance implements Disposable
{
    private static GameInstance instance;
    public static GameInstance getInstance()
    {
        if (instance == null)
            instance = new GameInstance();
        return instance;
    }

    private GameState state;
    private ArrayMap<String, StarClass> starClasses;
    private ArrayMap<String, PlanetClass> planetClasses;
    private ArrayMap<String, ShipClass> shipClasses;
    private XmlReader xmlReader;
    private Random random;
    private Skin guiSkin;
    private StarSystem selectedStarSystem;
    private Planet selectedPlanet;
    private ArrayMap<String, Skin> skins;
    private ShapeRenderer shipCircleRenderer;
    private LuaValue shipAIScripts;
    private ArrayMap<String, ScriptedShipState> shipAIStates;
    private ArrayMap<String, Terrain> terrains;

    private ArrayMap<String, String> flags;
    private ArrayMap<String, String> strings;
    private ObjectFloatMap<String> variables;

    public GameInstance()
    {
        state = new GameState();
        starClasses = new ArrayMap<String, StarClass>();
        planetClasses = new ArrayMap<String, PlanetClass>();
        shipClasses = new ArrayMap<String, ShipClass>();
        xmlReader = new XmlReader();
        this.random = new Random();
        this.skins = new ArrayMap<String, Skin>();
        shipCircleRenderer = new ShapeRenderer();
        shipCircleRenderer.setAutoShapeType(true);
        this.shipAIScripts = JsePlatform.standardGlobals();
        this.shipAIStates = new ArrayMap<String, ScriptedShipState>();
        this.terrains = new ArrayMap<String, Terrain>();

        this.flags = new ArrayMap<>();
        this.strings = new ArrayMap<>();
        this.variables = new ObjectFloatMap<>();

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
        loadShipAIScripts();
        loadTerrain();
        loadPlanetClasses();
        loadStarClasses();
        loadDefaultSkin();
        loadShipClasses();
    }

    public Terrain getTerrain(String tag)
    {
        return this.terrains.get(tag);
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
        for (FileHandle skinFolderFileHandle : skinFolderFileHandles)
        {
            if (skinFolderFileHandle.extension().equals(".json"))
                skinFileHandles.add(skinFolderFileHandle);
        }

        for (FileHandle skinFileHandle : skinFileHandles)
        {
            this.skins.put(skinFileHandle.nameWithoutExtension(), new Skin(skinFileHandle));
        }
    }

    public void loadTerrain()
    {
        FileHandle[] files = Gdx.files.internal("assets/common/surface_tiles/").list();
        for(FileHandle file : files)
        {
            if(file.extension().equalsIgnoreCase("xml"))
            {
                XmlReader.Element element = xmlReader.parse(file);
                Terrain terrain = new Terrain(element);
                terrains.put(terrain.getTag(), terrain);
            }
        }
    }

    public ShapeRenderer getShipCircleRenderer()
    {
        return this.shipCircleRenderer;
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
        for (PlanetClass planetClass : this.planetClasses.values())
        {
            OrbitalZone l_zone = planetClass.getSpawningZone();
            if (l_zone == zone || l_zone == OrbitalZone.ANY)
            {
                if (!includeStars && !planetClass.isStar())
                    retval.add(planetClass);
            }
        }
        return retval;
    }

    public void loadShipAIScripts()
    {
        FileHandle[] xmlFiles = Gdx.files.internal("assets/common/ship_ai_states/").list();
        FileHandle[] luaFiles = Gdx.files.internal("assets/scripts/ai/ship/").list();
        for(FileHandle luaFile : luaFiles) //We have to read the lua files first,
            // because the ScriptedShipState constructor expects the scripts to already be loaded.
        {
            this.shipAIScripts.get("dofile").call(luaFile.path());
        }
        for(FileHandle xmlFile : xmlFiles)
        {
            XmlReader.Element root = this.xmlReader.parse(xmlFile);
            for(XmlReader.Element child : root.getChildrenByName("ShipAIState"))
            {
                ScriptedShipState shipAIState = new ScriptedShipState(child, shipAIScripts);
                Gdx.app.log("[GameInstance.loadShipAIScripts Debug]", "Creating Ship AI State " + shipAIState.getTag());
                this.shipAIStates.put(shipAIState.getTag(), shipAIState);
                /*for(ObjectMap.Entry<String, String> attrib : child.getAttributes())
                {
                    if(attrib.key.equals("tag"))
                    {

                    }
                }*/
            }
        }
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
        for (FileHandle planetDefinePath : planetXMLDefinePaths)
        {
            if (planetDefinePath.extension().equals("xml"))
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

        if (isStar)
        {
            meltZoneRadius = Float.parseFloat(root.getAttribute("melting_zone_radius"));
            habZoneRadius = Float.parseFloat(root.getAttribute("habitable_zone_radius"));
            freezeZoneRadius = Float.parseFloat(root.getAttribute("freezing_zone_radius"));
            isTerrestrial = false;
        }

        if (root.hasAttribute("terrestrial"))
        {
            String terrestrial = root.getAttribute("terrestrial");
            isTerrestrial = StringToType.yesOrNoToTrueOrFalse("terrestrial");
        }

        String spawningZone = "HABITABLE";
        if (root.hasAttribute("spawning_zone"))
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
        for (FileHandle starClassDefinePath : starClassDefinesPaths) {
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

    //Loads planet classes from XML files in the assets/common/planet_classes folder.
    public void loadShipClasses()
    {
        FileHandle[] shipClassXMLDefinePaths = Gdx.files.internal("assets/common/ship_classes/").list();
        Gdx.app.log("Loading Ship Class XML Files...", "Testing. " + shipClassXMLDefinePaths.length + " ship classes detected.");
        for (FileHandle shipClassDefinePath : shipClassXMLDefinePaths)
        {
            if (shipClassDefinePath.extension().equals("xml"))
            {
                ShipClass sc = loadShipClass(xmlReader, shipClassDefinePath);
                shipClasses.put(sc.getTag(), sc);
            }
        }
    }

    private ShipClass loadShipClass(XmlReader reader, FileHandle handle)
    {
        XmlReader.Element root = reader.parse(handle);
        String tag = root.getAttribute("tag");
        Texture gfx = new Texture(Gdx.files.internal(root.getAttribute("texture")));
        ShipClassType type;
        String typeTag = root.getAttribute("type");
        Vector2 maxVel = new Vector2();
        switch (typeTag)
        {
            case "MILITARY":
            case "military":
                type = ShipClassType.MILITARY;
                break;
            case "CIVILIAN":
            case "civilian":
                type = ShipClassType.CIVILIAN;
                break;
            default:
                type = ShipClassType.UNDEFINED;
        }
        String[] maxVelStrVals = root.getAttribute("max_vel").replaceAll("[(){}<>\\[\\]]", "").replaceAll("\\s", "").split(",");
        maxVel.set(Float.parseFloat(maxVelStrVals[0]), Float.parseFloat(maxVelStrVals[1]));
        float maxAccel = Float.parseFloat(root.getAttribute("max_accel"));
        float maxHP = Float.parseFloat(root.getAttribute("hp"));
        float spriteOffset = Float.parseFloat(root.getAttribute("sprite_offset"));
        return new ShipClass(tag, gfx, maxVel, maxAccel, maxHP, spriteOffset, type);
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

    public ShipClass getShipClass(String tag)
    {
        return this.shipClasses.get(tag);
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

    public ScriptedShipState getShipAIState(String tag)
    {
        if(shipAIStates.containsKey(tag))
        {
            return shipAIStates.get(tag);
        }
        else
        {
            Gdx.app.log("GameInstance Debug: getShipAIState()", "The ship state " + tag + " does not exist. Returning a null value.");
            return null;
        }
    }


    //A lot of these "unused" methods are referenced in Lua scripts. DO NOT REMOVE THEM!!!
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

    public void deselectPlanet()
    {
        this.selectedPlanet = null;
    }

    public void deselectSystem()
    {
        this.selectedStarSystem = null;
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
        for (StarClass starType : starClasses.values())
        {
            starType.dispose();
        }
    }

    private void disposeOfPlanetData()
    {
        for (PlanetClass planetType : planetClasses.values())
        {
            planetType.dispose();
        }
    }

    private void disposeOfGUIData()
    {
        for (Skin skin : this.skins.values())
        {
            skin.dispose();
        }
    }

    private boolean shipSpawnMode = false;
    public void toggleShipSpawnMode()
    {
        this.shipSpawnMode = !shipSpawnMode;
    }

    public boolean isSpawnModeEnabled()
    {
        return this.shipSpawnMode;
    }

    public void log(String tag, String txt)
    {
        Gdx.app.log(tag, txt);
    }

    public Vector2 vec2()
    {
        return new Vector2();
    }

    public Vector2 vec2(float x, float y)
    {
        return new Vector2(x, y);
    }

    public Vector2 vec2(Vector2 other)
    {
        log("GameInstance Debug", "Vector 2 created at " + other.toString());
        return new Vector2(other);
    }

    public void saveVariable(String name, float value)
    {
        variables.put(name, value);
    }

    public void saveFlag(String name)
    {
        flags.put(name, name);
    }

    public float getVariable(String name)
    {
        return variables.get(name, 0f);
    }

    public void setString(String name, String value)
    {
        this.strings.put(name, value);
    }

    public String getString(String name)
    {
        if(this.strings.containsKey(name))
            return strings.get(name);
        else
            return "NULL";
    }

    public boolean hasFlag(String name)
    {
        return flags.containsKey(name);
    }

    public boolean hasString(String name)
    {
        return this.strings.containsKey(name);
    }

    public float dst(Vector2 a, Vector2 b)
    {
        return a.dst(b);
    }

    //public ScriptedGUIScene getStarSystemSceneIfOpen()
    //{
        //Screen starSystemScreen = game.getScreen();
        //if(starSystemScreen != null && starSystemScreen instanceof StarSystemScreen)
            //return starSystemScreen.get
    //}
}
