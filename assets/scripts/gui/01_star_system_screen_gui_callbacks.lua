

--This sends us back to the galaxy screen.
--setRenderView is case insensitive.
--gameState is a reference to the currently active game state.
--widget is the widget that has been touched (in this case, our ScriptableTextButton).
function galaxy_screen_btn_on_click(self, game_instance, game_state)
    print('[Lua Debug] Galaxy screen button clicked!')
    game_instance:getCameraManager():setRenderView("GALACTIC");
end

--This updates the system text label on the scripted GUI screen.
--If no star system is selected, we just set the value to the default,
--But if a star system is selected, we set the text appropriately.
function system_name_label_on_show(self, game_instance, game_state)
    print('[Lua Debug] System name label showing...')
    if game_instance:isSystemSelected() then
        self.widget:setText('Star System #X');
        print('No system selected.')
    else
        print('System selected')
        self.widget:setText('Star System # ' .. game_instance:getSelectedStarSystem().id);
    end
end

function system_name_label_on_hide(self, game_instance, game_state)
    print('[Lua Debug] System name label hiding...')
end

------ Visit Planet Button ------

function visit_planet_btn_on_show(self, game_instance, game_state)
    print('[Lua Debug] Visit planet button showing...')
end

function visit_planet_btn_on_hide(self, game_instance, game_state)
    print('[Lua Debug] Visit planet button hiding...')
end

function visit_planet_btn_on_click(self, game_instance, game_state)
    print('[Lua Debug] Visit planet button clicked!')
    if game_instance:getSelectedPlanet():isHabitable() then
        game_instance:getCameraManager():setRenderView("PLANET");
    else
        print('[Lua Debug] Planet is uninhabitable, and thus cannot be visited.');
    end

end

---------------------------------

function planet_overview_window_on_create(self, game_instance, game_state)
    print('[Lua Debug] PLanet overview window being created...')
end

function planet_overview_window_on_show(self, game_instance, game_state)
    print('[Lua Debug] PLanet overview window showing...')
end

function planet_overview_window_on_hide(self, game_instance, game_state)
    print('[Lua Debug] PLanet overview window hiding...')
end

function planet_habitability_label_on_create(self, game_instance, game_state)
    print('[Lua Debug] PLanet habitability label being created...')
    if(game_instance:isPlanetSelected()) then
        self.widget:setText('Habitability: ' .. game_instance:getSelectedPlanet():getHabitability())
    end
end

function planet_habitability_label_on_show(self, game_instance, game_state)
    print('[Lua Debug] PLanet habitability label showing...')
end

function planet_habitability_label_on_hide(self, game_instance, game_state)
    print('[Lua Debug] PLanet habitability label hiding...')
end

-------------------------------------------------------------

function planet_size_label_on_create(self, game_instance, game_state)
    print('[Lua Debug] PLanet size label being created...')
end

function planet_size_label_on_show(self, game_instance, game_state)
    print('[Lua Debug] PLanet size label showing...')
end

function planet_size_label_on_hide(self, game_instance, game_state)
    print('[Lua Debug] PLanet size label hiding...')
end

--------------------------------------------------------------

function empire_management_btn_on_click(self, game_instance, game_state)
    print('[Lua Debug] Empire management button clicked...')
end

function empire_economy_btn_on_click(self, game_instance, game_state)
    print('[Lua Debug] Empire economy button clicked...')
end

function empire_military_btn_on_click(self, game_instance, game_state)
    print('[Lua Debug] Empire military button clicked...')
end

----------------------- Planet Class Label ------------------------------
function planet_class_label_on_create(self, game_instance, game_state)
    print('[Lua Debug] PLanet class label hiding...')
end

function planet_class_label_on_show(self, game_instance, game_state)
    print('[Lua Debug] Planet class label showing...')
        if game_instance:isPlanetSelected() then
            self.widget:setText('Planet Class ' .. 'no_class')
            print('No system selected.')
        else
            print('System selected')
            self.widget:setText('Planet Class ' .. game_instance:getSelectedPlanet():getPlanetClass():getTag())
        end
