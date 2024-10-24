
planet_overview_window_visible = false;


function on_click(touchPos, star_system, scene, game_instance, game_state)
    window = scene:getWidgetByID('planet_overview_window')
    if window:isVisible() then
        window:setVisible(false)
    end
    print('[Lua Debug] Planet un-selected.')
end

function on_planet_clicked(touchPos, planet, scene, game_instance, game_state)
    window = scene:getWidgetByID('planet_overview_window')
    if not window:isVisible() then
        window:setVisible(true)
    end
    print('[Lua Debug] Planet ' .. planet.id .. " selected.")
end
