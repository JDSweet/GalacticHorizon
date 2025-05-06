touchDst = 2

planet_overview_window_visible = false;

function on_click(touchPos, star_system, scene, game_instance, game_state)
    local window = scene:getWidgetByID('planet_overview_window'):getWidget()
    local shipWindow = scene:getWidgetByID('ship_debug_overview_window'):getWidget()
    --local window = window_wrapper.widget;
    if window:isVisible() then
        window:setVisible(false)
        game_instance:deselectPlanet();
        print('[00_star_system_screen_gameplay_callbacks.on_click Debug] Planet un-selected.')
    end
    if shipWindow ~= nil and shipWindow:isVisible() and game_instance:getSelectedStarSystem():isShipSelected() then
        shipWindow:setVisible(false)
        local selectedShip = game_instance:getSelectedStarSystem():getSelectedShip()
        if selectedShip ~= nil and game_instance:vec2(touchPos):dst(selectedShip:getPosition().x, selectedShip:getPosition().y) > touchDst then
            selectedShip:deselect()
        end
    end
    --spawnShip(String shipClassTag, Vector2 pos, Vector2 vel, Vector2 facing, int polityID)
    if(game_instance:hasString("game_mode") and game_instance:getString("game_mode") == "ship_mode_spawn") then
        local pos = game_instance:vec2();
        pos.x = touchPos.x;
        pos.y = touchPos.y;
        local facing = game_instance:vec2(pos.x, pos.y);
        local ship_class = "battleship";
        local vel = game_instance:vec2(0, 0);
        local player_idx = 0 --game_state:getPlayerID();
        if game_instance:getString("selected_faction") == "faction1" then
            player_idx = 0
        elseif game_instance:getString("selected_faction") == "faction2" then
            player_idx = 1
        elseif game_instance:getString("selected_faction") == "faction3" then
            player_idx = 2
        else
            player_idx = 6
        end
        print('[00_star_system_screen_gameplay_callbacks: Ship Spawning Debug: Player Index] ' .. player_idx)
        local ship = game_state:spawnShip(ship_class, pos, vel, facing, player_idx)
        --ship:setSystemLocation(game_instance:getSelectedStarSystem())
        --game_instance:getSelectedStarSystem():addShip(ship)
        --ship:turnTowards(pos)
    elseif(game_instance:hasString("game_mode") and game_instance:getString("game_mode") == "ship_mode_move") then
        local ship = star_system:getClosestShipToPoint(touchPos.x, touchPos.y)
        if ship ~= nil then
            on_ship_clicked(touchPos, ship, star_system, scene, game_instance, game_state)
        end
    else
        print("[00_star_system_screen_gameplay_callbacks.on_click Debug] Current Game mode isn't implemented.")
    end
end

function on_ship_clicked(touchPos, ship, star_system, scene, game_instance, game_state)
    local shipDebugWindow = scene:getWidgetByID("ship_debug_overview_window")
    local shipIDLabel = scene:getWidgetByID('ship_id_label')
    local shipTargetIDLabel = scene:getWidgetByID('ship_target_id_label')
    local shipAIStateLabel = scene:getWidgetByID("ship_ai_state_label")
    if ship:getPosition():dst(game_instance:vec2(touchPos.x, touchPos.y)) <= touchDst then
        if ship:isSelected() then
            ship:deselect()
            star_system:deselectShip()
        else
            ship:select()
            shipDebugWindow:getWidget():setVisible(true)
            local prevSelectedShip = star_system:getSelectedShip()
            if prevSelectedShip ~= nil then
                prevSelectedShip:deselect()
            end
            star_system:selectShip(ship)
            shipIDLabel.widget:setText('Ship ID: ' .. ship:getID())
            shipAIStateLabel.widget:setText("AI State: " .. ship:getStateMachine():getCurrentState():getTag())
            if ship:getTarget() ~= nil then
                shipTargetIDLabel.widget:setText("Target ID: " .. ship:getTarget():getID())
            else
                shipTargetIDLabel.widget:setText("Target ID: NIL")
            end
        end
    end
end

-- This function selects the planet that was clicked and sets the data fields for
-- all the different widgets relating to planet management based on that selection.
function on_planet_clicked(touchPos, planet, scene, game_instance, game_state)
    game_instance:selectPlanet(planet)

    local window = scene:getWidgetByID('planet_overview_window'):getWidget()
    local titleLabel = window:getTitleLabel()
    titleLabel:setText('Planet ' .. planet:getID())
    if not window:isVisible() then
        window:setVisible(true)
    end

    local hab_label = scene:getWidgetByID('planet_habitability_label'):getWidget()
    local size_label = scene:getWidgetByID('planet_size_label'):getWidget()
    local planet_class = scene:getWidgetByID('planet_class_label'):getWidget()
    local orbital_zone = scene:getWidgetByID('oz_debug_label'):getWidget()
    hab_label:setText('Habitability: ' .. game_instance:getSelectedPlanet():getHabitabilityRounded())
    size_label:setText('Planet Size: ' .. game_instance:getSelectedPlanet():getSize())
    planet_class:setText('Planet Class: ' .. game_instance:getSelectedPlanet():getPlanetClass():getTag())
    orbital_zone:setText('Orbital Zone ' .. game_instance:getSelectedPlanet():getOrbitalZone():name())

    print('[Lua Debug] Planet ' .. planet.id .. " selected.")
end
