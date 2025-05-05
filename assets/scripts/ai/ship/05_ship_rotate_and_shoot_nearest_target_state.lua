

function ship_rotate_and_shoot_nearest_target_state_on_enter(ship, game_instance, game_state)
    --print('[05_ship_rotate_and_shoot_target_state.on_enter] Begining combat!' .. at_location_dst)
    print('[ScriptingDebug: 05_ship_rotate_and_shoot_nearest_target_state.on_enter]: Ship ' .. ship:getID() .. ' is fighting.')
end

function ship_rotate_and_shoot_nearest_target_state_on_update(ship, game_instance, game_state)
    --Flies away from nearest ship until it leaves attack range.
    if game_instance:hasString("game_mode") and game_instance:getString("game_mode") == "ship_mode_move" then
        local target = ship:getTarget()
        local target_position = target:getPosition()
        --print('[ScriptingDebug: 05_ship_rotate_and_shoot_nearest_target_state.on_update]: Ship ' .. ship:getID() .. ' is fighting.')

        if ship:getFlag("is_orbiting_target") == "yes" then
            local combat_dest = ship:getVector("combat_dest")
            if combat_dest ~= nil then
                ship:turnTowards(combat_dest)
                ship:thrust(baseThrustValue)
                local dst_to_target = ship:getPosition():dst(ship:getTarget():getPosition())
                local dst_to_combat_dest = ship:getPosition():dst(combat_dest)
                if dst_to_combat_dest < dst_to_target then
                    --print('[ScriptingDebug: 05_ship_rotate_and_shoot_nearest_target_state.on_update]: Ship ' .. ship:getID() .. ' is preparing to scan for the nearest target.')
                    ship:getStateMachine():changeState(game_instance:getShipAIState('ship_search_nearest_target_state'))
                    ship:setFlag("is_orbiting_target", "no")
                end
            else
                print('this should never be reached.')
            end
        else
            local point = ship:getRandomPointInOrbit(target_position, combat_radius)
            ship:saveVector("combat_dest", point)
            ship:setFlag("is_orbiting_target", "yes")
            ship:turnTowards(point)
            ship:thrust(baseThrustValue)
        end
    else
        ship:stop()
    end
end

function ship_rotate_and_shoot_nearest_target_state_on_exit(ship, game_instance, game_state)
end

function ship_rotate_and_shoot_nearest_target_state_on_receive_message(ship, message, game_instance, game_state)
end
