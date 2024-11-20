Pre-Alpha Space Game Project Roadmap:

v 0.3: Planetary Terrain Generation and Solar System mechanics, UI extensions
- Scale back and temporarily disable the galactic map/galaxy generation.
- Refactor system generation to add moon generation.
- Possible refactoring of GameInstance/GameState to no longer use singletons.
    - The main bottleneck here is, passing everything a reference to the GameInstance and GameState would be annoying, and a lot of the code is designed around the presence of a singleton.
    - The main issue with the singleton pattern here is, it leads some of the codebase to spaghettify (in that, a function might call another function, which itself calls GameInstance, which calls another function which also references stuff in GameInstance, and so on and so forth). I’ve actually run into weird stack-overflow errors a couple of times, because functions ended up calling themselves indirectly through the GameInstance, and while those bits of code got cleaned up, I think it’s more a result of the pattern.
    - I guess the main question here is, is completely refactoring my code-base worth the delay in productivity that it would cause (time spent refactoring is time not spent working on the game).
- Make sure the system you generate inside of is interesting and has multiple worlds.
- The ability to select/deselect individual ships, and groups of ships.
- If you click and drag your mouse, a box is drawn, and any ships in that box are selected.
- We only draw circles around selected ships.
- The ability to use your ships to settle planets.
  - You select your colony ship.
  - You right click a planet.
  - A context-window with a list of possible actions/targeted decisions is generated and displayed.
  - The option to deploy colonizers is shown.
  - If you choose the option to colonize the planet, your selected ships are sent to the planet.
  - When your ships arrive, the game screen is taken to the surface of that planet, and you are prompted to select where you want to land your colonizer units.
- You can build stations of several varieties in those orbital slots (shipyards, defense platforms, etc).
  - Right click the hammer/+ icon.
  - Select the station you want to build.
- Planetary Terrain System
- Use Perlin Noise to generate the “height” of each tile in each planet.
- Generate terrains based on planet-class and tile height (each planet class should have a list of planet tiles that it can generate, and each tile should have a minHeight that it can generate at. minHeight should be relative to sea level).

v 0.4: Country AI, Ship AI, Economy System base, decision system, event system, UI expansions/extensions.
- Add the economy system (Gasses -> Energy, Minerals -> Alloys, and pops in cities generate units)
  - Energy
  - Minerals
  - Alloys
  - Gasses
  - Influence
- Add support for AI Polities
- National AI
  - State Machine
    - Exploring
    - Exploiting
    - Expanding
    - Exterminating
- Decision/Event System
- Ship AI
  - Aggressive: Ship seeks out and attacks nearest non-allied ship in the system.
  - Defensive_Travel_Enabled: Ship seeks out the nearest friendly world or asset in the system and patrols near it. If none is present, the ship will go to any allies in the system that are being attacked to assist. If no allies in the system are being attacked and no friendly worlds or assets are in the system, the ship will attack any enemy ships in the system. If no enemy fleets are in the system, it will go to the nearest friendly system where there are enemies present. A specific asset or planet to defend can optionally be selected for this ship.
  - Defensive_Travel_Disabled: This functions like Defensive_Travel_Enabled, except the ship in question will remain in the system it is currently in, defending the most valuable asset or planet in the system by default, or defending a planet or asset that is selected to be defended.
  - TravelingToDefensivePriority: This ship will travel to the nearest system that the AI has flagged as a defensive priority, and will enter into Defensive_Travel_Disabled once it arrives. It will only leave this state after receiving a telegram from its owner to switch to another state.
  - TravelingTo: This ship will travel to a specified system, only engaging in combat or fleeing if it is actively attacked.
  - Fleeing: This ship will try to flee to the nearest friendly system with no enemy forces if it detects enemy forces present. When the exit conditions for this state are satisfied, it transitions to the ship-class’s default behavioral state.
  - Colonizing: This ship will go to the nearest friendly planet and will attempt to colonize it.
  - Trading: This ship will chart and travel random paths between allied planets, fleeing any enemies it encounters.
- Add the event system.
  - We need a bunch of test events. 
    - Define them in XML.
    - Write the logic in Lua.
    - These are going to be a pain to write.
Add more decisions/the decision system.
  - Defined in XML
  - Written in Lua

v 0.5: Ship Modules, Ship Design, Weapons Systems:
- Ships should have weapon slots that various different weapons can be added to.
- These should be defined using XML and behavior should be implemented using Lua (possibly - there might be enough ships in the late-game that defining behavior for individual guns in Lua could be too slow. In any case, moving behavior to Java shouldn’t be too difficult if it turns out to be problematic from a performance perspective).

v 0.6: Planet Settlement Mechanics:
- Formers construct districts and terraform tiles.
- Resource generation happens on a per-planet basis. The actual placement of buildings and factories on-world is mostly going to be superficial, with the only real system that is impacted by placement being surface combat mechanics (i.e. invading planets).

v 0.7: Planet movement mechanics:
- Pathfinding for units on the planet.
- Combat between units on a planet of different factions.

v 0.8: Ship combat mechanics.
- Ship combat behavior is going to get refactored and debugged here to work with the systems implemented in v 0.5.

v 0.9: Interstellar mechanics
- Interstellar mechanics are going to return to active development here.
- FTL Travel
- Star system ownership?
- Interstellar polities - how do they work?

v 1.0 (Possible Patreon Release)
