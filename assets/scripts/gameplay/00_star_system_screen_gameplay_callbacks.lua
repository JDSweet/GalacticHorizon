
planet_overview_window_visible = false;


function on_click(touchPos, star_system, scene, game_instance, game_state)
    local window = scene:getWidgetByID('planet_overview_window'):getWidget()
    --local window = window_wrapper.widget;
    if window:isVisible() then
        window:setVisible(false)
    end
    print('[Lua Debug] Planet un-selected.')
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