end

function planet_class_label_on_hide(self, game_instance, game_state)
    print('[Lua Debug] PLanet class label hiding...')
end
--------------------------------------------------------------------------

function spawn_ship_btn_on_click(self, game_instance, game_state)
    --Toggle the map to spawn ships.
    --game_instance:toggleShipSpawnMode();
    if game_instance:getString("game_mode") ~= "ship_mode_spawn" then
        game_instance:setString("game_mode", "ship_mode_spawn")
    else
        game_instance:setString("game_mode", "ship_mode_move")
    end
    --game_instance:log('SpawnShipBtn Debug', 'Ship Spawn Mode ' .. tostring(game_instance:isSpawnModeEnabled()))
    game_instance:log("[01_star_system_screen_gui_callbacks.spawn_ship_btn_on_click]", game_instance:getString("game_mode"))
end

--------------------------------------------------------------------------

function game_mode_select_box_on_create(self, game_instance, game_state)
    --print('System selected')
end

function game_mode_select_box_on_show(self, game_instance, game_state)
end

function game_mode_select_box_on_click(self, game_instance, game_state)
    if self == nil then
        print('[Select Box] Self is null!')
    end
    local selected = self.widget
    if(selected ~= nil) then
        local selection = selected:getSelected()
        print('Game mode set to ' .. selection)
        if selection == "ShipMoveMode" then
            game_instance:setString("game_mode", "ship_mode_move")
            print(game_instance:hasString("game_mode"))
        elseif selection == "ShipSpawnMode" then
            game_instance:setString("game_mode", "ship_mode_spawn")
            print(game_instance:hasString("game_mode"))
        elseif selection == "ShipSelectMode" then
            game_instance:setString("game_mode", "ship_mode_select")
            print(game_instance:hasString("game_mode"))
        end
    end
end

function game_mode_select_box_on_hide(self, game_instance, game_state)
end

--------------------------------------------------------------------------

function faction_select_box_on_click(self, game_instance, game_state)
    if self == nil then
        print('[Select Box] Self is null!')
    end
    local selected = self.widget
    if(selected ~= nil) then
        print('Faction set to ' .. selected:getSelected())
        local selection = selected:getSelected()
        if selection == "Faction 1" then
            game_instance:setString("selected_faction", "faction1")
        elseif selection == "Faction 2" then
            game_instance:setString("selected_faction", "faction2")
        elseif selection == "Faction 3" then
            game_instance:setString("selected_faction", "faction3")
        end
    end
end

----------------------- Ship Debug --------------------------------

function ship_debug_overview_window_on_create(self, game_instance, game_state)

end

function ship_debug_overview_window_on_show(self, game_instance, game_state)

end

function ship_debug_overview_window_on_hide(self, game_instance, game_state)

end

function ship_id_label_on_show(self, game_instance, game_state)
    self.widget:setText('Ship ID: ' .. game_instance:getSelectedStarSystem():getSelectedShip():getID());
end

function ship_id_label_on_hide(self, game_instance, game_state)

end

function ship_ai_state_label_on_create(self, game_instance, game_state)

end

function ship_ai_state_label_on_show(self, game_instance, game_state)
    print("showing " .. self:getDebugID())
end

function ship_ai_state_label_on_update(self, game_instance, game_state)
    print('Updating label')
    local curSystem = game_instance:getSelectedStarSystem()
    if curSystem ~= nil then
        local curShip = curSystem:getSelectedShip()
        if curShip ~= nil then
            local aiStateTag = curShip:getStateMachine():getCurrentState():getTag()
            local shipPrevAIState = curShip:getFlag("ship_prev_ai_state")
            if aiStateTag ~= shipPrevAIState then
                self.widget:setText("AI State: " .. aiStateTag)
                curShip:setFlag("ship_prev_ai_state", aiStateTag)
            end
        end
    end
end

function ship_ai_state_label_on_hide(self, game_instance, game_state)

end
