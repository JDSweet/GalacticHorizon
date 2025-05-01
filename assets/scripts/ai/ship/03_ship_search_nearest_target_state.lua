function ship_search_nearest_target_state_on_enter(ship, game_instance, game_state)
    print('[ScriptingDebug: 03_ship_search_nearest_target_state.lua.on_enter] ' .. ship:getID() .. ' is earching for nearest target...')
    ship:stop()
end

function ship_search_nearest_target_state_on_update(ship, game_instance, game_state)
    if game_instance:hasString("game_mode") and game_instance:getString("game_mode") == "ship_mode_move" then
        --ship:thrust(baseThrustValue)
        --print('[ScriptingDebug: 03_ship_search_nearest_target_state.lua.on_update] Updating State')
        if ship == nil then
            print('[ScriptingDebug: 03_ship_search_nearest_target_state.lua.on_update] ship is a nil value. Update terminating.')
            return
        elseif ship:getSystemLocation() == nil then
            print('[ScriptingDebug: 03_ship_search_nearest_target_state.lua.on_update] System Location is a nil value. Update terminating.')
            return
        end
        local enemy_ships = ship:getSystemLocation():getEnemyShipsFor(ship:getOwnerID())
        local closest_target = nil
        local closest_distance = 0
        for i = 0, enemy_ships.size-1 do
            if enemy_ships.size <= 0 then
                print('No enemy ships found.')
            else
                local enemy = enemy_ships:get(i)
                local distance = game_instance:dst(ship:getPosition(), enemy:getPosition())
                if closest_target == nil or distance < closest_distance then
                    closest_target = enemy
                    closest_distance = distance
                end
            end
        end
        if closest_target ~= nil then
            ship:setTarget(closest_target)
        end
        if closest_distance <= ship:getShipClass():getCombatRange() and closest_target ~= nil then
            ship:getStateMachine():changeState(game_instance:getShipAIState('ship_rotate_and_shoot_nearest_target_state'))
            --print('[ScriptingDebug: 03_ship_search_nearest_target_state.lua.on_update] Changing state to <ship_rotate_and_shoot_nearest_target_state>')
        elseif closest_target ~= nil and closest_distance > ship:getShipClass():getCombatRange() then
            ship:getStateMachine():changeState(game_instance:getShipAIState('ship_approach_nearest_target_state'))
            --print('[ScriptingDebug: 03_ship_search_nearest_target_state.lua.on_update] Changing state to <ship_approach_nearest_target_state>')
        end
    else
        ship:stop()
    end
end

function ship_search_nearest_target_state_on_exit(ship, game_instance, game_state)
end

function ship_search_nearest_target_state_on_receive_message(ship, message, game_instance, game_state)
end
