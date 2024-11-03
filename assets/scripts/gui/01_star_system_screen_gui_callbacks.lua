

--This sends us back to the galaxy screen.
--setRenderView is case insensitive.
--gameState is a reference to the currently active game state.
--widget is the widget that has been touched (in this case, our ScriptableTextButton).
function galaxy_screen_btn_on_click(widget, game_instance, game_state)
    print('[Lua Debug] Galaxy screen button clicked!')
    game_instance:getCameraManager():setRenderView("GALACTIC");
end

--This updates the system text label on the scripted GUI screen.
--If no star system is selected, we just set the value to the default,
--But if a star system is selected, we set the text appropriately.
function system_name_label_on_show(widget, game_instance, game_state)
    print('[Lua Debug] System name label showing...')
    if game_instance:isSystemSelected() then
        widget:setText('Star System #X');
        print('No system selected.')
    else
        print('System selected')
        widget:setText('Star System # ' .. game_instance:getSelectedStarSystem().id);
    end
end

function system_name_label_on_hide(widget, game_instance, game_state)
    print('[Lua Debug] System name label hiding...')
end

function visit_planet_btn_on_show(widget, game_instance, game_state)
    print('[Lua Debug] Visit planet button showing...')
end

function visit_planet_btn_on_hide(widget, game_instance, game_state)
    print('[Lua Debug] Visit planet button hiding...')
end

function visit_planet_btn_on_click(widget, game_instance, game_state)
    print('[Lua Debug] Visit planet button clicked!')
end

function planet_overview_window_on_create(widget, game_instance, game_state)
    print('[Lua Debug] PLanet overview window being created...')
end

function planet_overview_window_on_show(widget, game_instance, game_state)
    print('[Lua Debug] PLanet overview window showing...')
end

function planet_overview_window_on_hide(widget, game_instance, game_state)
    print('[Lua Debug] PLanet overview window hiding...')
end

function planet_habitability_label_on_create(widget, game_instance, game_state)
    print('[Lua Debug] PLanet habitability label being created...')
    if(game_instance:isPlanetSelected()) then
        widget:setText('Habitability: ' .. game_instance:getSelectedPlanet():getHabitability())
    end
end

function planet_habitability_label_on_show(widget, game_instance, game_state)
    print('[Lua Debug] PLanet habitability label showing...')
end

function planet_habitability_label_on_hide(widget, game_instance, game_state)
    print('[Lua Debug] PLanet habitability label hiding...')
end

function planet_size_label_on_create(widget, game_instance, game_state)
    print('[Lua Debug] PLanet size label being created...')
end

function planet_size_label_on_show(widget, game_instance, game_state)
    print('[Lua Debug] PLanet size label showing...')
end

function planet_size_label_on_hide(widget, game_instance, game_state)
    print('[Lua Debug] PLanet size label hiding...')
end

function empire_management_btn_on_click(widget, game_instance, game_state)
    print('[Lua Debug] Empire management button clicked...')
end

function empire_economy_btn_on_click(widget, game_instance, game_state)
    print('[Lua Debug] Empire economy button clicked...')
end

function empire_military_btn_on_click(widget, game_instance, game_state)
    print('[Lua Debug] Empire military button clicked...')
end

----------------------- Planet Class Label ------------------------------
function planet_class_label_on_create(widget, game_instance, game_state)
    print('[Lua Debug] PLanet class label hiding...')
end

function planet_class_label_on_show(widget, game_instance, game_state)
    print('[Lua Debug] Planet class label showing...')
        if game_instance:isPlanetSelected() then
            widget:setText('Planet Class ' .. 'no_class')
            print('No system selected.')
        else
            print('System selected')
            widget:setText('Planet Class ' .. game_instance:getSelectedPlanet():getPlanetClass():getTag())
        end
end

function planet_class_label_on_hide(widget, game_instance, game_state)
    print('[Lua Debug] PLanet class label hiding...')
end
--------------------------------------------------------------------------

function spawn_ship_btn_on_click(widget, game_instance, game_state)
    --Toggle the map to spawn ships.
    game_instance:toggleShipSpawnMode();
    game_instance:log('SpawnShipBtn Debug', 'Ship Spawn Mode ' .. tostring(game_instance:isSpawnModeEnabled()))
end
